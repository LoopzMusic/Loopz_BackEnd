package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeRequest;
import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AbacatePayService {

    private final WebClient webClient;

    @Value("${abacatepay.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.abacatepay.com/v1";
    private static final String BILLING_CREATE_ENDPOINT = "/billing/create";

    public AbacatePayService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<AbacatePayChargeResponse> createCharge(AbacatePayChargeRequest request) {
        log.info("📤 Enviando requisição para AbacatePay:");
        log.info("   - ExternalId: {}", request.getExternalId());
        log.info("   - Frequency: {}", request.getFrequency());
        log.info("   - Methods: {}", request.getMethods());
        log.info("   - Customer: {}", request.getCustomer().getEmail());
        log.info("   - Products: {}", request.getProducts().size());

        // Log do produto
        if (!request.getProducts().isEmpty()) {
            var product = request.getProducts().get(0);
            log.info("   - Produto: name={}, quantity={}, price={}",
                    product.getName(), product.getQuantity(), product.getPrice());
        }

        return webClient.post()
                .uri(BILLING_CREATE_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(AbacatePayChargeResponse.class)
                .doOnSuccess(response -> {
                    if (response != null && response.getData() != null) {
                        log.info("✅ Resposta AbacatePay recebida:");
                        log.info("   - ID: {}", response.getData().getId());
                        log.info("   - URL: {}", response.getData().getUrl());
                        log.info("   - Status: {}", response.getData().getStatus());
                    } else {
                        log.warn("⚠️ Resposta AbacatePay vazia ou sem dados");
                    }
                })
                .doOnError(WebClientResponseException.class, error -> {
                    log.error("❌ Erro HTTP {} do AbacatePay:", error.getStatusCode());
                    log.error("   - Body da resposta: {}", error.getResponseBodyAsString());
                    log.error("   - Headers: {}", error.getHeaders());
                })
                .doOnError(error -> {
                    if (!(error instanceof WebClientResponseException)) {
                        log.error("❌ Erro genérico ao chamar AbacatePay: {}", error.getMessage(), error);
                    }
                });
    }
}