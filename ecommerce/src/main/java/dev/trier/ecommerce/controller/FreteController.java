package dev.trier.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frete")
public class FreteController {

    private final String API_KEY = "";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/calcular")
    public ResponseEntity<Double> calcularFrete(@RequestParam String destino) {
        try {

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

