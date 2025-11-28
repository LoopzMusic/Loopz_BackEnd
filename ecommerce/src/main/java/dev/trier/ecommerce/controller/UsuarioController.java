package dev.trier.ecommerce.controller;

import dev.trier.ecommerce.dto.usuario.criacao.UsuarioCriarDto;
import dev.trier.ecommerce.dto.usuario.criacao.UsuarioResponseDto;
import dev.trier.ecommerce.dto.usuario.modificacao.UsuarioUpdateDto;
import dev.trier.ecommerce.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Capacidade de criação e modificação do usuário")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping(path = "/listar/usuarios")
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários cadastrados")
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        return ResponseEntity.ok().body(usuarioService.listarUsuarios());
    }


    @GetMapping("/buscar/{cdCliente}")
    @Operation(summary = "Buscar usuário por código", description = "Retorna os dados do usuário pelo código informado")
    public Optional<UsuarioResponseDto>  listarCdUsuario(@PathVariable Integer cdCliente) {
        return usuarioService.listarCdUsuario(cdCliente);
    }


    @GetMapping("/nome/{nmCliente}")
    @Operation(summary = "Buscar usuário por nome", description = "Retorna os dados do usuário pelo nome informado")
    public ResponseEntity<?> listarUsuarioNome(@PathVariable String nmCliente) {
        Optional<UsuarioResponseDto> usuario = usuarioService.listarUsuarioNome(nmCliente);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/update/{cdUsuario}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário pelo código")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Integer cdUsuario,
                                                                @RequestBody @Valid UsuarioUpdateDto usuarioUpdateDto) {
        try {
            UsuarioResponseDto atualizado = usuarioService.atualizarUsuario(cdUsuario, usuarioUpdateDto);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping("/{cdUsuario}/perfil")
    @Operation(summary = "Atualizar perfil do usuário", description = "Atualiza as informações de perfil do usuário")
    public ResponseEntity<UsuarioResponseDto> updateUserProfile(
            @PathVariable Integer cdUsuario,
            @Valid @RequestBody UsuarioCriarDto dto) {

        UsuarioResponseDto updated = usuarioService.atualizarPerfil(cdUsuario, dto);
        return ResponseEntity.ok(updated);
    }
}
