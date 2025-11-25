package dev.trier.ecommerce.dto.acoesUsuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para favoritar/desfavoritar um produto por usuário")
public record FavoritosCriarDto(
        @NotNull
        @Schema(description = "Código do usuário", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer cdUsuario,

        @NotNull
        @Schema(description = "Código do produto", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer cdProduto
) {}

