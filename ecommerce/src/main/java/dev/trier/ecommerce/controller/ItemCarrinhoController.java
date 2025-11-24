package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.ItemCarrinho.criacao.ItemCarrinhoCriarDto;
import dev.trier.ecommerce.dto.ItemCarrinho.modificacao.ItemCarrinhoUpdateDto;
import dev.trier.ecommerce.dto.ItemCarrinho.response.ItemCarrinhoCriadoResponseDto;
import dev.trier.ecommerce.service.ItemCarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itemcarrinhos")
@Tag(name = "Item Carrinhos", description = "Capacidade de criação e modificação do item Carrinho")
@AllArgsConstructor
public class ItemCarrinhoController {
    private ItemCarrinhoService itemCarrinhoService;

    @Operation(summary = "Adicionar item ao carrinho", description = "Adiciona um novo item vinculado a um carrinho")
    @PostMapping("/carrinhos/{cdCarrinho}/itens")
    public ResponseEntity<ItemCarrinhoCriadoResponseDto> adicionarItemAoCarrinho(
            @PathVariable Integer cdCarrinho,
            @RequestBody @Valid ItemCarrinhoCriarDto itemCriarDto) {

        ItemCarrinhoCriadoResponseDto response = itemCarrinhoService.adicionarItemAoCarrinho(cdCarrinho, itemCriarDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Atualizar quantidade", description = "Atualiza quantidade de um item específico pelo código")
    @PutMapping("/itens-carrinho/{cdItemCarrinho}")
    public ResponseEntity<ItemCarrinhoCriadoResponseDto> atualizarItemCarrinho(
            @PathVariable Integer cdItemCarrinho,
            @RequestBody @Valid ItemCarrinhoUpdateDto updateDto) {

        ItemCarrinhoCriadoResponseDto response = itemCarrinhoService.atualizarItemCarrinho(cdItemCarrinho, updateDto);
        return ResponseEntity.ok(response);
    }

}
