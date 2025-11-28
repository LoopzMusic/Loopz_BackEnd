package dev.trier.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.enums.UsersRole;
import dev.trier.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GoogleOAuth2Service {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    /**
     * Valida o token do Google e cria ou atualiza o usuário
     */
    public UsuarioModel validateAndCreateOrUpdateUser(String googleToken) throws Exception {
        Map<String, Object> claims = verifyGoogleToken(googleToken);

        String email = (String) claims.get("email");
        String name = (String) claims.get("name");
        String googleId = (String) claims.get("sub");

        // Verifica se o usuário já existe
        Optional<UsuarioModel> existingUser = usuarioRepository.findByDsEmail(email);

        if (existingUser.isPresent()) {
            UsuarioModel usuario = existingUser.get();
            if (usuario.getGoogleId() == null) {
                usuario.setGoogleId(googleId);
                usuarioRepository.save(usuario);
            }
            return usuario;
        }

        // Cria um novo usuário
        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setDsEmail(email);
        novoUsuario.setNmCliente(name);
        novoUsuario.setGoogleId(googleId);
        novoUsuario.setUserRole(UsersRole.USER);
        novoUsuario.setFlAtivo("S");
        novoUsuario.setDsSenha(generateRandomPassword());

        return usuarioRepository.save(novoUsuario);
    }

    /**
     * Verifica o token JWT do Google
     */
    private Map<String, Object> verifyGoogleToken(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Token inválido");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        return objectMapper.readValue(payload, Map.class);
    }

    /**
     * Gera uma senha aleatória para usuários OAuth
     */
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 16);
    }

    /**
     * Verifica se o perfil do usuário está completo
     */
    public boolean isProfileComplete(UsuarioModel usuario) {
        return usuario.getNuCPF() != null && !usuario.getNuCPF().isEmpty() &&
                usuario.getNuTelefone() != null && !usuario.getNuTelefone().isEmpty() &&
                usuario.getDsEndereco() != null && !usuario.getDsEndereco().isEmpty() &&
                usuario.getDsCidade() != null && !usuario.getDsCidade().isEmpty() &&
                usuario.getDsEstado() != null && !usuario.getDsEstado().isEmpty();
    }
}