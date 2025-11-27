package dev.trier.ecommerce.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackListResponseDto {

    private Integer cdFeedback;
    private Integer nuAvaliacao;
    private String dsComentario;
    private String nmCliente;
    private String nmProduto;
}
