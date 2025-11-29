package dev.trier.ecommerce.controller.views;

import dev.trier.ecommerce.service.views.RecomendacaoInteligenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recomendacao")
@RequiredArgsConstructor
@Tag(name = "Recomendação Inteligente", description = "Endpoints para recomendações de produtos baseado em vendas e compras relacionadas")
@CrossOrigin("*")
public class RecomendacaoInteligenteController {

    private final RecomendacaoInteligenteService recomendacaoInteligenteService;

    @Operation(summary = "Recomendar produtos", description = "Recomenda produtos baseados em um produto específico e no vínculo dele com outros produtos" +
            "caso ele tenha aparecido mais de uma vez com o próprio em compras anteriores.")
    @GetMapping("/{id}/recomendados")
    public ResponseEntity<?> recomendar(@PathVariable Integer id) {
        return ResponseEntity.ok(recomendacaoInteligenteService.recomendar(id));
    }

    @Operation(summary = "Listar mais vendidos", description = "Lista os produtos mais vendidos na plataforma")
    @GetMapping("/mais-vendidos")
    public ResponseEntity<?> maisVendidos() {
        return ResponseEntity.ok(recomendacaoInteligenteService.listarMaisVendidos());
    }
}
