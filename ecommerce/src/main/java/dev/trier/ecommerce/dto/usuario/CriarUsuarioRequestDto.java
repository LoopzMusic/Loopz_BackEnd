package dev.trier.ecommerce.dto.usuario;

import dev.trier.ecommerce.model.enums.UsersRole;

public record CriarUsuarioRequestDto(
        UsersRole usersRole,
        String nmCliente,
        String nuCPF,
        String dsEmail,
        String dsSenha,
        Integer nuTelefone,
        String nuRG,
        String dsEndereco,
        String nuEndereco,
        String flAtivo
) {
}
