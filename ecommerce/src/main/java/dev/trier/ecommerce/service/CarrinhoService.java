package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriadoResponseDto;
import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriarDto;
import dev.trier.ecommerce.dto.carrinho.response.CarrinhoResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.carrinho.CarrinhoModel;
import dev.trier.ecommerce.model.enums.StatusPedido;
import dev.trier.ecommerce.repository.ProdutoRepository;
import dev.trier.ecommerce.repository.UsuarioRepository;
import dev.trier.ecommerce.repository.carrinho.CarrinhoRepository;
import dev.trier.ecommerce.repository.carrinho.ItemCarrinhoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CarrinhoService {
    private final ProdutoRepository produtoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;

    @Transactional
    public CarrinhoCriadoResponseDto criarCarrinho(CarrinhoCriarDto carrinhoCriarDto) {
        UsuarioModel usuarioModel = usuarioRepository.findByCdUsuario(carrinhoCriarDto.cdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        CarrinhoModel carrinhoModel = new CarrinhoModel();
        carrinhoModel.setUsuario(usuarioModel);
        carrinhoModel.setStatusCarrinho(StatusPedido.ABERTO);

        CarrinhoModel salvo = carrinhoRepository.save(carrinhoModel);

        return new CarrinhoCriadoResponseDto(
                salvo.getCdCarrinho(),
                salvo.getUsuario().getCdUsuario(),
                salvo.getStatusCarrinho(),
                salvo.getCriadoEm()
        );
    }



}
