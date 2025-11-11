package dev.trier.ecommerce.dto.produto.criacao;

import dev.trier.ecommerce.model.enums.CategoriaProduto;

public record CriarProdutoResponseDto(
        Integer cdProduto,
        String nmProduto,
        Double vlProduto,
        CategoriaProduto dsCategoria,
        String dsProduto,
        Integer cdEmpresa
) {
}
