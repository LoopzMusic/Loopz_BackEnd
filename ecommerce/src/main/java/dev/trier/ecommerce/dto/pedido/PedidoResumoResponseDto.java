package dev.trier.ecommerce.dto.pedido;

import java.time.LocalDate;
import java.util.List;

public record PedidoResumoResponseDto(
        Integer cdPedido,
        Double valorTotal,
        LocalDate dtFinalizacao,
        List<ItemPedidoResponseDto> itens
) {}

