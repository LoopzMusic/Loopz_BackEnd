package dev.trier.ecommerce.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackListResponseDto {

    private Integer cdFeedback;
    private Integer nuAvaliacao;
    private String dsComentario;
    private String nmCliente;
    private String nmProduto;
    private LocalDate dtCriacao;
}
