package dev.trier.ecommerce.dto.carrinho.response;

import dev.trier.ecommerce.model.enums.StatusCarrinho;
import dev.trier.ecommerce.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CarrinhoResponseDto(
        Integer cdCarrinho,
        Integer cdUsuario,
        String nomeUsuario,
        List<ItemCarrinhoDto> itens,
        StatusCarrinho status,
        BigDecimal valorTotalCarrinho,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) { }
