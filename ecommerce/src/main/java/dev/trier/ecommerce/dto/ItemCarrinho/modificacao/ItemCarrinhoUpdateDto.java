package dev.trier.ecommerce.dto.ItemCarrinho.modificacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemCarrinhoUpdateDto(
        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser positiva.")
        Integer qtdItemCarrinho
) {}