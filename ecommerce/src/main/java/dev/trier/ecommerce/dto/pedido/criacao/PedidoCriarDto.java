package dev.trier.ecommerce.dto.pedido.criacao;

import dev.trier.ecommerce.model.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de um novo pedido.")
public record PedidoCriarDto(


    @Schema(description = "Usuário do pedido", example = "Davi Beckhauser", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer cdUsuario,

    @NotNull(message = "A forma de pagamento é obrigatória.")
    @Schema(description = "Forma de pagamento do pedido", example = "PIX", requiredMode = Schema.RequiredMode.REQUIRED)
    FormaPagamento formaPagamento,

    @NotNull(message = "O valor do frete é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor do frete não pode ser negativo.")
    @Schema(description = "Valor do frete aplicado ao pedido", example = "15.50", requiredMode = Schema.RequiredMode.REQUIRED)
    Double vlFrete,

    @NotNull(message = "O valor total do pedido é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor total do pedido não pode ser negativo.")
    @Schema(description = "Valo     r total do pedido incluindo frete", example = "200.00", requiredMode = Schema.RequiredMode.REQUIRED)
    Double vlTotalPedido

) {}

