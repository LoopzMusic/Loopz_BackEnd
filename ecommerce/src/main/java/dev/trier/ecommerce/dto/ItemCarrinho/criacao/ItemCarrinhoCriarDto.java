package dev.trier.ecommerce.dto.ItemCarrinho.criacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemCarrinhoCriarDto(
        @NotNull(message = "O código do produto é obrigatório")
        Integer cdProduto,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer qtdItemCarrinho
) {}
