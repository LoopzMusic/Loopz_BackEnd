package dev.trier.ecommerce.dto.produto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoTextUpdateDto {

    @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres.")
    private String nmProduto;

    @NotNull(message = "O valor do produto não pode ser nulo.")
    private Double vlProduto;

    @Size(max = 500, message = "A descrição do produto não pode exceder 500 caracteres.")
    private String dsProduto;
}
