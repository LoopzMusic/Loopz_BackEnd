package dev.trier.ecommerce.dto.pedido;

import dev.trier.ecommerce.model.enums.StatusPedido;

import java.time.LocalDate;
import java.util.List;

public record PedidoResumoResponseDto(
        Integer cdPedido,
        Double valorTotal,
        StatusPedido statusPedido,
        LocalDate dtFinalizacao,
        List<ItemPedidoResponseDto> itens
) {}

