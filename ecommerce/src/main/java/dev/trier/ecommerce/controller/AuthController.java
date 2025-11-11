package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.auth.LoginRequest;
import dev.trier.ecommerce.dto.auth.LoginResponse;
import dev.trier.ecommerce.dto.usuario.criacao.UsuarioCriarDto;
import dev.trier.ecommerce.dto.usuario.criacao.UsuarioResponseDto;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.repository.UsuarioRepository;
import dev.trier.ecommerce.security.TokenConfig;
import dev.trier.ecommerce.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro de usuários")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Login do usuário", description = "Realiza a autenticação do usuário e retorna um token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.dsEmail(), request.dsSenha());
        authenticationManager.authenticate(authToken);

        UsuarioModel usuario = usuarioRepository.findByDsEmail(request.dsEmail()).orElseThrow();
        String token = tokenConfig.generateToken(usuario);


        return ResponseEntity.ok(new LoginResponse(token, usuario.getCdUsuario(), usuario.getDsEmail(),usuario.getUserRole()));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<UsuarioResponseDto> register(@Valid @RequestBody UsuarioCriarDto dto) {
        UsuarioResponseDto created = usuarioService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}

