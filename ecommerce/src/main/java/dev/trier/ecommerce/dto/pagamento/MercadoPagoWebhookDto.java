package dev.trier.ecommerce.dto.pagamento;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para receber notificações webhook do Mercado Pago")
public record MercadoPagoWebhookDto(
        @JsonProperty("id")
        @Schema(description = "ID da notificação")
        Long id,

        @JsonProperty("live_mode")
        @Schema(description = "Indica se é ambiente de produção")
        Boolean liveMode,

        @JsonProperty("type")
        @Schema(description = "Tipo de notificação (payment, merchant_order)")
        String type,

        @JsonProperty("date_created")
        @Schema(description = "Data de criação da notificação")
        String dateCreated,

        @JsonProperty("user_id")
        @Schema(description = "ID do usuário no Mercado Pago")
        String userId,

        @JsonProperty("api_version")
        @Schema(description = "Versão da API")
        String apiVersion,

        @JsonProperty("action")
        @Schema(description = "Ação realizada (payment.created, payment.updated)")
        String action,

        @JsonProperty("data")
        @Schema(description = "Dados do pagamento")
        WebhookData data
) {
    public record WebhookData(
            @JsonProperty("id")
            String id
    ) {}
}