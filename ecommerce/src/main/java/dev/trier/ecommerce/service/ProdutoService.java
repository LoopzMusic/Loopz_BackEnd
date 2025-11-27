package dev.trier.ecommerce.service;


import dev.trier.ecommerce.dto.produto.criacao.CriarProdutoResponseDto;

import dev.trier.ecommerce.dto.produto.response.*;
import dev.trier.ecommerce.dto.produto.response.ProdutoTextUpdateDto;
import dev.trier.ecommerce.dto.produto.criacao.ProdutoCriarDto;
import dev.trier.ecommerce.exceptions.RecursoNaoEncontradoException;
import dev.trier.ecommerce.model.EmpresaModel;
import dev.trier.ecommerce.model.EstoqueModel;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.enums.CategoriaProduto;
import dev.trier.ecommerce.repository.EmpresaRepository;
import dev.trier.ecommerce.repository.EstoqueRepository;
import dev.trier.ecommerce.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.trier.ecommerce.exceptions.EntityInUseException;
import dev.trier.ecommerce.repository.ItemPedidoRepository;

@AllArgsConstructor
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EmpresaRepository empresaRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final EstoqueRepository estoqueRepository;

    @Transactional
    public CriarProdutoResponseDto criarProduto(ProdutoCriarDto produtoCriarDto) {
        EmpresaModel empresaModel = empresaRepository.findById(produtoCriarDto.cdEmpresa())
                .orElseThrow(
                        () -> new RuntimeException("Empresa não encontrada para o código: " + produtoCriarDto.cdEmpresa()) // Procura empresa antes de criar o produto
                );
        ProdutoModel produtoModel = new ProdutoModel(); 
        produtoModel.setNmProduto(produtoCriarDto.nmProduto());
        produtoModel.setVlProduto(produtoCriarDto.vlProduto());
        produtoModel.setDsCategoria(CategoriaProduto.valueOf(produtoCriarDto.dsCategoria()));
        produtoModel.setDsProduto(produtoCriarDto.dsProduto());
        produtoModel.setDsAcessorio(produtoCriarDto.dsAcessorio());
        produtoModel.setEmpresa(empresaModel);

        MultipartFile imgProduto = produtoCriarDto.imgProduto();
        if (imgProduto != null && !imgProduto.isEmpty()) {
            try {
                produtoModel.setImgProduto(imgProduto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar imagem do produto", e);
            }
        }
        ProdutoModel salvo = produtoRepository.save(produtoModel);
        return new CriarProdutoResponseDto(
                salvo.getCdProduto(),
                salvo.getNmProduto(),
                salvo.getVlProduto(),
                salvo.getDsCategoria(),
                salvo.getDsProduto(),
                salvo.getDsAcessorio(),
                salvo.getEmpresa().getCdEmpresa()
        );
    }


    public List<ListarProdutosResponseDto> listarProdutos() {
        return produtoRepository.findAll()
                .stream()
                .map(produto -> {
                    int qtdEstoque = Stream.ofNullable(produto.getEstoques())
                            .flatMap(List::stream)
                            .filter(e -> "S".equalsIgnoreCase(e.getFlAtivo()))
                            .mapToInt(e -> Optional.ofNullable(e.getQtdEstoqueProduto()).orElse(0))
                            .sum();

                    return new ListarProdutosResponseDto(
                            produto.getNmProduto(),
                            produto.getVlProduto(),
                            produto.getDsCategoria().toString(),
                            produto.getDsAcessorio(),
                            produto.getDsProduto(),
                            produto.getCdProduto(),
                            produto.getEmpresa().getCdEmpresa(),
                            qtdEstoque
                    );
                })
                .collect(Collectors.toList());
    }

    public ProdutoModel buscarProdutoPorId(Integer cdProduto) {
        return produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(
                        () -> new RuntimeException("Produto não encontrado"));
    }

    @Transactional(readOnly = true)
    public ListarProdutoDetalhadoResponseDto buscarProdutoPorIdMaisDetalhes(Integer cdProduto) {
        ProdutoModel produto = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + cdProduto));

        int qtdEstoque = Optional.ofNullable(produto.getEstoques())
                .orElse(List.of())
                .stream()
                .filter(e -> "S".equalsIgnoreCase(e.getFlAtivo()))
                .mapToInt(e -> Optional.ofNullable(e.getQtdEstoqueProduto()).orElse(0))
                .sum();

        return new ListarProdutoDetalhadoResponseDto(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getVlProduto(),
                produto.getDsCategoria(),
                produto.getDsProduto(),
                String.valueOf(produto.getDsAcessorio()),
                produto.getEmpresa().getCdEmpresa(),
                qtdEstoque
        );
    }


    public Optional<ProdutoIdResponseDto> buscarProdutoId(Integer cdProduto) {
        return produtoRepository.findByCdProduto(cdProduto)
                .map(produto -> new ProdutoIdResponseDto(
                        produto.getNmProduto(),
                        produto.getVlProduto(),
                        produto.getDsProduto(),
                        produto.getDsCategoria()
                ));

    }

    @Transactional
    public ProdutoTextUpdateResponseDto atualizarProdutoTexto(ProdutoTextUpdateDto updateProdutoDto, Integer cdProduto) {
        ProdutoModel produtoModel = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + cdProduto));

        if (updateProdutoDto.nmProduto() != null) {
            produtoModel.setNmProduto(updateProdutoDto.nmProduto());
        }
        if (updateProdutoDto.vlProduto() != null) {
            produtoModel.setVlProduto(updateProdutoDto.vlProduto());
        }
        if (updateProdutoDto.dsProduto() != null) {
            produtoModel.setDsProduto(updateProdutoDto.dsProduto());
        }


        if (updateProdutoDto.dsCategoria() != null) {
            try {
                produtoModel.setDsCategoria(CategoriaProduto.valueOf(updateProdutoDto.dsCategoria()));
            } catch (IllegalArgumentException ex) {
                throw new RecursoNaoEncontradoException("Categoria inválida: " + updateProdutoDto.dsCategoria());
            }
        }

        if (updateProdutoDto.dsAcessorio() != null && !updateProdutoDto.dsAcessorio().isEmpty()) {
            produtoModel.setDsAcessorio(updateProdutoDto.dsAcessorio().charAt(0));
        }


        if (updateProdutoDto.cdEmpresa() != null) {
            EmpresaModel empresaModel = empresaRepository.findById(updateProdutoDto.cdEmpresa())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada: " + updateProdutoDto.cdEmpresa()));
            produtoModel.setEmpresa(empresaModel);
        }


        ProdutoModel salvo = produtoRepository.save(produtoModel);

        Integer qtdEstoqueAtual = null;

        if (updateProdutoDto.qtdEstoque() != null) {
            EstoqueModel estoque = estoqueRepository.findByProduto_CdProduto(cdProduto);
            if (estoque == null) {
                estoque = new EstoqueModel();
                estoque.setProduto(salvo);
                estoque.setFlAtivo("S");
            }
            estoque.setQtdEstoqueProduto(updateProdutoDto.qtdEstoque());
            EstoqueModel salvoEstoque = estoqueRepository.save(estoque);
            qtdEstoqueAtual = salvoEstoque.getQtdEstoqueProduto();
        } else {

            EstoqueModel estoqueExistente = estoqueRepository.findByProduto_CdProduto(cdProduto);
            qtdEstoqueAtual = estoqueExistente != null ? estoqueExistente.getQtdEstoqueProduto() : 0;
        }

        return new ProdutoTextUpdateResponseDto(
                salvo.getNmProduto(),
                salvo.getVlProduto(),
                salvo.getDsProduto(),
                salvo.getDsCategoria() != null ? salvo.getDsCategoria().name() : null,
                String.valueOf(salvo.getDsAcessorio()),
                salvo.getEmpresa() != null ? salvo.getEmpresa().getCdEmpresa() : null,
                qtdEstoqueAtual
        );
    }

    @Transactional
    public void atualizarImagemProduto(Integer cdProduto, MultipartFile imgProduto) {
        ProdutoModel produtoModel = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + cdProduto));

        if (imgProduto == null || imgProduto.isEmpty()) {
            throw new IllegalArgumentException("Imagem inválida ou vazia.");
        }

        try {
            produtoModel.setImgProduto(imgProduto.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar imagem do produto", e);
        }

        produtoRepository.save(produtoModel);
    }

    @Transactional(readOnly = true)
    public byte[] buscarImagemProduto(Integer cdProduto) {
        ProdutoModel produtoModel = produtoRepository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + cdProduto));
        return produtoModel.getImgProduto();
    }

    public Optional<ProdutoNomeResponseDto> listarProdutoNome(String nmProduto) {
        return produtoRepository.findByNmProduto(nmProduto)
                .map(produto -> new ProdutoNomeResponseDto(
                        produto.getNmProduto(),
                        produto.getVlProduto(),
                        produto.getDsCategoria(),
                        produto.getDsProduto()
                ));
    }


    @Transactional
    public void removerProduto(Integer cdProduto) {
        boolean usada = itemPedidoRepository.existsByProduto_CdProduto(cdProduto);
        if (usada) {
            throw new EntityInUseException("Produto já está presente em um ItemPedido e não pode ser excluído.");
        }

        produtoRepository.deleteById(cdProduto);
    }

}