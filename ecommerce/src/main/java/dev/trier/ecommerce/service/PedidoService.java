package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeRequest;
import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeResponse;
import dev.trier.ecommerce.dto.pedido.criacao.ListarPedidosResponseDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoCriarDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoCriarResponseDto;
import dev.trier.ecommerce.dto.pedido.ItemPedidoResponseDto;
import dev.trier.ecommerce.dto.pedido.PedidoResumoResponseDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoResumoTodosResponseDto;
import dev.trier.ecommerce.model.enums.StatusPedido;
import dev.trier.ecommerce.service.CarrinhoService;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.PedidoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.repository.PedidoRepository;
import dev.trier.ecommerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AbacatePayService abacatePayService;
    private final CarrinhoService carrinhoService;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoCriarResponseDto criarPedido(PedidoCriarDto pedidoCriarDto) {
        UsuarioModel usuarioModel = usuarioRepository.findById(pedidoCriarDto.cdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para o código: " + pedidoCriarDto.cdUsuario()));

        PedidoModel pedidoModel = new PedidoModel();
        pedidoModel.setUsuario(usuarioModel);
        pedidoModel.setFormaPagamento(pedidoCriarDto.formaPagamento());
        pedidoModel.setVlFrete(pedidoCriarDto.vlFrete());
        pedidoModel.setVlTotalPedido(pedidoCriarDto.vlTotalPedido());

        PedidoModel salvo = pedidoRepository.save(pedidoModel);

        String urlPagamento = null;

        log.info("Forma de pagamento recebida: {}", pedidoCriarDto.formaPagamento().name());

        if ("PIX".equalsIgnoreCase(pedidoCriarDto.formaPagamento().name())) {
            try {
                log.info("Criando cobrança no AbacatePay para pedido #{}", salvo.getCdPedido());
                AbacatePayChargeRequest chargeRequest = createChargeRequest(salvo);
                AbacatePayChargeResponse chargeResponse = abacatePayService.createCharge(chargeRequest).block();

                if (chargeResponse != null && chargeResponse.getData() != null) {
                    urlPagamento = chargeResponse.getData().getUrl();
                    log.info("URL de pagamento gerada com sucesso: {}", urlPagamento);
                } else {
                    log.error("Falha ao criar cobrança - response null ou data null. Response: {}", chargeResponse);
                }
            } catch (Exception e) {
                log.error("Erro ao criar cobrança no AbacatePay: {}", e.getMessage(), e);
            }
        } else {
            log.info("Forma de pagamento não é PIX, pulando AbacatePay");
        }

        PedidoCriarResponseDto response = new PedidoCriarResponseDto(
                salvo.getCdPedido(),
                salvo.getFormaPagamento(),
                salvo.getVlFrete(),
                salvo.getVlTotalPedido(),
                urlPagamento
        );

        log.info("Retornando resposta para frontend - cdPedido: {}, urlPagamento: {}",
                response.cdPedido(), response.urlPagamento());

        return response;
    }

    @Transactional
    public void processarRetornoPagamento(String externalId, Boolean sucesso) {
        log.info("Processando retorno de pagamento para externalId: {}, sucesso: {}", externalId, sucesso);

        if (!externalId.startsWith("PEDIDO-")) {
            log.error("ExternalId inválido: {}", externalId);
            return;
        }

        Integer cdPedido = Integer.parseInt(externalId.substring("PEDIDO-".length()));

        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o código: " + cdPedido));

        if (Boolean.TRUE.equals(sucesso)) {
            // Pagamento bem-sucedido
            pedido.setStatusPedido(StatusPedido.FINALIZADO);
            pedido.setDtFinalizacao(java.time.LocalDate.now());
            log.info("Pedido #{} atualizado para FINALIZADO.", cdPedido);
            // A limpeza do carrinho e a criação dos itens do pedido devem ocorrer no frontend
            // após o redirecionamento, ou o backend deve lidar com isso.
            // Pelo código do frontend, a criação dos itens e a finalização do carrinho
            // ocorrem ANTES do redirecionamento para o AbacatePay.
            // O bug é que o pedido é criado e os itens são criados, mas o status
            // não é alterado para FINALIZADO/CANCELADO.
            // Como o frontend já criou os itens, vamos apenas finalizar o pedido.
            // O frontend precisa ser ajustado para não limpar o carrinho
            // antes de ter certeza do sucesso do pagamento.
        } else {
            // Pagamento falhou ou foi cancelado
            pedido.setStatusPedido(StatusPedido.CANCELADO); // Adicionar CANCELADO ao enum StatusPedido
            log.warn("Pedido #{} atualizado para CANCELADO.", cdPedido);
            // O carrinho deve ser mantido.
            // O frontend precisa ser ajustado para não limpar o carrinho
            // antes de ter certeza do sucesso do pagamento.
        }

        pedidoRepository.save(pedido);
    }

    private AbacatePayChargeRequest createChargeRequest(PedidoModel pedido) {
        UsuarioModel usuario = pedido.getUsuario();

        if (usuario.getDsEmail() == null || usuario.getDsEmail().isBlank()) {
            throw new IllegalArgumentException("Email do usuário é obrigatório para criar cobrança");
        }
        if (usuario.getNmCliente() == null || usuario.getNmCliente().isBlank()) {
            throw new IllegalArgumentException("Nome do usuário é obrigatório para criar cobrança");
        }

        AbacatePayChargeRequest.Product product = new AbacatePayChargeRequest.Product(
                "PEDIDO-" + pedido.getCdPedido(),
                "Compra na Loopz E-commerce",
                "Pedido #" + pedido.getCdPedido(),
                1,
                (int) (pedido.getVlTotalPedido() * 100)
        );

        AbacatePayChargeRequest.Customer customer = new AbacatePayChargeRequest.Customer(
                usuario.getNmCliente(),
                usuario.getNuTelefone() != null ? usuario.getNuTelefone() : "",
                usuario.getDsEmail(),
                usuario.getNuCPF() != null ? usuario.getNuCPF() : ""
        );

        log.info("Criando request AbacatePay - Pedido: {}, Valor: {}, Cliente: {}",
                pedido.getCdPedido(), pedido.getVlTotalPedido(), usuario.getNmCliente());


        return new AbacatePayChargeRequest(
                "ONE_TIME",
                List.of("PIX"),
                List.of(product),
                "http://localhost:8085/pedido/retorno-pagamento?externalId=PEDIDO-" + pedido.getCdPedido() + "&sucesso=false",
                "http://localhost:8085/pedido/retorno-pagamento?externalId=PEDIDO-" + pedido.getCdPedido() + "&sucesso=true",
                customer,
                "PEDIDO-" + pedido.getCdPedido(),
                java.util.Collections.emptyMap()
        );
    }

    public List<ListarPedidosResponseDto> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidos -> new ListarPedidosResponseDto(
                        pedidos.getUsuario().getCdUsuario(),
                        pedidos.getFormaPagamento(),
                        pedidos.getVlFrete(),
                        pedidos.getStatusPedido(),
                        pedidos.getVlTotalPedido()
                ))
                .toList();
    }

    @Transactional()
    public List<PedidoResumoResponseDto> listarPedidosDoUsuarioPorId(Integer cdUsuario) {
        if (!usuarioRepository.existsById(cdUsuario)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado: " + cdUsuario);
        }

        List<PedidoModel> pedidos = pedidoRepository.findByCdUsuarioComItem(cdUsuario)
                .stream()
                .filter(pedido -> pedido.getStatusPedido() == StatusPedido.FINALIZADO)
                .toList();

        return pedidos.stream().map(pedido -> {
            List<ItemPedidoResponseDto> itens = pedido.getItensPedido() == null ? List.of() :
                    pedido.getItensPedido().stream().map(item -> {
                        Integer cdProduto = null;
                        String nmProduto = null;

                        if (item.getProduto() != null) {
                            cdProduto = item.getProduto().getCdProduto();
                            nmProduto = item.getProduto().getNmProduto();
                        }

                        Integer quantidade = item.getQtItem();
                        Double precoPorProdutoUnidade = item.getVlItemPedido();
                        Double total = (precoPorProdutoUnidade == null || quantidade == null) ? 0.0 : precoPorProdutoUnidade * quantidade;

                        return new ItemPedidoResponseDto(cdProduto, nmProduto, quantidade, precoPorProdutoUnidade, total);
                    }).toList();

            return new PedidoResumoResponseDto(pedido.getCdPedido(), pedido.getVlTotalPedido(),pedido.getDtFinalizacao(), itens);
        }).toList();
    }

    @Cacheable(value = "todosPedidosCache")
    @Transactional
    public List<PedidoResumoTodosResponseDto> listarTodosPedidos() {

        List<PedidoModel> pedidos = pedidoRepository.findAllComItens();

        return pedidos.stream().map(pedido -> {

            String nmCliente = pedido.getUsuario() != null
                    ? pedido.getUsuario().getNmCliente()
                    : null;

            List<ItemPedidoResponseDto> itens = pedido.getItensPedido() == null
                    ? List.of()
                    : pedido.getItensPedido().stream().map(item -> {

                Integer cdProduto = null;
                String nmProduto = null;

                if (item.getProduto() != null) {
                    cdProduto = item.getProduto().getCdProduto();
                    nmProduto = item.getProduto().getNmProduto();
                }

                Integer quantidade = item.getQtItem();
                Double precoPorProdutoUnidade = item.getVlItemPedido();
                Double total = (precoPorProdutoUnidade == null || quantidade == null)
                        ? 0.0
                        : precoPorProdutoUnidade * quantidade;

                return new ItemPedidoResponseDto(
                        cdProduto,
                        nmProduto,
                        quantidade,
                        precoPorProdutoUnidade,
                        total
                );
            }).toList();

            return new PedidoResumoTodosResponseDto(
                    pedido.getCdPedido(),
                    pedido.getVlTotalPedido(),
                    nmCliente,
                    pedido.getDtFinalizacao(),
                    itens
            );

        }).toList();
    }
}