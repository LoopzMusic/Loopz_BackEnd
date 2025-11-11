package dev.trier.ecommerce.dto.produto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoTextUpdateResponseDto {
    private String nmProduto;
    private Double vlProduto;
    private String dsProduto;
}
