package dev.trier.ecommerce.dto.auth;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição de login")
public record LoginRequest(
        @NotBlank String dsEmail,
        @NotBlank String dsSenha
) {}
