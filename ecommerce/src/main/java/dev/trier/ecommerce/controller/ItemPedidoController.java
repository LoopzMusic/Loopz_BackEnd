package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.itempedido.criacao.ItemPedidoCriadoRespostaDto;
import dev.trier.ecommerce.dto.itempedido.criacao.ItemPedidoCriarDto;
import dev.trier.ecommerce.dto.itempedido.criacao.ListarItensPedidosResponseDto;
import dev.trier.ecommerce.model.ItemPedidoModel;
import dev.trier.ecommerce.repository.ItemPedidoRepository;
import dev.trier.ecommerce.service.ItemPedidoService;
import dev.trier.ecommerce.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itempedido")
@Tag(name = "Item Pedido", description = "Capacidade de criação e modificação do item pedido")
@CrossOrigin("*")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoService itemPedidoService;


    @PostMapping(path = "/criar")
    @Operation(summary = "Criar item do pedido", description = "Cria um novo item vinculado a um pedido")
    public ResponseEntity<ItemPedidoCriadoRespostaDto> criarItemPedido(@RequestBody @Valid ItemPedidoCriarDto itemPedidoCriarDto){
        ItemPedidoCriadoRespostaDto itemPedidoModel = itemPedidoService.criarItemPedido(itemPedidoCriarDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemPedidoModel);
    }

    @GetMapping(path = "/listar/todos")
    @Operation(summary = "Listar itens de pedido", description = "Lista todos os itens de pedido cadastrados")
    public ResponseEntity<List<ListarItensPedidosResponseDto>> listarItemPedido() {
        var lista = itemPedidoService.listaItemPedidos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lista);
    }

}
