package dev.trier.ecommerce.dto.itempedido.criacao;

public record ListarItensPedidosResponseDto(
        Integer cdPedido,
        Double vlItemPedido,
        Integer qtItem
) {
}
