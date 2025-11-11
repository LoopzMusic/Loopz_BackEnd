package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.pedido.criacao.ListarPedidosResponseDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoCriarDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoCriarResponseDto;
import dev.trier.ecommerce.dto.pedido.PedidoResumoResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.security.JWTUserData;
import dev.trier.ecommerce.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@Tag(name = "Pedido", description = "Capacidade de criação e modificação do pedido")
@CrossOrigin("*")
public class
PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping(path = "/criar")
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido")
    public ResponseEntity<PedidoCriarResponseDto> criarPedido(@RequestBody @Valid PedidoCriarDto pedidoCriarDto) {
        PedidoCriarResponseDto pedidoModel = pedidoService.criarPedido(pedidoCriarDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoModel);
    }
    @GetMapping(path = "/listar/todos")
    @Operation(summary = "Listar pedidos", description = "Lista todos os pedidos cadastrados")
    public ResponseEntity<List<ListarPedidosResponseDto>> listarPedidos(){
        var lista = pedidoService.listarPedidos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lista);
    }

    @GetMapping(path = "/meus")
    @Operation(summary = "Listar meus pedidos", description = "Retorna cdPedido, valor total e os itens de cada pedido do usuário autenticado")
    public ResponseEntity<List<PedidoResumoResponseDto>> listarMeusPedidos(Authentication authentication) {
        // Extrai o cdUsuario do JWT
        Integer cdUsuario = null;

        if (authentication.getPrincipal() instanceof JWTUserData) {
            cdUsuario = ((JWTUserData) authentication.getPrincipal()).cdUsuario();
        }

        if (cdUsuario == null) {
            throw new RecursoNaoEncontradoException("Usuário não autenticado corretamente");
        }

        List<PedidoResumoResponseDto> pedidos = pedidoService.listarPedidosDoUsuarioPorId(cdUsuario);
        return ResponseEntity.ok(pedidos);
    }
}
