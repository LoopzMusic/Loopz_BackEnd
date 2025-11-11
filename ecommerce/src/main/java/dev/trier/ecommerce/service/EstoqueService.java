package dev.trier.ecommerce.service;

import dev.trier.ecommerce.dto.empresa.criacao.EmpresaCriadaRespostaDto;
import dev.trier.ecommerce.dto.estoque.criacao.EstoqueCriadoRespostaDto;
import dev.trier.ecommerce.dto.estoque.criacao.EstoqueCriarDto;
import dev.trier.ecommerce.dto.estoque.criacao.ListarEstoqueResponseDto;
import dev.trier.ecommerce.dto.estoque.modificacao.EstoqueUpdateDto;
import dev.trier.ecommerce.model.EstoqueModel;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.repository.EstoqueRepository;
import dev.trier.ecommerce.repository.ProdutoRespository;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRespository produtoRespository;

    //Metodo criad estoque
    @Transactional
    public EstoqueCriadoRespostaDto criarEstoque(EstoqueCriarDto estoqueCriarDto) {

        ProdutoModel produtoModel = produtoRespository.findById(estoqueCriarDto.cdProduto())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado para o código: " + estoqueCriarDto.cdProduto()));  //Procura cdProduto antes de criar no estoque

        EstoqueModel estoqueModel = new EstoqueModel();
        estoqueModel.setProduto(produtoModel);
        estoqueModel.setQtdEstoqueProduto(estoqueCriarDto.qtdEstoqueProduto());
        EstoqueModel salvar = estoqueRepository.save(estoqueModel);

        return new EstoqueCriadoRespostaDto(
            salvar.getCdEstoque(),
                salvar.getProduto().getCdProduto(),
                salvar.getProduto().getNmProduto(),
                salvar.getQtdEstoqueProduto()
        );

    }

    //Metodo atualizar estoque
    @Transactional
    public EstoqueCriadoRespostaDto atualizarEstoque(Integer cdEstoque, EstoqueUpdateDto updateDto) {
        EstoqueModel estoqueModel = estoqueRepository.findByCdEstoque(cdEstoque)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estoque não encontrado: " + cdEstoque));

        // If cdProduto is provided, fetch the product and set it
        if (updateDto.cdProduto() != null) {
            ProdutoModel produtoModel = produtoRespository.findById(updateDto.cdProduto())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + updateDto.cdProduto()));
            estoqueModel.setProduto(produtoModel);
        }

        // Copy other non-null properties (like qtdEstoqueProduto)
        Utils.copyNonNullProperties(updateDto, estoqueModel, "cdEstoque", "produto", "flAtivo");

        EstoqueModel salvo = estoqueRepository.save(estoqueModel);

        return new EstoqueCriadoRespostaDto(
                salvo.getCdEstoque(),
                salvo.getProduto() != null ? salvo.getProduto().getCdProduto() : null,
                salvo.getProduto() != null ? salvo.getProduto().getNmProduto() : null,
                salvo.getQtdEstoqueProduto()
        );
    }

    //Metodo listar estoque
    public List<ListarEstoqueResponseDto> listarEstoque(){
        return estoqueRepository.findAll()
                .stream()
                .map(estoqueModel -> new ListarEstoqueResponseDto(
                        estoqueModel.getCdEstoque(),
                        estoqueModel.getQtdEstoqueProduto(),
                        estoqueModel.getProduto().getCdProduto(),
                        estoqueModel.getProduto().getNmProduto(),
                        estoqueModel.getProduto().getFlAtivo()
                ))
                .toList();
    }

    //Metodo para diminuir estoque
    @Transactional
    public void diminuirEstoqueProduto(Integer cdProduto, Integer qtdEstoqueProduto) {
        EstoqueModel estoqueModel = estoqueRepository.findByProduto_CdProduto(cdProduto);
        if (estoqueModel.getQtdEstoqueProduto() < qtdEstoqueProduto) {
            throw new RecursoNaoEncontradoException("Estoque insuficente (estoque atual: " + estoqueModel.getQtdEstoqueProduto() + ")");
        }
        estoqueModel.setQtdEstoqueProduto(estoqueModel.getQtdEstoqueProduto() - qtdEstoqueProduto);
        estoqueRepository.save(estoqueModel);
    }



}