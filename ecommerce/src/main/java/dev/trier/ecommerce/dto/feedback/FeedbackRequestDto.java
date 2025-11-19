package dev.trier.ecommerce.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestDto {

    private Integer cdUsuario;
    private Integer cdProduto;
    private Integer nuAvaliacao;
    private String dsComentario;
}
