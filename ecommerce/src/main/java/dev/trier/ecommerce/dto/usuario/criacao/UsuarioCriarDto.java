package dev.trier.ecommerce.dto.usuario.criacao;

import dev.trier.ecommerce.model.enums.UsersRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO para criação de usuário")
public record UsuarioCriarDto(
    @NotBlank(message = "O nome do cliente é obrigatório.")
    @Schema(description = "Nome do cliente", example = "Davi Beckhauser", requiredMode = Schema.RequiredMode.REQUIRED)
    String nmCliente,

    @NotBlank(message = "O Email é obrigatório.")
    @Schema(description = "Email para login do usuário", example = "davi@hotmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email
    String dsEmail,

    @NotBlank(message = "A senha é obrigatório.")
    @Schema(description = "Senha para login do usuário", example = "Spring@2025", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    String dsSenha,

    @NotBlank(message = "O CPF é obrigatório.")
    @Schema(description = "Número de CPF", example = "25368896026", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos numéricos")
    String nuCPF,

    @NotNull(message = "O telefone é obrigatório.")
    @Schema(description = "Número de telefone da pessoa", example = "48920003890", requiredMode = Schema.RequiredMode.REQUIRED)
    String nuTelefone,

    @Schema(description = "Número de RG da pessoa", example = "7510437")
    String nuRG,

    @NotBlank(message = "A cidade é obrigatório.")
    @Schema(description = "Nome da cidade", example = "Braço do Norte", requiredMode = Schema.RequiredMode.REQUIRED)
    String dsCidade,

    @NotBlank(message = "O estado é obrigatório.")
    @Schema(description = "Nome do estado", example = "Santa Catarina", requiredMode = Schema.RequiredMode.REQUIRED)
    String dsEstado,

    @NotBlank(message = "O endereço é obrigatório.")
    @Schema(description = "Endereço de localização de sua moradia", example = "Travessão, estrada geral", requiredMode = Schema.RequiredMode.REQUIRED)
    String dsEndereco,

    @NotBlank(message = "Preenchimento do número do endereço obrigatório")
    @Pattern(regexp = "^(\\d+[A-Za-z]?)$|^(?i)s\\s*/?\\s*n$")  //repositório da faculdade
    @Schema(description = "Número do endereço", example = "123 ou SN S/N ", requiredMode = Schema.RequiredMode.REQUIRED)
    String nuEndereco
) {}

