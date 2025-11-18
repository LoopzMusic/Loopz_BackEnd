package dev.trier.ecommerce.dto.produto.response;



public record ListarProdutosResponseDto(
        String nmProduto,
        Double vlProduto,
        String dsCategoria,
        char dsAcessorio,
        String dsProduto,
        byte[] imgProduto,
        Integer cdProduto,
        Integer cdEmpresa,
        Integer qtdEstoqueProduto

) {
}
