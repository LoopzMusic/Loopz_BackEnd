package dev.trier.ecommerce.dto.feedback;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


@Schema(description = "DTO para de um feedback.")
public record FeedbackRequestDto (

    @Schema(description = "Código do usuário que realizou a avaliação.",
            example = "12",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O ID do usuário é obrigatório.")
    @Positive(message = "O ID do usuário deve ser positivo.")
    Integer cdUsuario,

    @Schema(description = "Código do produto avaliado.",
            example = "99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "O ID do produto é obrigatório.")
    @Positive(message = "O ID do produto deve ser positivo.")
    Integer cdProduto,

    @Schema(description = "Nota dada ao produto, variando de 1 a 5.",
            example = "4",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "A avaliação é obrigatória.")
    @Min(value = 1, message = "A nota mínima é 1.")
    @Max(value = 5, message = "A nota máxima é 5.")
    Integer nuAvaliacao,

    @Schema(description = "Comentário opcional sobre o produto.",
            example = "Ótima qualidade, entregou rápido!",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "O comentário não pode ultrapassar 500 caracteres.")
    String dsComentario

) {}
