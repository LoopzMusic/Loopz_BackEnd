package dev.trier.ecommerce.dto.itempedido.criacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Criação do item pedido por DTO")
public record ItemPedidoCriarDto(

        @Schema(description = "Identificador único do pedido que vai no item pedido", example = "1")
        @NotNull(message = "Obrigatório informar o código do pedido")
        Integer cdPedido,
        @NotNull(message = "Obrigatório informar o código do produto")
        @Schema(description = "Identificador único do produto que vai no item pedido", example = "1")
        Integer cdProduto,

        @NotNull(message = "Obrigatório informar o valor do item pedido")
        @Positive
        @Schema(description = "Aqui vai ser o valor dos itens para este pedido")
        Double vlItemPedido,

        @NotNull(message = "Obrigatório informar a quantitade do item pedido")
        @Positive
        @Schema(description = "Aqui vai ser a quantidade de itens para este pedido")
        Integer qtItem
) {

}