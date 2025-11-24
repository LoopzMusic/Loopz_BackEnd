package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.feedback.FeedbackRequestDto;
import dev.trier.ecommerce.dto.feedback.FeedbackResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.acoesUsuario.FeedbackModel;
import dev.trier.ecommerce.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Endpoints para gerenciar avaliações de produtos")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Criar feedback", description = "Cria uma nova avaliação de produto")
    public ResponseEntity<FeedbackResponseDto> criarFeedback(@Valid @RequestBody FeedbackRequestDto FeedbackDto) {
        try {
            FeedbackResponseDto novoFeedback = feedbackService.criarFeedback(FeedbackDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoFeedback);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

   @DeleteMapping("/delete/{cdFeednack}")
    public ResponseEntity<Void> deletarFeedback(@PathVariable Integer cdFeedback) {
        try{
            feedbackService.removerFeedback(cdFeedback);
            return ResponseEntity.noContent().build();
        } catch (dev.trier.ecommerce.exceptions.EntityInUseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
   }

}

