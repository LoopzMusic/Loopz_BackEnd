package dev.trier.ecommerce.dto.usuario.response;

public record ListarCdUsuariodResponse(
        String nmCliente,
        String dsEmail,
        String nuTelefone,
        String dsCidade,
        String flAtivo
) {
}
