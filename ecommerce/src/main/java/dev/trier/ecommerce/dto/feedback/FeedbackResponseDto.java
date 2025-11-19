package dev.trier.ecommerce.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDto {

    private Integer cdFeedback;
    private Integer nuAvaliacao;
    private String dsComentario;
    private Integer cdUsuario;
    private String nmUsuario;
    private Integer cdProduto;
    private String nmProduto;
    private String dsImagem;
    }

