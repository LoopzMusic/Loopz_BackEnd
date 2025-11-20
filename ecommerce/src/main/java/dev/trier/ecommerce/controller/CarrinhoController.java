package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriadoResponseDto;
import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriarDto;
import dev.trier.ecommerce.dto.carrinho.modificacao.CarrinhoStatusUpdateDto;
import dev.trier.ecommerce.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
@Tag(name = "Carrinhos", description = "Capacidade de criação e modificação de status do carrinho")
@CrossOrigin("*")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping(path = "/criar")
    @Operation(summary = "Criar Carrinho", description = "Criação de carrinho destinado a um usuário")
    public ResponseEntity<CarrinhoCriadoResponseDto> criarCarrinho(@RequestBody @Valid CarrinhoCriarDto carrinhoCriarDto) {
        CarrinhoCriadoResponseDto response = carrinhoService.criarCarrinho(carrinhoCriarDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    @CrossOrigin
    @PatchMapping("/{cdCarrinho}/status")
    @Operation(summary = "Obter status do carrinho", description = "Atualiza apenas o status do carrinho")
    public ResponseEntity<CarrinhoCriadoResponseDto> alterarStatusCarrinho(
            @PathVariable Integer cdCarrinho,
            @RequestBody @Valid CarrinhoStatusUpdateDto updateDto) {

        CarrinhoCriadoResponseDto response = carrinhoService.alterarStatusCarrinho(cdCarrinho, updateDto);
        return ResponseEntity.ok(response);
    }
}
