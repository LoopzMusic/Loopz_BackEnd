package dev.trier.ecommerce.controller.administrativo;

import dev.trier.ecommerce.dto.administrativo.TotalResponseDto;
import dev.trier.ecommerce.repository.FeedbackRepository;
import dev.trier.ecommerce.repository.PedidoRepository;
import dev.trier.ecommerce.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/resumo")
@CrossOrigin("*")
@Tag(name = "Resumo Administrativo", description = "Contadores gerais do sistema")
public class ResumoAdministrativoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/produtos/total")
    @Operation(summary = "Total de produtos", description = "Retorna a quantidade total de produtos cadastrados no sistema")
    public ResponseEntity<TotalResponseDto> totalProdutos() {
        long total = produtoRepository.count();
        return ResponseEntity.ok(new TotalResponseDto(total));
    }

    @GetMapping("/pedidos/total")
    @Operation(summary = "Total de pedidos", description = "Retorna a quantidade total de pedidos cadastrados no sistema")
    public ResponseEntity<TotalResponseDto> totalPedidos() {
        long total = pedidoRepository.count();
        return ResponseEntity.ok(new TotalResponseDto(total));
    }

    @GetMapping("/feedbacks/total")
    @Operation(summary = "Total de feedbacks", description = "Retorna a quantidade total de feedbacks cadastrados no sistema")
    public ResponseEntity<TotalResponseDto> totalFeedbacks() {
        long total = feedbackRepository.count();
        return ResponseEntity.ok(new TotalResponseDto(total));
    }
}

