package dev.trier.ecommerce.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record GoogleTokenValidationRequest(
        @NotBlank(message = "Token é obrigatório")
        String token
) {
}
