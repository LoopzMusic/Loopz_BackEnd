package dev.trier.ecommerce.dto.feedback;

import java.time.LocalDate;

public record FeedbackResponseDto(

     Integer cdFeedback,
     Integer nuAvaliacao,
     String dsComentario,
     Integer cdUsuario,
     String nmCliente,
     Integer cdProduto,
     String nmProduto,
     LocalDate dtCriacao
) {
}

