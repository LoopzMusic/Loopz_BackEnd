package dev.trier.ecommerce.dto.estoque.criacao;

public record EstoqueCriadoRespostaDto(

        Integer cdEstoque,

        Integer cdProduto,

        String nmProduto,

        Integer qtdEstoqueProduto

) {
}
