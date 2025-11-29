package dev.trier.ecommerce.dto.produto.request;

public record UpdateRequestDto(
        String nmProduto,
        Double vlProduto,
        String dsProduto,
        String dsCategoria,
        Integer cdEmpresa
) {}
