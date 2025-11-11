package dev.trier.ecommerce.dto.empresa.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO para listagem de empresas com produtos referenciados por ID")
public record EmpresaListResponseDto(
        Integer cdEmpresa,
        String nmFantasia,
        String nmRazao,
        String nuCNPJ,
        String nuTelefone,
        String dsCidade,
        String dsEstado,
        String dsEndereco,
        String nuEndereco,
        String flAtivo,
        List<Integer> produtos
) {}

