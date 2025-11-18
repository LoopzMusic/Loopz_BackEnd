package dev.trier.ecommerce.dto.produto.criacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import jakarta.websocket.OnMessage;
import org.springframework.web.multipart.MultipartFile;
@Schema(description = "DTO para criação de um novo produto.")
public record ProdutoCriarDto(

        @Schema(description = "Nome do produto.", example = "Violino 4/4", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Obrigatório nome no produto")
        @Size(min = 1, message = "O produto precisa ter mais de um caracter em seu nome")
        String nmProduto,

        @Schema(description = "Valor do produto em reais (precisa ser positivo).", example = "417.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Obrigatório colocar valor no produto")
        @Positive(message = "Valor do produto precisa ser positivo")
        Double vlProduto,

        @Schema(description = "Categoria do produto. Escritas válidas: CORDA,TECLADO,SOPRO e PERCUSSAO", example = "CORDA", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Obrigatório preenchimento da categoria")
        @Pattern(regexp = "^(CORDA|TECLADO|SOPRO|PERCUSSAO)$", message = "Equipamento inválido. As opções válidas são: CORDA,TECLADO,SOPRO e PERCUSSAO")
        String dsCategoria,

        @Schema(description = "Indicação se o produto seria um acessório", example = "N", requiredMode = Schema.RequiredMode.REQUIRED)
        char dsAcessorio,


        @NotBlank(message = "Obrigatório a descrição do produto")
        @Schema(description = "Descrição do produto, deixar descrito o produto neste campo para apresentar", example = "Violino fosco estudante avançado Vogga", requiredMode = Schema.RequiredMode.REQUIRED)
        String dsProduto,


        @Schema(description = "Imagem do produto que vai ser utilizado.", requiredMode = Schema.RequiredMode.REQUIRED)
        MultipartFile imgProduto,


        @Schema(description = "Código identificador da empresa relacionada ao produto.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer cdEmpresa
) {}
