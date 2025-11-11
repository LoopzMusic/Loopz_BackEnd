package dev.trier.ecommerce.security;

import lombok.Builder;

@Builder
public record JWTUserData(
        Integer cdUsuario,
        String dsEmail,
        String role
) {
}
