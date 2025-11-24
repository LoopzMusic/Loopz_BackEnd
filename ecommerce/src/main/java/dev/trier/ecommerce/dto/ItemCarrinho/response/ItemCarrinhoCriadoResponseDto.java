package dev.trier.ecommerce.dto.ItemCarrinho.response;

public record ItemCarrinhoCriadoResponseDto(
        Integer cdItemCarrinho,
        Integer cdCarrinho,
        Integer cdProduto,
        String nomeProduto,
        Integer qtdItemCarrinho
) {}
