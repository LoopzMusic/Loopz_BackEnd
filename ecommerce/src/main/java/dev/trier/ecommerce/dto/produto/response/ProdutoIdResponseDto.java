package dev.trier.ecommerce.dto.produto.response;

import dev.trier.ecommerce.model.enums.CategoriaProduto;

public record ProdutoIdResponseDto(
        String nmProduto,
        Double vlProduto,
        String dsProduto,
        CategoriaProduto categoria
) {
}
