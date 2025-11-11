package dev.trier.ecommerce.dto.pedido;

import java.util.List;

public record PedidoResumoResponseDto(
        Integer cdPedido,
        Double valorTotal,
        List<ItemPedidoResponseDto> itens
) {}

