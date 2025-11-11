package dev.trier.ecommerce.dto.pedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDto(
        Integer cdProduto,
        String nmProduto,
        Integer quantidade,
        Double precoPorProdutoUnidade,
        Double total
) {}
