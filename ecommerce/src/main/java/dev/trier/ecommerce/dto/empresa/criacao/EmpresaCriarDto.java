package dev.trier.ecommerce.dto.empresa.criacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação de uma nova empresa")
public record EmpresaCriarDto(
    @NotBlank(message = "Preenchimento do nome fantasia obrigatório")
    @Schema(description = "Nome fantasia da empresa", example = "Neo Tech Ltda", requiredMode = Schema.RequiredMode.REQUIRED)
    String nmFantasia,

    @NotBlank(message = "Preencimento do nome razão obrigatório")
    @Schema(description = "Razão social da empresa", example = "Neo Tech Tecnologia Ltda", requiredMode = Schema.RequiredMode.REQUIRED)
    String nmRazao,

    @NotBlank(message = "Preenchimento do CPNJ obrigatório")
    @Size(min = 14, max = 18)
    @Pattern(regexp = "\\d{14}", message = "CNPJ deve conter exatamente 14 dígitos numéricos")
    @Schema(description = "CNPJ da empresa nesse formato: 00000000000000)", example = "12345678000199", requiredMode = Schema.RequiredMode.REQUIRED)
    String nuCNPJ,

    @NotBlank(message = "Preenchimento do telefone obrigatório")
    @Size(min = 10, max = 15)
    @Pattern(regexp = "\\d{10,15}")
    @Schema(description = "Telefone da empresa (apenas números, com DDD)", example = "11987654321", requiredMode = Schema.RequiredMode.REQUIRED)
    String nuTelefone,

    @NotBlank(message = "Preenchimento da cidade obrigatório")
    @Schema(description = "Nome da cidade a empresa sede ", example = "Braço do Norte", requiredMode = Schema.RequiredMode.REQUIRED)
    String dsCidade,

    @NotBlank(message = "Preenchimento do estado obrigatório")
    @Schema(description = "Estado brasileiro que a empresa sede", example = "Santa Catarina", requiredMode = Schema.RequiredMode.REQUIRED)
    String dsEstado,

    @NotBlank(message = "Preenchimento do endereço obrigatório")
    @Schema(description = "Descrição do endereço da empresa", example = "Rua estrada Geral, Bairro Travessão", requiredMode = Schema.RequiredMode.REQUIRED)
    String dsEndereco,

    @NotBlank(message = "Preenchimento do número do endereço obrigatório")
    @Pattern(regexp = "^(\\d+[A-Za-z]?)$|^(?i)s\\s*/?\\s*n$")  //repositório da faculdade
    @Schema(description = "Número do endereço", example = "123 ou SN S/N ", requiredMode = Schema.RequiredMode.REQUIRED)
    String nuEndereco
) {}
