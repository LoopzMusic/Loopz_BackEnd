package dev.trier.ecommerce.dto.feedback;

import java.time.LocalDate;

public record FeedbackListResponseDto(
        Integer cdFeedback,
        Integer nuAvaliacao,
        String dsComentario,
        String nmCliente,
        String nmProduto,
        LocalDate dtCriacao
) {}
