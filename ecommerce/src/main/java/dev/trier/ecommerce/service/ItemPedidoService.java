package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.itempedido.criacao.ItemPedidoCriadoRespostaDto;
import dev.trier.ecommerce.dto.itempedido.criacao.ItemPedidoCriarDto;
import dev.trier.ecommerce.dto.itempedido.criacao.ListarItensPedidosResponseDto;
import dev.trier.ecommerce.model.ItemPedidoModel;
import dev.trier.ecommerce.model.PedidoModel;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.repository.ItemPedidoRepository;
import dev.trier.ecommerce.repository.PedidoRepository;
import dev.trier.ecommerce.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRespository;
    private final PedidoRepository pedidoRepository;
    private final EstoqueService estoqueService;
    private final EmailService emailService; // ...existing code... add EmailService


    @Transactional
    public ItemPedidoCriadoRespostaDto criarItemPedido(ItemPedidoCriarDto itemPedidoCriarDto) {

        ProdutoModel produtoModel = produtoRespository.findById(itemPedidoCriarDto.cdProduto())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado para o código: " + itemPedidoCriarDto.cdProduto()));
        PedidoModel pedidoModel = pedidoRepository.findById(itemPedidoCriarDto.cdPedido())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado para o código: " + itemPedidoCriarDto.cdPedido()));

        estoqueService.diminuirEstoqueProduto(itemPedidoCriarDto.cdProduto(),itemPedidoCriarDto.qtItem());

        ItemPedidoModel itemPedidoModel = new ItemPedidoModel();
        itemPedidoModel.setProduto(produtoModel);
        itemPedidoModel.setPedido(pedidoModel);
        itemPedidoModel.setVlItemPedido(itemPedidoCriarDto.vlItemPedido());
        itemPedidoModel.setQtItem(itemPedidoCriarDto.qtItem());

        ItemPedidoModel salvar=  itemPedidoRepository.save(itemPedidoModel);

        //rever para retirar try/cath devido a verificação do 2auth
        try {
            if (pedidoModel != null && pedidoModel.getUsuario() != null && pedidoModel.getUsuario().getDsEmail() != null) {
                String destinatario = pedidoModel.getUsuario().getDsEmail();
                String assunto = "Pedido confirmado - Pedido #" + (pedidoModel.getCdPedido() != null ? pedidoModel.getCdPedido() : "");
                String nmProduto = produtoModel != null ? produtoModel.getNmProduto() : "";
                Integer quantidade = salvar.getQtItem();
                Double valorTotalPedido = pedidoModel.getVlTotalPedido();


                StringBuilder emailHtml = new StringBuilder();
                emailHtml.append("<html><body>");
                emailHtml.append("<meta charset='UTF-8'>");
                emailHtml.append("<h3>Seu pedido foi confirmado!</h3>");
                emailHtml.append(String.format("<p><strong>Produto:</strong> %s</p>", escapeHtml(nmProduto)));
                emailHtml.append(String.format("<p><strong>Quantidade:</strong> %d</p>", quantidade != null ? quantidade : 0));
                emailHtml.append(String.format("<p><strong>Valor total do pedido:</strong> R$ %.2f</p>", valorTotalPedido != null ? valorTotalPedido : 0.0));
                emailHtml.append("<p>Obrigado por comprar conosco! 😊</p>");
                emailHtml.append("</body></html>");

                emailService.enviarEmailHtml(destinatario, assunto, emailHtml.toString());
            }
        } catch (Exception e) {
            System.out.printf("Erro ao enviar email de confirmação: %s%n", e.getMessage());
        }

        return new ItemPedidoCriadoRespostaDto(
                salvar.getCdItemPedido(),
                salvar.getPedido().getCdPedido(),
                salvar.getProduto().getCdProduto(),
                salvar.getVlItemPedido(),
                salvar.getQtItem()

        );
    }

    private Object escapeHtml(String nmProduto) {
        return StringEscapeUtils.escapeHtml4(nmProduto);
    }

    public List<ListarItensPedidosResponseDto> listaItemPedidos() {
        return itemPedidoRepository.findAll()
                .stream()
                .map(itemPedidoModel -> new ListarItensPedidosResponseDto(
                        itemPedidoModel.getPedido().getCdPedido(),
                        itemPedidoModel.getVlItemPedido(),
                        itemPedidoModel.getQtItem()
                ))
                .toList();
    }

}
