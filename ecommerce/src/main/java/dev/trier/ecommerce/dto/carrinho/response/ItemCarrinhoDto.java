package dev.trier.ecommerce.dto.carrinho.response;

import java.math.BigDecimal;

public record ItemCarrinhoDto(
        Integer cdItemCarrinho,
        Integer cdProduto,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotalItem
) {}