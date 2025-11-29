package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.itempedido.criacao.ItemPedidoCriadoRespostaDto;
import dev.trier.ecommerce.dto.itempedido.criacao.ItemPedidoCriarDto;
import dev.trier.ecommerce.dto.itempedido.criacao.ListarItensPedidosResponseDto;
import dev.trier.ecommerce.model.ItemPedidoModel;
import dev.trier.ecommerce.model.PedidoModel;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.enums.StatusPedido;
import dev.trier.ecommerce.repository.ItemPedidoRepository;
import dev.trier.ecommerce.repository.PedidoRepository;
import dev.trier.ecommerce.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.text.StringEscapeUtils;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRespository;
    private final PedidoRepository pedidoRepository;
    private final EstoqueService estoqueService;
    private final EmailService emailService;


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


        try {
            if (pedidoModel.getStatusPedido() == null || pedidoModel.getStatusPedido() != StatusPedido.FINALIZADO) {
                pedidoModel.setStatusPedido(StatusPedido.ANDAMENTO);
                pedidoRepository.save(pedidoModel);
            }
        } catch (Exception ex) {
            System.out.printf("Erro ao atualizar status/dtFinalizacao do pedido: %s%n", ex.getMessage());
        }


        try {
            if (pedidoModel != null && pedidoModel.getUsuario() != null && pedidoModel.getUsuario().getDsEmail() != null) {
                String destinatario = pedidoModel.getUsuario().getDsEmail();
                String assunto = "Pedido confirmado - Pedido #" + (pedidoModel.getCdPedido() != null ? pedidoModel.getCdPedido() : "");
                String nmProduto = produtoModel != null ? produtoModel.getNmProduto() : "";
                Integer quantidade = salvar.getQtItem();
                Double valorTotalPedido = pedidoModel.getVlTotalPedido();


                final String emailHtml = String.format(
                        "<html><body><meta charset='UTF-8'><h3>Seu pedido foi confirmado!</h3>"
                                + "<p><strong>Produto:</strong> %s</p>"
                                + "<p><strong>Quantidade:</strong> %d</p>"
                                + "<p><strong>Valor total do pedido:</strong> R$ %.2f</p>"
                                + "<p>Obrigado por comprar conosco! 😊</p></body></html>",
                        escapeHtml(nmProduto),
                        quantidade != null ? quantidade : 0,
                        valorTotalPedido != null ? valorTotalPedido : 0.0
                );


                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            CompletableFuture<String> envio = emailService.enviarEmailHtmlAsync(destinatario, assunto, emailHtml);
                            envio.thenAccept(resultado -> System.out.printf("Envio de email concluído: %s%n", resultado))
                                  .exceptionally(ex -> {
                                      System.out.printf("Erro assíncrono ao enviar email: %s%n", ex.getMessage());
                                      return null;
                                  });
                        } catch (Exception ex) {
                            System.out.printf("Erro ao agendar envio de email: %s%n", ex.getMessage());
                        }
                    }
                });
            }
        } catch (Exception e) {

            System.out.printf("Erro ao disparar envio de email de confirmação: %s%n", e.getMessage());
        }

        return new ItemPedidoCriadoRespostaDto(
                salvar.getCdItemPedido(),
                salvar.getPedido().getCdPedido(),
                salvar.getProduto().getCdProduto(),
                salvar.getVlItemPedido(),
                salvar.getQtItem()

        );
    }

    private String escapeHtml(String nmProduto) {
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
