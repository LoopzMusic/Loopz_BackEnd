package dev.trier.ecommerce.dto.itempedido.criacao;

public record ItemPedidoCriadoRespostaDto(

        Integer cdItemPedido,

        Integer cdPedido,

        Integer cdProduto,

        Double vlItemPedido,

        Integer qtItem
) {
}
