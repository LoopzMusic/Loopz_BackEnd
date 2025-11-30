package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.model.ApiSecretKeyModel;
import dev.trier.ecommerce.repository.SecretKeyGoogleFrete.ApiSecretKeyRepository;
import dev.trier.ecommerce.utils.Encriptar.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frete")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FreteController {


    private final ApiSecretKeyRepository repository;


    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/calcular")
    public ResponseEntity<Double> calcularFrete(@RequestParam String destino) {
        try {
            ApiSecretKeyModel chaveModel = repository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Secret key não encontrada"));


            String API_KEY = CryptoUtils.decrypt(chaveModel.getSecretKey());

            String origem = "Tubarão, SC";

            String url = "https://maps.googleapis.com/maps/api/distancematrix/json" +
                    "?origins=" + origem +
                    "&destinations=" + destino +
                    "&key=" + API_KEY +
                    "&units=metric";

            Map resposta = restTemplate.getForObject(url, Map.class);

            Map row = ((List<Map>)resposta.get("rows")).get(0);
            Map element = ((List<Map>)row.get("elements")).get(0);
            Map distance = (Map)element.get("distance");
            double distanciaKm = ((Number)distance.get("value")).doubleValue() / 1000.0;

            double frete = 5 + distanciaKm * 0.5;

            return ResponseEntity.ok(Math.round(frete * 100.0) / 100.0);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}

