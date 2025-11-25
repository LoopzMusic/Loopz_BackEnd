package dev.trier.ecommerce.service.acoesUsuario;

import dev.trier.ecommerce.dto.acoesUsuario.FavoritosCriarDto;
import dev.trier.ecommerce.dto.acoesUsuario.FavoritosResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.acoesUsuario.FavoritosModel;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.repository.ProdutoRepository;
import dev.trier.ecommerce.repository.UsuarioRepository;
import dev.trier.ecommerce.repository.acoesUsuario.FavoritosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoritosService {

    private final FavoritosRepository favoritosRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public FavoritosResponseDto favoritar(FavoritosCriarDto favoritosCriarDto) {
        UsuarioModel usuarioModel = usuarioRepository.findByCdUsuario(favoritosCriarDto.cdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + favoritosCriarDto.cdUsuario()));

        ProdutoModel produtoModel = produtoRepository.findByCdProduto(favoritosCriarDto.cdProduto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + favoritosCriarDto.cdProduto()));

        if (favoritosRepository.existsByUsuario_CdUsuarioAndProduto_CdProduto(favoritosCriarDto.cdUsuario(), favoritosCriarDto.cdProduto())) {

            FavoritosModel existente = favoritosRepository
                    .findByUsuario_CdUsuarioAndProduto_CdProduto(favoritosCriarDto.cdUsuario(), favoritosCriarDto.cdProduto())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Favorito não encontrado após verificação"));

            return new FavoritosResponseDto(existente.getCdFavoritos(), favoritosCriarDto.cdUsuario(), favoritosCriarDto.cdProduto());
        }

        FavoritosModel favoritosModel = new FavoritosModel();
        favoritosModel.setUsuario(usuarioModel);
        favoritosModel.setProduto(produtoModel);
        FavoritosModel salvo = favoritosRepository.save(favoritosModel);

        return new FavoritosResponseDto(salvo.getCdFavoritos(), usuarioModel.getCdUsuario(), produtoModel.getCdProduto());
    }

    @Transactional
    public void desfavoritar(Integer cdUsuario, Integer cdProduto) {
        boolean exists = favoritosRepository.existsByUsuario_CdUsuarioAndProduto_CdProduto(cdUsuario, cdProduto);
        if (!exists) {
            throw new RecursoNaoEncontradoException("Favorito não encontrado para usuário: " + cdUsuario + " e produto: " + cdProduto);
        }
        favoritosRepository.deleteByUsuario_CdUsuarioAndProduto_CdProduto(cdUsuario, cdProduto);
    }

    @Transactional(readOnly = true)
    public List<Integer> listarCdProdutosFavoritadosPorUsuario(Integer cdUsuario) {

        usuarioRepository.findByCdUsuario(cdUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + cdUsuario));

        List<FavoritosModel> favoritosModel = favoritosRepository.findByUsuario_CdUsuario(cdUsuario);
        return favoritosModel.stream()
                .map(fav -> fav.getProduto().getCdProduto())
                .collect(Collectors.toList());
    }
}
