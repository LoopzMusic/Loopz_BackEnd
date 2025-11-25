package dev.trier.ecommerce.dto.acoesUsuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta para ações de favoritos")
public record FavoritosResponseDto(
        Integer cdFavoritos,
        Integer cdUsuario,
        Integer cdProduto
) {}

