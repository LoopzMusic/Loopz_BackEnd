package dev.trier.ecommerce.dto.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com dados do pagamento criado")
public record PagamentoResponseDto(
        @Schema(description = "ID da preferência de pagamento criada no Mercado Pago")
        String preferenceId,

        @Schema(description = "URL de inicialização do pagamento")
        String initPoint,

        @Schema(description = "URL do Sandbox (para testes)")
        String sandboxInitPoint,

        @Schema(description = "Status da criação")
        String status
) {
}