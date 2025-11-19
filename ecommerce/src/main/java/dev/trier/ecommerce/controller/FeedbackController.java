package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.model.acoesUsuario.FeedbackModel;
import dev.trier.ecommerce.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Endpoints para gerenciar avaliações de produtos")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Criar feedback", description = "Cria uma nova avaliação de produto")

    //trocar por dto depois
    public ResponseEntity<FeedbackModel> criarFeedback(@RequestBody FeedbackModel feedback) {
        try {
            FeedbackModel novoFeedback = feedbackService.criarFeedback(feedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

