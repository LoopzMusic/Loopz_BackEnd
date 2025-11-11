package dev.trier.ecommerce.dto.usuario.modificacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO para atualização de usuário (PUT)")
public record UsuarioUpdateDto(
        @Schema(description = "Nome do cliente", example = "Davi Beckhauser")
        String nmCliente,

        @Schema(description = "Email para login do usuário", example = "davi@example.com")
        String dsEmail,

//        @Schema(description = "Senha do usuário", example = "Secret@123")
//        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Senha precisa ter letras maiúsculas, minúsculas, número e caractere especial, mínimo 8 caracteres")
//        String dsSenha,

        @Schema(description = "Número de CPF", example = "112.123.123-22")
        @Pattern(regexp = "^(\\d{3}\\.?){3}-?\\d{2}$")
        String nuCPF,

        @Schema(description = "Número de telefone da pessoa", example = "48920023805")
        String nuTelefone,

        @Schema(description = "Número de RG da pessoa", example = "7510437")
        String nuRG,

        @Schema(description = "Nome da cidade", example = "Braço do Norte")
        String dsCidade,

        @Schema(description = "Nome do estado", example = "Santa Catarina")
        String dsEstado,

        @Schema(description = "Endereço de localização", example = "Travessão, estrada geral")
        String dsEndereco,

        @Schema(description = "Número do endereço", example = "102")
        String nuEndereco
) {}
