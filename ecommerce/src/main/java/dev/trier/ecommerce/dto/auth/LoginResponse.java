package dev.trier.ecommerce.dto.auth;

import dev.trier.ecommerce.model.enums.UsersRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de login com token JWT")
public record LoginResponse(
        String token,
        Integer cdUsuario,
        String nmCliente,
        String dsEmail,
        UsersRole userRole
) {}
