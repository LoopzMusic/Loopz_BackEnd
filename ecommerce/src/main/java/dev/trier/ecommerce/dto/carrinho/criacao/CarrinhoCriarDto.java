package dev.trier.ecommerce.dto.carrinho.criacao;

import jakarta.validation.constraints.NotNull;

public record CarrinhoCriarDto(
        @NotNull(message = "O código de usuário é obrigatório")
        Integer cdUsuario
) {
}
