package dev.trier.ecommerce.dto.feedback;

public record FeedbackResponseDto(

     Integer cdFeedback,
     Integer nuAvaliacao,
     String dsComentario,
     Integer cdUsuario,
     String nmUsuario,
     Integer cdProduto,
     String nmProduto
) {
}

