package dev.trier.ecommerce.dto.feedback;

public record FeedbackResponseDto(

     Integer cdFeedback,
     Integer nuAvaliacao,
     String dsComentario,
     Integer cdUsuario,
     String nmCliente,
     Integer cdProduto,
     String nmProduto
) {
}

