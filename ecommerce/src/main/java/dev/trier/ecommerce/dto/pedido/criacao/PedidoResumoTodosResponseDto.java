package dev.trier.ecommerce.dto.pedido.criacao;

import dev.trier.ecommerce.dto.pedido.ItemPedidoResponseDto;
import dev.trier.ecommerce.model.enums.StatusPedido;

import java.time.LocalDate;
import java.util.List;

public record PedidoResumoTodosResponseDto(
        Integer cdPedido,
        Double vlTotalPedido,
        StatusPedido statusPedido,
        String nmCliente,
        LocalDate dtFinalizacao,
        List<ItemPedidoResponseDto> itens
) {
}
