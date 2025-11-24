package dev.trier.ecommerce.dto.auditoria;

import java.time.LocalDateTime;
import java.util.Map;

public record PedidoAuditoriaDto(
        Integer cdRevisao,
        LocalDateTime dataAlteracao,
        String cdUsuario,
        String tipoAlteracao,
        Map<String, Object> alteracoes
) {
}
