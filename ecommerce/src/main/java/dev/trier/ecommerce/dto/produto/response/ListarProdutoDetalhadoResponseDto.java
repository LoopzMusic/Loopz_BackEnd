package dev.trier.ecommerce.dto.produto.response;

import dev.trier.ecommerce.model.enums.CategoriaProduto;

public record ListarProdutoDetalhadoResponseDto(
        Integer cdProduto,
        String nmProduto,
        Double vlProduto,
        String dsCategoria,
        String dsProduto,
        String dsAcessorio,
        Integer cdEmpresa,
        Integer qtdEstoqueProduto

) {

    public ListarProdutoDetalhadoResponseDto(
            Integer cdProduto,
            String nmProduto,
            Double vlProduto,
            CategoriaProduto dsCategoria,
            String dsProduto,
            String dsAcessorio,
            Integer cdEmpresa,
            Integer qtdEstoqueProduto
            ) {
        this(cdProduto, nmProduto, vlProduto, dsCategoria.toString(), dsProduto, dsAcessorio, cdEmpresa, qtdEstoqueProduto);
    }

}