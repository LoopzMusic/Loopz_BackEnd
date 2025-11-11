package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.usuario.criacao.UsuarioCriarDto;
import dev.trier.ecommerce.dto.usuario.criacao.UsuarioResponseDto;
import dev.trier.ecommerce.dto.usuario.modificacao.UsuarioUpdateDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.utils.Utils;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.enums.UsersRole;
import dev.trier.ecommerce.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UsuarioService {

    private EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDto criarUsuario(UsuarioCriarDto usuarioCriarDto){
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setUserRole(UsersRole.valueOf("USER"));
        usuarioModel.setNmCliente(usuarioCriarDto.nmCliente());
        usuarioModel.setDsEmail(usuarioCriarDto.dsEmail());
        // encode password
        usuarioModel.setDsSenha(passwordEncoder.encode(usuarioCriarDto.dsSenha()));
        usuarioModel.setNuCPF(usuarioCriarDto.nuCPF());
        usuarioModel.setNuRG(usuarioCriarDto.nuRG());
        usuarioModel.setDsCidade(usuarioCriarDto.dsCidade());
        usuarioModel.setDsEstado(usuarioCriarDto.dsEstado());
        usuarioModel.setNuTelefone(usuarioCriarDto.nuTelefone());
        usuarioModel.setDsEndereco(usuarioCriarDto.dsEndereco());
        usuarioModel.setNuEndereco(usuarioCriarDto.nuEndereco());
        UsuarioModel salvo = usuarioRepository.save(usuarioModel);

        return new UsuarioResponseDto(
                salvo.getNmCliente(),
                salvo.getDsEmail(),
                salvo.getNuTelefone(),
                salvo.getDsCidade(),
                salvo.getNuCPF(),
                salvo.getNuRG(),
                salvo.getNuEndereco(),
                salvo.getDsEndereco(),
                salvo.getDsEstado(),
                salvo.getFlAtivo()
        );
    }

    @Transactional
    public UsuarioResponseDto atualizarUsuario(Integer cdUsuario, UsuarioUpdateDto usuarioUpdateDto) {
        UsuarioModel usuarioModel = usuarioRepository.findByCdUsuario(cdUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + cdUsuario));

        
        Utils.copyNonNullProperties(usuarioUpdateDto, usuarioModel, "cdUsuario", "userRole", "pedidos", "flAtivo");

        UsuarioModel salvo = usuarioRepository.save(usuarioModel);

        return new UsuarioResponseDto(
                salvo.getNmCliente(),
                salvo.getDsEmail(),
                salvo.getNuTelefone(),
                salvo.getDsCidade(),
                salvo.getNuCPF(),
                salvo.getNuRG(),
                salvo.getNuEndereco(),
                salvo.getDsEndereco(),
                salvo.getDsEstado(),
                salvo.getFlAtivo()
        );
    }

    public Optional<UsuarioResponseDto> listarCdUsuario(Integer cdUsuario){
        return usuarioRepository.findByCdUsuario(cdUsuario)
                .map(usuario -> new UsuarioResponseDto(
                        usuario.getNmCliente(),
                        usuario.getNuCPF(),
                        usuario.getNuRG(),
                        usuario.getNuTelefone(),
                        usuario.getDsCidade(),
                        usuario.getDsEstado(),
                        usuario.getDsEndereco(),
                        usuario.getNuEndereco(),
                        usuario.getDsEmail(),
                        usuario.getFlAtivo()

                ));
    }

    public List<UsuarioResponseDto> listarUsuarios(){
        return usuarioRepository.findAll() 
                .stream()
                .map(usuario -> new UsuarioResponseDto(
                        usuario.getNmCliente(),
                        usuario.getNuCPF(),
                        usuario.getNuRG(),
                        usuario.getNuTelefone(),
                        usuario.getDsCidade(),
                        usuario.getDsEstado(),
                        usuario.getDsEndereco(),
                        usuario.getNuEndereco(),
                        usuario.getDsEmail(),
                        usuario.getFlAtivo()
                ))
                .collect(Collectors.toList()) ;
    }

    public Optional<UsuarioResponseDto> listarUsuarioNome(String nmCliente){
       return usuarioRepository.findByNmCliente(nmCliente)
               .map(usuario -> new UsuarioResponseDto(
                       usuario.getNmCliente(),
                       usuario.getNuCPF(),
                       usuario.getNuRG(),
                       usuario.getNuTelefone(),
                       usuario.getDsCidade(),
                       usuario.getDsEstado(),
                       usuario.getDsEndereco(),
                       usuario.getNuEndereco(),
                       usuario.getDsEmail(),
                       usuario.getFlAtivo()
               ));
    }

    public Optional<UsuarioResponseDto> listarUsuarioCPF(String nuCPF){
        return  usuarioRepository.findByNuCPF(nuCPF)
                .map(usuario -> new UsuarioResponseDto(
                        usuario.getNmCliente(),
                        usuario.getNuCPF(),
                        usuario.getNuRG(),
                        usuario.getNuTelefone(),
                        usuario.getDsCidade(),
                        usuario.getDsEstado(),
                        usuario.getDsEndereco(),
                        usuario.getNuEndereco(),
                        usuario.getDsEmail(),
                        usuario.getFlAtivo()
                ));
    }


}
