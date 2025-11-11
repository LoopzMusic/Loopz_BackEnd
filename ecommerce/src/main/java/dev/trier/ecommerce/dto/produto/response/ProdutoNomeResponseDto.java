package dev.trier.ecommerce.dto.produto.response;

import dev.trier.ecommerce.model.EmpresaModel;
import dev.trier.ecommerce.model.enums.CategoriaProduto;

public record ProdutoNomeResponseDto(
        String nmProduto,
        Double vlProduto,
        CategoriaProduto dsCategoria,
        String dsProduto
) {
}
