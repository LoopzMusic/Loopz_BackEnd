package dev.trier.ecommerce.controller.EmailCredentials;

import dev.trier.ecommerce.model.EmailCredenciais.EmailCredencialModel;
import dev.trier.ecommerce.repository.EmailCredencialRepository;
import dev.trier.ecommerce.utils.Encriptar.CryptoUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credenciais")
@RequiredArgsConstructor
@Tag(description = "Gerenciamento de Credenciais de Email", name = "EmailCredencialController")
public class EmailCredencialController {

    private final EmailCredencialRepository repository;


    @PostMapping("/salvar")
    public ResponseEntity<String> salvarCredenciais(@RequestParam String email,
                                                    @RequestParam String senha) {
        try {

            String emailCriptografado = CryptoUtils.encrypt(email);
            String senhaCriptografada = CryptoUtils.encrypt(senha);


            EmailCredencialModel creds = EmailCredencialModel.builder()
                    .email(emailCriptografado)
                    .password(senhaCriptografada)
                    .build();


            repository.save(creds);

            return ResponseEntity.ok("Credenciais salvas com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar credenciais: " + e.getMessage());
        }
    }


    @GetMapping("/buscar/{id}")
    public ResponseEntity<EmailCredencialModel> buscarCredenciais(@PathVariable Long id) {
        try {
            EmailCredencialModel creds = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Credenciais não encontradas"));

            creds.setEmail(CryptoUtils.decrypt(creds.getEmail()));
            creds.setPassword(CryptoUtils.decrypt(creds.getPassword()));

            return ResponseEntity.ok(creds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

