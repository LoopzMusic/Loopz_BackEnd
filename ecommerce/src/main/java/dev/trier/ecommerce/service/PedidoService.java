package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeRequest;
import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeResponse;
import dev.trier.ecommerce.dto.pedido.criacao.ListarPedidosResponseDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoCriarDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoCriarResponseDto;
import dev.trier.ecommerce.dto.pedido.ItemPedidoResponseDto;
import dev.trier.ecommerce.dto.pedido.PedidoResumoResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.PedidoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.repository.PedidoRepository;
import dev.trier.ecommerce.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.List;

@AllArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AbacatePayService abacatePayService;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoCriarResponseDto criarPedido(PedidoCriarDto pedidoCriarDto){
        UsuarioModel usarioModel = usuarioRepository.findById(pedidoCriarDto.cdUsuario()).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado para o código: " + pedidoCriarDto.cdUsuario()) // Procura usuário antes de criar o pedidoCriarDto
        );

        PedidoModel pedidoModel = new PedidoModel();
        pedidoModel.setUsuario(usarioModel);
        pedidoModel.setFormaPagamento(pedidoCriarDto.formaPagamento());
        pedidoModel.setVlFrete(pedidoCriarDto.vlFrete());
        pedidoModel.setVlTotalPedido(pedidoCriarDto.vlTotalPedido());

        PedidoModel salvo = pedidoRepository.save(pedidoModel);

        // 1. Criar a requisição de cobrança para o AbacatePay
        AbacatePayChargeRequest chargeRequest = createChargeRequest(salvo);

        // 2. Enviar a requisição para o AbacatePay
        // Usamos block() aqui para simplificar a integração com o método @Transactional síncrono.
        // Em um cenário ideal, o método criarPedido seria assíncrono ou usaria um fluxo reativo completo.
        AbacatePayChargeResponse chargeResponse = abacatePayService.createCharge(chargeRequest).block();

        // 3. Processar a resposta (Exemplo: salvar a URL de pagamento e o ID da cobrança)
        if (chargeResponse != null && chargeResponse.getData() != null) {
            // Aqui você salvaria o ID da cobrança (chargeResponse.getData().getId())
            // e a URL de pagamento (chargeResponse.getData().getUrl()) no seu PedidoModel
            // para que o frontend possa redirecionar o usuário.
            // Por enquanto, vamos apenas retornar a URL de pagamento no DTO de resposta.
            return new PedidoCriarResponseDto(
                    salvo.getCdPedido(),
                    salvo.getFormaPagamento(),
                    salvo.getVlFrete(),
                    salvo.getVlTotalPedido(),
                    chargeResponse.getData().getUrl() // Adicionando a URL de pagamento
            );
        } else {
            // Tratar erro na criação da cobrança
            throw new RuntimeException("Falha ao criar cobrança no AbacatePay.");
        }
    }

    // Método auxiliar para construir a requisição de cobrança
    private AbacatePayChargeRequest createChargeRequest(PedidoModel pedido) {
        // Implementação simplificada, assumindo que o PedidoModel tem os itens
        // **ATENÇÃO**: O PedidoModel precisa ter os itens carregados para que isso funcione.
        // Se os itens não estiverem carregados, será necessário buscar o carrinho/itens.

        // Exemplo de produto (simplificado, pois não temos a estrutura completa do PedidoModel)
        AbacatePayChargeRequest.Product product = new AbacatePayChargeRequest.Product(
                "PEDIDO-" + pedido.getCdPedido(),
                "Compra na Loopz E-commerce",
                "Pedido #" + pedido.getCdPedido() + " - Total: R$ " + pedido.getVlTotalPedido(),
                1, // Quantidade total do pedido como 1 item
                (int) (pedido.getVlTotalPedido() * 100) // Valor total em centavos
        );

        // Exemplo de cliente (simplificado)
        AbacatePayChargeRequest.Customer customer = new AbacatePayChargeRequest.Customer(
                pedido.getUsuario().getNmCliente(),
                null, // Telefone
                pedido.getUsuario().getDsEmail(),
                null // CPF/CNPJ
        );

        return new AbacatePayChargeRequest(
                "ONE_TIME",
                List.of("PIX"),
                List.of(product),
                "http://localhost:4200/payment/return", // URL de retorno (ajustar para o frontend)
                "http://localhost:4200/payment/success", // URL de sucesso (ajustar para o frontend)
                customer,
                "PEDIDO-" + pedido.getCdPedido(),
                null
        );
    }

    public List<ListarPedidosResponseDto> listarPedidos(){
        return pedidoRepository.findAll()
                .stream()
                .map(pedidos-> new ListarPedidosResponseDto(
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

        List<PedidoModel> pedidos = pedidoRepository.findByCdUsuarioComItem(cdUsuario);

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

            return new PedidoResumoResponseDto(pedido.getCdPedido(), pedido.getVlTotalPedido(), itens);
        }).toList();
    }
}