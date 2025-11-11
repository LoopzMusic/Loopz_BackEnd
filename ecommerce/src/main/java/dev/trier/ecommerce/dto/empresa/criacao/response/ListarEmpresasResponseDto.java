package dev.trier.ecommerce.dto.empresa.criacao.response;

public record ListarEmpresasResponseDto(
        String nmFantasia,
        String nmRazao,
        String nuCNPJ,
        String nuTelefone,
        String dsEstado,
        String dsEndereco,
        String nuEndereco
) {
}
