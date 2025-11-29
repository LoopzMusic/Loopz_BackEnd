package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeRequest;
import dev.trier.ecommerce.dto.AbacatePay.AbacatePayChargeResponse;
import dev.trier.ecommerce.service.AbacatePayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/abacatepay")
public class AbacatePayController {

    private final AbacatePayService abacatePayService;

    public AbacatePayController(AbacatePayService abacatePayService) {
        this.abacatePayService = abacatePayService;
    }

    @PostMapping("/charge")
    public Mono<ResponseEntity<AbacatePayChargeResponse>> createCharge(@RequestBody AbacatePayChargeRequest request) {
        return abacatePayService.createCharge(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}