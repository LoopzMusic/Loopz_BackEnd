package dev.trier.ecommerce.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import dev.trier.ecommerce.dto.pagamento.PagamentoRequestDto;
import dev.trier.ecommerce.dto.pagamento.PagamentoResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.PagamentoModel;
import dev.trier.ecommerce.model.PedidoModel;
import dev.trier.ecommerce.model.enums.StatusPagamento;
import dev.trier.ecommerce.model.enums.StatusPedido;
import dev.trier.ecommerce.repository.PagamentoRepository;
import dev.trier.ecommerce.repository.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final String accessToken;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            PedidoRepository pedidoRepository,
            @Value("${mercadopago.access-token}") String accessToken) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.accessToken = accessToken;
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    @Transactional
    public PagamentoResponseDto criarPagamento(PagamentoRequestDto request) {
        PedidoModel pedido = pedidoRepository.findById(request.cdPedido())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + request.cdPedido()));

        if (pagamentoRepository.findByPedido_CdPedido(request.cdPedido()).isPresent()) {
            throw new IllegalStateException("Já existe um pagamento para este pedido");
        }

        try {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(String.valueOf(pedido.getCdPedido()))
                    .title(request.descricao() != null ? request.descricao() : "Pedido #" + pedido.getCdPedido())
                    .description("Compra de instrumentos musicais")
                    .quantity(1)
                    .currencyId("BRL")
                    .unitPrice(request.valor())
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferencePayerRequest payerRequest = PreferencePayerRequest.builder()
                    .name(request.nomePagador() != null ? request.nomePagador() : pedido.getUsuario().getNmCliente())
                    .email(request.emailPagador() != null ? request.emailPagador() : pedido.getUsuario().getDsEmail())
                    .build();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:8085/api/pagamento/sucesso")
                    .failure("http://localhost:8085/api/pagamento/falha")
                    .pending("http://localhost:8085/api/pagamento/pendente")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .payer(payerRequest)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(String.valueOf(pedido.getCdPedido()))
                    .notificationUrl("https://seu-dominio.com/api/pagamento/webhook")
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            PagamentoModel pagamento = new PagamentoModel();
            pagamento.setPedido(pedido);
            pagamento.setPreferenceId(preference.getId());
            pagamento.setValor(request.valor());
            pagamento.setStatus(StatusPagamento.PENDENTE);
            pagamento.setInitPoint(preference.getInitPoint());
            pagamento.setSandboxInitPoint(preference.getSandboxInitPoint());

            pagamentoRepository.save(pagamento);

            log.info("Pagamento criado com sucesso. PreferenceId: {}", preference.getId());

            return new PagamentoResponseDto(
                    preference.getId(),
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint(),
                    "CRIADO"
            );

        } catch (MPApiException apiException) {
            log.error("Erro na API do Mercado Pago: {}", apiException.getApiResponse().getContent());
            throw new RuntimeException("Erro ao criar pagamento no Mercado Pago: " + apiException.getMessage());
        } catch (MPException exception) {
            log.error("Erro no SDK do Mercado Pago: {}", exception.getMessage());
            throw new RuntimeException("Erro ao processar pagamento: " + exception.getMessage());
        }
    }

    @Transactional
    public void processarWebhook(String paymentId) {
        try {
            com.mercadopago.client.payment.PaymentClient paymentClient = new com.mercadopago.client.payment.PaymentClient();
            com.mercadopago.resources.payment.Payment payment = paymentClient.get(Long.parseLong(paymentId));

            String externalReference = payment.getExternalReference();
            if (externalReference == null) {
                log.warn("Payment sem external reference: {}", paymentId);
                return;
            }

            Integer cdPedido = Integer.valueOf(externalReference);
            PagamentoModel pagamento = pagamentoRepository.findByPedido_CdPedido(cdPedido)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado para o pedido: " + cdPedido));

            pagamento.setPaymentId(paymentId);
            pagamento.setMetodoPagamento(payment.getPaymentMethodId());
            pagamento.setStatusDetalhe(payment.getStatusDetail());

            StatusPagamento novoStatus = mapearStatusPagamento(payment.getStatus());
            pagamento.setStatus(novoStatus);

            if (novoStatus == StatusPagamento.APROVADO) {
                pagamento.setPagamentoAprovadoEm(LocalDateTime.now());
                PedidoModel pedido = pagamento.getPedido();
                pedido.setStatusPedido(StatusPedido.ANDAMENTO);
                pedidoRepository.save(pedido);
                log.info("Pagamento aprovado e pedido atualizado para ANDAMENTO. Pedido: {}", cdPedido);
            }

            pagamentoRepository.save(pagamento);
            log.info("Webhook processado com sucesso. PaymentId: {}, Status: {}", paymentId, novoStatus);

        } catch (MPApiException | MPException e) {
            log.error("Erro ao processar webhook do Mercado Pago: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar notificação de pagamento", e);
        }
    }

    private StatusPagamento mapearStatusPagamento(String mpStatus) {
        return switch (mpStatus) {
            case "approved" -> StatusPagamento.APROVADO;
            case "rejected" -> StatusPagamento.REJEITADO;
            case "cancelled" -> StatusPagamento.CANCELADO;
            case "in_process", "in_mediation" -> StatusPagamento.EM_ANALISE;
            case "refunded", "charged_back" -> StatusPagamento.ESTORNADO;
            default -> StatusPagamento.PENDENTE;
        };
    }

    @Transactional(readOnly = true)
    public PagamentoModel buscarPorPedido(Integer cdPedido) {
        return pagamentoRepository.findByPedido_CdPedido(cdPedido)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado para o pedido: " + cdPedido));
    }

    @Transactional(readOnly = true)
    public List<PagamentoModel> listarPorStatus(StatusPagamento status) {
        return pagamentoRepository.findByStatus(status);
    }
}