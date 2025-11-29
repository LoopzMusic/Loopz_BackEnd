package dev.trier.ecommerce.dto.pedido.administrativo;

import dev.trier.ecommerce.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoAdminResponseDto(
        Integer cdPedido,
        String nmCliente,
        StatusPedido statusPedido,
        Integer quantidadeItens,
        List<PedidoAdminItemDto> itens
) {}
