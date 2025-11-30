package dev.trier.ecommerce.service.emailCredencialService;


import dev.trier.ecommerce.model.EmailCredenciais.EmailCredencialModel;
import dev.trier.ecommerce.repository.EmailCredencialRepository;
import dev.trier.ecommerce.utils.Encriptar.CryptoUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailCredencialService {
    private final EmailCredencialRepository repository;

    public EmailCredencialModel buscarCredenciais(Long id) throws Exception {
        EmailCredencialModel creds = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credenciais não encontradas"));

        creds.setEmail(CryptoUtils.decrypt(creds.getEmail()));
        creds.setPassword(CryptoUtils.decrypt(creds.getPassword()));

        return creds;
    }
}
