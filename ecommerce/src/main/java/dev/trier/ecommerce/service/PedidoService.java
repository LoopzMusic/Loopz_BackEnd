package dev.trier.ecommerce.service;

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

@AllArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    //Metodo Criar Pedido
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

        return new PedidoCriarResponseDto(
                salvo.getCdPedido(),
                salvo.getFormaPagamento(),
                salvo.getVlFrete(),
                salvo.getVlTotalPedido()
        );
    }

    //Metodo Listar Pedidos //SOMENTE PARA ADMIN
    public List<ListarPedidosResponseDto> listarPedidos(){
        return pedidoRepository.findAll()
                .stream()
                .map(pedidos-> new ListarPedidosResponseDto(
                        pedidos.getUsuario().getCdUsuario(),
                        pedidos.getFormaPagamento(),
                        pedidos.getVlFrete(),
                        pedidos.getVlTotalPedido()
                ))
                .toList();
    }

    // Novo: listar pedidos do usuário autenticado
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
