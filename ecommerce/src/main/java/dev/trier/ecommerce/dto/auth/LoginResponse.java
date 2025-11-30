package dev.trier.ecommerce.dto.auth;

import dev.trier.ecommerce.model.enums.UsersRole;

public record LoginResponse(
        String token,
        Integer cdUsuario,
        String nmCliente,
        String dsEmail,
        UsersRole userRole,
        String googleId,
        Boolean profileComplete
) {}
