package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.ItemCarrinho.criacao.ItemCarrinhoCriarDto;
import dev.trier.ecommerce.dto.ItemCarrinho.modificacao.ItemCarrinhoUpdateDto;
import dev.trier.ecommerce.dto.ItemCarrinho.response.ItemCarrinhoCriadoResponseDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.carrinho.CarrinhoModel;
import dev.trier.ecommerce.model.carrinho.ItemCarrinhoModel;
import dev.trier.ecommerce.repository.ProdutoRepository;
import dev.trier.ecommerce.repository.carrinho.CarrinhoRepository;
import dev.trier.ecommerce.repository.carrinho.ItemCarrinhoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemCarrinhoService {

    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public ItemCarrinhoCriadoResponseDto adicionarItemAoCarrinho(Integer cdCarrinho, ItemCarrinhoCriarDto itemCriarDto) {

        CarrinhoModel carrinhoModel = carrinhoRepository.findById(cdCarrinho)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carrinho não encontrado: " + cdCarrinho));

        ProdutoModel produtoModel = produtoRepository.findById(itemCriarDto.cdProduto())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + itemCriarDto.cdProduto()));

        Optional<ItemCarrinhoModel> itemExistenteOptional = itemCarrinhoRepository.findByCarrinho_CdCarrinhoAndProduto_CdProduto(
                cdCarrinho, itemCriarDto.cdProduto());

        ItemCarrinhoModel itemSalvo;

        if (itemExistenteOptional.isPresent()) {
            ItemCarrinhoModel itemExistente = itemExistenteOptional.get();
            itemExistente.setQtdItemCarrinho(itemExistente.getQtdItemCarrinho() + itemCriarDto.qtdItemCarrinho());
            itemSalvo = itemCarrinhoRepository.save(itemExistente);
        } else {
            ItemCarrinhoModel novoItem = new ItemCarrinhoModel();
            novoItem.setCarrinho(carrinhoModel);
            novoItem.setProduto(produtoModel);
            novoItem.setQtdItemCarrinho(itemCriarDto.qtdItemCarrinho());
            itemSalvo = itemCarrinhoRepository.save(novoItem);
        }

        return new ItemCarrinhoCriadoResponseDto(
                itemSalvo.getCdItensCarrinho(),
                itemSalvo.getCarrinho().getCdCarrinho(),
                itemSalvo.getProduto().getCdProduto(),
                itemSalvo.getProduto().getNmProduto(),
                itemSalvo.getQtdItemCarrinho()
        );
    }

    @Transactional
    public ItemCarrinhoCriadoResponseDto atualizarItemCarrinho(Integer cdItemCarrinho, ItemCarrinhoUpdateDto updateDto) {

        ItemCarrinhoModel itemModel = itemCarrinhoRepository.findById(cdItemCarrinho)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item do Carrinho não encontrado: " + cdItemCarrinho));

        if (updateDto.qtdItemCarrinho() != null) {
            itemModel.setQtdItemCarrinho(updateDto.qtdItemCarrinho());
        }

        ItemCarrinhoModel itemSalvo = itemCarrinhoRepository.save(itemModel);

        return new ItemCarrinhoCriadoResponseDto(
                itemSalvo.getCdItensCarrinho(),
                itemSalvo.getCarrinho().getCdCarrinho(),
                itemSalvo.getProduto().getCdProduto(),
                itemSalvo.getProduto().getNmProduto(),
                itemSalvo.getQtdItemCarrinho()
        );
    }
}