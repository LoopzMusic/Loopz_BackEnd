package dev.trier.ecommerce.dto.estoque.modificacao;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para atualização de estoque (PUT)")
public record EstoqueUpdateDto(
        @Schema(description = "ID do produto para associar ao estoque", example = "1")
        Integer cdProduto,

        @Schema(description = "Quantidade em estoque", example = "50")
        Integer qtdEstoqueProduto
) {}

