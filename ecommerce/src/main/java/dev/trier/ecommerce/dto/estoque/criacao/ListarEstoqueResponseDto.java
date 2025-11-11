package dev.trier.ecommerce.dto.estoque.criacao;

public record ListarEstoqueResponseDto(
        Integer cdEstoque,
        Integer qtdEstoqueProduto,
        Integer cdProduto,
        String nmProduto,
        String flAtivo
) {
}
