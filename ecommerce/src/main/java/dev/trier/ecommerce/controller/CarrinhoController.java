package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriadoResponseDto;
import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriarDto;
import dev.trier.ecommerce.dto.carrinho.modificacao.CarrinhoStatusUpdateDto;
import dev.trier.ecommerce.dto.carrinho.response.CarrinhoResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.security.JWTUserData;
import dev.trier.ecommerce.service.CarrinhoService;
import dev.trier.ecommerce.service.ItemCarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinhos")
@Tag(name = "Carrinhos", description = "Gerenciamento do carrinho de compras persistente")
@CrossOrigin("*")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ItemCarrinhoService itemCarrinhoService;

    @GetMapping("/meu-carrinho")
    @Operation(summary = "Buscar meu carrinho",
            description = "Retorna o carrinho ABERTO do usuário autenticado ou cria um novo se não existir")
    public ResponseEntity<CarrinhoCriadoResponseDto> buscarOuCriarMeuCarrinho(Authentication authentication) {
        Integer cdUsuario = extrairCdUsuario(authentication);
        CarrinhoCriadoResponseDto response = carrinhoService.buscarOuCriarCarrinho(cdUsuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meu-carrinho/detalhes")
    @Operation(summary = "Detalhes do meu carrinho",
            description = "Retorna o carrinho ABERTO com todos os itens, quantidades e valor total")
    public ResponseEntity<CarrinhoResponseDto> buscarDetalhesCarrinho(Authentication authentication) {
        Integer cdUsuario = extrairCdUsuario(authentication);
        return carrinhoService.buscarCarrinhoAberto(cdUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{cdCarrinho}")
    @Operation(summary = "Buscar carrinho por ID",
            description = "Retorna detalhes completos de um carrinho específico")
    public ResponseEntity<CarrinhoResponseDto> buscarCarrinhoPorId(@PathVariable Integer cdCarrinho) {
        CarrinhoResponseDto response = carrinhoService.buscarCarrinhoPorId(cdCarrinho);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar carrinho (legado)",
            description = "Busca ou cria carrinho. Use GET /meu-carrinho para nova implementação")
    public ResponseEntity<CarrinhoCriadoResponseDto> criarCarrinho(@RequestBody @Valid CarrinhoCriarDto carrinhoCriarDto) {
        CarrinhoCriadoResponseDto response = carrinhoService.buscarOuCriarCarrinho(carrinhoCriarDto.cdUsuario());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional
    @PatchMapping("/{cdCarrinho}/status")
    @Operation(summary = "Atualizar status do carrinho",
            description = "Atualiza o status (ABERTO/FINALIZADO). Use FINALIZADO ao converter para pedido")
    public ResponseEntity<CarrinhoCriadoResponseDto> alterarStatusCarrinho(
            @PathVariable Integer cdCarrinho,
            @RequestBody @Valid CarrinhoStatusUpdateDto updateDto) {

        CarrinhoCriadoResponseDto response = carrinhoService.alterarStatusCarrinho(cdCarrinho, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cdCarrinho}/itens")
    @Operation(summary = "Limpar carrinho",
            description = "Remove todos os itens do carrinho mas mantém o carrinho ativo")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Integer cdCarrinho) {
        carrinhoService.limparCarrinho(cdCarrinho);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/meu-carrinho/limpar")
    @Operation(summary = "Limpar meu carrinho",
            description = "Remove todos os itens do carrinho do usuário autenticado")
    public ResponseEntity<Void> limparMeuCarrinho(Authentication authentication) {
        Integer cdUsuario = extrairCdUsuario(authentication);
        return carrinhoService.buscarCarrinhoAberto(cdUsuario)
                .map(carrinho -> {
                    carrinhoService.limparCarrinho(carrinho.cdCarrinho());
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remover item do carrinho",
            description = "Remove um item específico de um carrinho pelo código do item")
    @DeleteMapping("/{cdCarrinho}/itens/{cdItemCarrinho}")
    public ResponseEntity<Void> removerItemDoCarrinho(@PathVariable Integer cdCarrinho,
                                                       @PathVariable Integer cdItemCarrinho) {
        itemCarrinhoService.removerItemDoCarrinho(cdCarrinho, cdItemCarrinho);
        return ResponseEntity.noContent().build();
    }

    private Integer extrairCdUsuario(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JWTUserData)) {
            throw new RecursoNaoEncontradoException("Usuário não autenticado corretamente");
        }
        return ((JWTUserData) authentication.getPrincipal()).cdUsuario();
    }
}