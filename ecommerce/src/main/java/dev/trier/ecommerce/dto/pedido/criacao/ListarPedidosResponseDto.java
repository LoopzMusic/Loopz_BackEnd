package dev.trier.ecommerce.dto.pedido.criacao;

import dev.trier.ecommerce.model.enums.FormaPagamento;
import dev.trier.ecommerce.model.enums.StatusPedido;

public record ListarPedidosResponseDto(
        Integer cdUsuario,
        FormaPagamento formaPagamento,
        Double vlFrete,
        StatusPedido statusPedido,
        Double vlTotalPedido

) {
}
