package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeRequest;
import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        return webClient.post()
                .uri(BILLING_CREATE_ENDPOINT)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(AbacatePayChargeResponse.class);
    }
}