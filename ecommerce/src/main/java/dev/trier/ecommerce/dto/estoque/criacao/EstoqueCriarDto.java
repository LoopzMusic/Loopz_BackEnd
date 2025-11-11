package dev.trier.ecommerce.dto.estoque.criacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de um novo estoque.")
public record EstoqueCriarDto(
    @NotNull(message = "O código do produto é obrigatório.")
    @Schema(description = "ID do produto relacionado ao estoque", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer cdProduto,

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    @Schema(description = "Quantidade de produtos em estoque", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer qtdEstoqueProduto
) {}
