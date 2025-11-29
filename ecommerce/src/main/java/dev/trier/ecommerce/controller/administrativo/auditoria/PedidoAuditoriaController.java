package dev.trier.ecommerce.controller.administrativo.auditoria;


import dev.trier.ecommerce.dto.auditoria.PedidoAuditoriaDto;
import dev.trier.ecommerce.dto.pedido.PedidoResumoResponseDto;
import dev.trier.ecommerce.dto.pedido.criacao.PedidoResumoTodosResponseDto;
import dev.trier.ecommerce.service.PedidoService;
import dev.trier.ecommerce.service.auditoria.PedidoAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/auditoria/pedidos")
@CrossOrigin("*")
@Tag(name = "Auditoria de Pedidos", description = "Endpoints para auditoria de pedidos")
public class PedidoAuditoriaController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping()
    @Operation(summary = "Listar todos os pedidos",
            description = "Retorna cdPedido, valor total e os itens de cada pedido de todos os usuários")
    public ResponseEntity<List<PedidoResumoTodosResponseDto>> listarTodosPedidos() {
        List<PedidoResumoTodosResponseDto> pedidos = pedidoService.listarTodosPedidos();

        return ResponseEntity.ok(pedidos);
    }
}
