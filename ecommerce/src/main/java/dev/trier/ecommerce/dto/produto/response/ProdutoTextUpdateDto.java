package dev.trier.ecommerce.dto.produto.response;

import jakarta.validation.constraints.Size;


public record ProdutoTextUpdateDto(
        @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres.")
        String nmProduto,

        Double vlProduto,

        @Size(max = 500, message = "A descrição do produto não pode exceder 500 caracteres.")
        String dsProduto,

        String dsCategoria,

        String dsAcessorio,

        Integer cdEmpresa,

        Integer qtdEstoque
) {}
