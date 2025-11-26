package dev.trier.ecommerce.dto.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO para criar preferência de pagamento no Mercado Pago")
public record PagamentoRequestDto(
        @NotNull(message = "O código do pedido é obrigatório")
        @Schema(description = "Código do pedido", example = "1")
        Integer cdPedido,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        @Schema(description = "Valor total do pagamento", example = "250.00")
        BigDecimal valor,

        @Schema(description = "Descrição do pedido", example = "Pedido #1 - Instrumentos Musicais")
        String descricao,

        @Schema(description = "Email do pagador", example = "cliente@email.com")
        String emailPagador,

        @Schema(description = "Nome do pagador", example = "João Silva")
        String nomePagador
) {
}
