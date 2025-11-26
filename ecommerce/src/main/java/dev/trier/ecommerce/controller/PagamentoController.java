package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.pagamento.MercadoPagoWebhookDto;
import dev.trier.ecommerce.dto.pagamento.PagamentoRequestDto;
import dev.trier.ecommerce.dto.pagamento.PagamentoResponseDto;
import dev.trier.ecommerce.model.PagamentoModel;
import dev.trier.ecommerce.model.enums.StatusPagamento;
import dev.trier.ecommerce.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagamento")
@Tag(name = "Pagamento", description = "Gerenciamento de pagamentos via Mercado Pago")
@AllArgsConstructor
@CrossOrigin("*")
@Slf4j
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping("/criar")
    @Operation(summary = "Criar pagamento", description = "Cria uma preferência de pagamento no Mercado Pago para um pedido")
    public ResponseEntity<PagamentoResponseDto> criarPagamento(@RequestBody @Valid PagamentoRequestDto request) {
        PagamentoResponseDto response = pagamentoService.criarPagamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/webhook")
    @Operation(summary = "Webhook Mercado Pago", description = "Endpoint para receber notificações do Mercado Pago")
    public ResponseEntity<Void> webhook(@RequestBody MercadoPagoWebhookDto webhook) {
        log.info("Webhook recebido: Type={}, Action={}", webhook.type(), webhook.action());

        if ("payment".equals(webhook.type())) {
            String paymentId = webhook.data().id();
            pagamentoService.processarWebhook(paymentId);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pedido/{cdPedido}")
    @Operation(summary = "Buscar pagamento por pedido", description = "Retorna o pagamento associado a um pedido")
    public ResponseEntity<PagamentoModel> buscarPorPedido(@PathVariable Integer cdPedido) {
        PagamentoModel pagamento = pagamentoService.buscarPorPedido(cdPedido);
        return ResponseEntity.ok(pagamento);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pagamentos por status", description = "Lista todos os pagamentos com um status específico")
    public ResponseEntity<List<PagamentoModel>> listarPorStatus(@PathVariable StatusPagamento status) {
        List<PagamentoModel> pagamentos = pagamentoService.listarPorStatus(status);
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/sucesso")
    @Operation(summary = "Callback de sucesso", description = "Página de redirecionamento após pagamento aprovado")
    public ResponseEntity<Map<String, String>> paginaSucesso(
            @RequestParam(required = false) String payment_id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String external_reference) {

        log.info("Redirecionado para página de sucesso. PaymentId: {}, Status: {}", payment_id, status);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Pagamento realizado com sucesso!",
                "paymentId", payment_id != null ? payment_id : "N/A",
                "status", status != null ? status : "N/A"
        ));
    }

    @GetMapping("/falha")
    @Operation(summary = "Callback de falha", description = "Página de redirecionamento após pagamento rejeitado")
    public ResponseEntity<Map<String, String>> paginaFalha(
            @RequestParam(required = false) String payment_id,
            @RequestParam(required = false) String status) {

        log.warn("Redirecionado para página de falha. PaymentId: {}, Status: {}", payment_id, status);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Pagamento rejeitado ou falhou.",
                "paymentId", payment_id != null ? payment_id : "N/A",
                "status", status != null ? status : "N/A"
        ));
    }

    @GetMapping("/pendente")
    @Operation(summary = "Callback pendente", description = "Página de redirecionamento para pagamento pendente")
    public ResponseEntity<Map<String, String>> paginaPendente(
            @RequestParam(required = false) String payment_id,
            @RequestParam(required = false) String status) {

        log.info("Redirecionado para página pendente. PaymentId: {}, Status: {}", payment_id, status);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Pagamento pendente de confirmação.",
                "paymentId", payment_id != null ? payment_id : "N/A",
                "status", status != null ? status : "N/A"
        ));
    }
}