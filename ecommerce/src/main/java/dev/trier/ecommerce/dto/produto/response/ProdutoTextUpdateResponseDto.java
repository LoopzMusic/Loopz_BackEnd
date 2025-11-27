package dev.trier.ecommerce.dto.produto.response;


public record ProdutoTextUpdateResponseDto(
        String nmProduto,
        Double vlProduto,
        String dsProduto,
        String dsCategoria,
        String dsAcessorio,
        Integer cdEmpresa,
        Integer qtdEstoque
) {}
