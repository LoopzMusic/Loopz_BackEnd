package dev.trier.ecommerce.controller.SecretKeyGoogleFrete;

import dev.trier.ecommerce.model.ApiSecretKeyModel;
import dev.trier.ecommerce.repository.SecretKeyGoogleFrete.ApiSecretKeyRepository;
import dev.trier.ecommerce.utils.Encriptar.CryptoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secretkey")
public class ApiSecretKeyController {

    private final ApiSecretKeyRepository repository;

    public ApiSecretKeyController(ApiSecretKeyRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/inserir")
    public ResponseEntity<String> inserirSecretKey(@RequestParam String key) {
        try {
            String encrypted = CryptoUtils.encrypt(key);

            ApiSecretKeyModel apiKey = ApiSecretKeyModel.builder()
                    .SecretKey(encrypted)
                    .build();

            repository.save(apiKey);

            return ResponseEntity.ok("Deu boa");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Deu ruim");
        }
    }
}
