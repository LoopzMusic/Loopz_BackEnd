package dev.trier.ecommerce.dto.pedido.administrativo;

public record PedidoAdminItemDto(
        Integer cdProduto,
        String nmProduto,
        Double vlProduto,
        Integer qtdProduto
) {}
