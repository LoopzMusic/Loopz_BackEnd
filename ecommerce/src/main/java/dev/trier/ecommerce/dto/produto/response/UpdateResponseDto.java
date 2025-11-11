package dev.trier.ecommerce.dto.produto.response;

public record UpdateResponseDto(
        String nmProduto,
        Double vlProduto,
        String dsProduto
) {
}
