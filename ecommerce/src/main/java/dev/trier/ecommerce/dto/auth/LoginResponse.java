package dev.trier.ecommerce.dto.auth;

import dev.trier.ecommerce.model.enums.UsersRole;

public record LoginResponse(
        String token,
        Integer cdUsuario,
        String dsEmail,
        UsersRole userRole,
        String nmCliente,
        String googleId,
        Boolean profileComplete
) {}
