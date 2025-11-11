package dev.trier.ecommerce.dto.pedido.criacao;

import dev.trier.ecommerce.model.enums.FormaPagamento;

public record PedidoCriarResponseDto(
        Integer cdPedido,
        FormaPagamento formaPagamento,
        Double vlFrete,
        Double vlTotalPedido
) {
}
