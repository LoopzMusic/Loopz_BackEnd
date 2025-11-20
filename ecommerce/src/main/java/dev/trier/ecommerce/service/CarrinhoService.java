package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriadoResponseDto;
import dev.trier.ecommerce.dto.carrinho.criacao.CarrinhoCriarDto;
import dev.trier.ecommerce.dto.carrinho.modificacao.CarrinhoStatusUpdateDto;
import dev.trier.ecommerce.dto.carrinho.response.CarrinhoResponseDto;
import dev.trier.ecommerce.dto.carrinho.response.ItemCarrinhoDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.carrinho.CarrinhoModel;
import dev.trier.ecommerce.model.enums.StatusCarrinho;
import dev.trier.ecommerce.repository.UsuarioRepository;
import dev.trier.ecommerce.repository.carrinho.CarrinhoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CarrinhoCriadoResponseDto buscarOuCriarCarrinho(Integer cdUsuario) {
        UsuarioModel usuarioModel = usuarioRepository.findByCdUsuario(cdUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        Optional<CarrinhoModel> carrinhoExistente = carrinhoRepository
                .findByUsuario_CdUsuarioAndStatusCarrinho(cdUsuario, StatusCarrinho.ABERTO);

        CarrinhoModel carrinho;
        if (carrinhoExistente.isPresent()) {
            carrinho = carrinhoExistente.get();
        } else {
            carrinho = new CarrinhoModel();
            carrinho.setUsuario(usuarioModel);
            carrinho.setStatusCarrinho(StatusCarrinho.ABERTO);
            carrinho = carrinhoRepository.save(carrinho);
        }

        return new CarrinhoCriadoResponseDto(
                carrinho.getCdCarrinho(),
                carrinho.getUsuario().getCdUsuario(),
                carrinho.getStatusCarrinho(),
                carrinho.getCriadoEm()
        );
    }


    @Transactional(readOnly = true)
    public Optional<CarrinhoResponseDto> buscarCarrinhoAberto(Integer cdUsuario) {
        return carrinhoRepository
                .findByUsuario_CdUsuarioAndStatusCarrinho(cdUsuario, StatusCarrinho.ABERTO)
                .map(this::converterParaResponseDto);
    }

    @Transactional(readOnly = true)
    public CarrinhoResponseDto buscarCarrinhoPorId(Integer cdCarrinho) {
        CarrinhoModel carrinho = carrinhoRepository.findById(cdCarrinho)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado: " + cdCarrinho));

        return converterParaResponseDto(carrinho);
    }

    @Transactional
    public CarrinhoCriadoResponseDto alterarStatusCarrinho(Integer cdCarrinho, CarrinhoStatusUpdateDto updateDto) {
        CarrinhoModel carrinhoModel = carrinhoRepository.findById(cdCarrinho)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado: " + cdCarrinho));

        if (updateDto.statusCarrinho() != null) {
            try {
                StatusCarrinho novoStatus = StatusCarrinho.valueOf(updateDto.statusCarrinho().toUpperCase());
                carrinhoModel.setStatusCarrinho(novoStatus);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status de carrinho inválido: " + updateDto.statusCarrinho());
            }
        }

        CarrinhoModel salvo = carrinhoRepository.save(carrinhoModel);

        return new CarrinhoCriadoResponseDto(
                salvo.getCdCarrinho(),
                salvo.getUsuario().getCdUsuario(),
                salvo.getStatusCarrinho(),
                salvo.getCriadoEm()
        );
    }

    @Transactional
    public void limparCarrinho(Integer cdCarrinho) {
        CarrinhoModel carrinho = carrinhoRepository.findById(cdCarrinho)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado: " + cdCarrinho));

        if (carrinho.getItensCarrinho() != null) {
            carrinho.getItensCarrinho().clear();
            carrinhoRepository.save(carrinho);
        }
    }

    private CarrinhoResponseDto converterParaResponseDto(CarrinhoModel carrinho) {
        List<ItemCarrinhoDto> itensDto =
                carrinho.getItensCarrinho() == null ?
                        java.util.Collections.emptyList() :
                        carrinho.getItensCarrinho().stream()
                                .map(item -> {
                                    BigDecimal precoUnitario = BigDecimal.valueOf(item.getProduto().getVlProduto());
                                    BigDecimal quantidade = BigDecimal.valueOf(item.getQtdItemCarrinho());
                                    BigDecimal valorTotal = precoUnitario.multiply(quantidade);

                                    return new ItemCarrinhoDto(
                                            item.getCdItensCarrinho(),
                                            item.getProduto().getCdProduto(),
                                            item.getProduto().getNmProduto(),
                                            item.getQtdItemCarrinho(),
                                            precoUnitario,
                                            valorTotal
                                    );
                                })
                                .collect(Collectors.toList());


        BigDecimal valorTotalCarrinho = itensDto.stream()
                .map(ItemCarrinhoDto::valorTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarrinhoResponseDto(
                carrinho.getCdCarrinho(),
                carrinho.getUsuario().getCdUsuario(),
                carrinho.getUsuario().getNmCliente(),
                itensDto,
                carrinho.getStatusCarrinho(),
                valorTotalCarrinho,
                carrinho.getCriadoEm(),
                carrinho.getAtualizadoEm()
        );
    }
}