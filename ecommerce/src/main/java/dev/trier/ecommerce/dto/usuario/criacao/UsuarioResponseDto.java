package dev.trier.ecommerce.dto.usuario.criacao;

public record UsuarioResponseDto(
        String nmCliente,
        String nuCPF,
        String nuTelefone,
        String dsCidade,
        String dsEstado,
        String dsEndereco,
        String nuEndereco,
        String dsEmail,
        String flAtivo
) {}
