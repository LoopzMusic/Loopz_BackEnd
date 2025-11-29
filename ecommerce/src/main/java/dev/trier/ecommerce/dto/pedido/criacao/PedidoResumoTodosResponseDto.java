package dev.trier.ecommerce.dto.pedido.criacao;

import dev.trier.ecommerce.dto.pedido.ItemPedidoResponseDto;

import java.time.LocalDate;
import java.util.List;

public record PedidoResumoTodosResponseDto(
        Integer cdPedido,
        Double vlTotalPedido,
        String nmCliente,
        LocalDate dtFinalizacao,
        List<ItemPedidoResponseDto> itens
) {
}
