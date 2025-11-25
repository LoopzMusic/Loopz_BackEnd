package dev.trier.ecommerce.controller.acoesUsuario;

import dev.trier.ecommerce.dto.acoesUsuario.FavoritosCriarDto;
import dev.trier.ecommerce.dto.acoesUsuario.FavoritosResponseDto;
import dev.trier.ecommerce.service.acoesUsuario.FavoritosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@Tag(name = "Favoritos", description = "Ações de favoritar e desfavoritar")
@AllArgsConstructor
@CrossOrigin("*")
public class FavoritosController {

    private final FavoritosService favoritosService;

    @Operation(summary = "Favoritar produto", description = "Favorita um produto para um usuário")
    @PostMapping
    public ResponseEntity<FavoritosResponseDto> favoritar(@RequestBody @Valid FavoritosCriarDto dto) {
        FavoritosResponseDto response = favoritosService.favoritar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Desfavoritar produto", description = "Remove favorito de um produto para um usuário")
    @DeleteMapping("/desfavoritar/{cdUsuario}/{cdProduto}")
    public ResponseEntity<Void> desfavoritar(@PathVariable Integer cdUsuario, @PathVariable Integer cdProduto) {
        favoritosService.desfavoritar(cdUsuario, cdProduto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar códigos de produtos favoritados por usuário", description = "Retorna apenas os códigos dos produtos que o usuário favoritou")
    @GetMapping("/usuario/{cdUsuario}")
    public ResponseEntity<List<Integer>> listarCdProdutosFavoritados(@PathVariable Integer cdUsuario) {
        List<Integer> cds = favoritosService.listarCdProdutosFavoritadosPorUsuario(cdUsuario);
        return ResponseEntity.ok(cds);
    }
}
