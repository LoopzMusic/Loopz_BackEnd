package dev.trier.ecommerce.dto.carrinho.criacao;

import dev.trier.ecommerce.model.enums.StatusPedido;

import java.time.LocalDateTime;

public record CarrinhoCriadoResponseDto(
        Integer cdCarrinho,
        Integer cdUsuario,
        StatusPedido status,
        LocalDateTime criadoEm
) {}
