package dev.trier.ecommerce.dto.produto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestDto {
    private String nmProduto;
    private Double vlProduto;
    private String dsProduto;
    private String dsCategoria;
    private Integer cdEmpresa;




}
