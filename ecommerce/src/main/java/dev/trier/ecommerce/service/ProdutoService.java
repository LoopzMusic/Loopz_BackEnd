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
import dev.trier.ecommerce.repository.ProdutoRespository;
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

    private final ProdutoRespository produtoRespository;
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
        produtoModel.setEmpresa(empresaModel);

        MultipartFile imgProduto = produtoCriarDto.imgProduto();
        if (imgProduto != null && !imgProduto.isEmpty()) {
            try {
                produtoModel.setImgProduto(imgProduto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar imagem do produto", e);
            }
        }
        ProdutoModel salvo = produtoRespository.save(produtoModel);
        return new CriarProdutoResponseDto(
                salvo.getCdProduto(),
                salvo.getNmProduto(),
                salvo.getVlProduto(),
                salvo.getDsCategoria(),
                salvo.getDsProduto(),
                salvo.getCdProduto()
        );
    }

    //Vericar os dados do DTO
    public List<ListarProdutosResponseDto> listarProdutos() {
        return produtoRespository.findAll()
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
                            produto.getDsProduto(),
                            produto.getImgProduto(),
                            produto.getCdProduto(),
                            produto.getEmpresa().getCdEmpresa(),
                            qtdEstoque
                    );
                })
                .collect(Collectors.toList());
    }

//    public List<ProdutoIdResponseDto> listarProdutosPorCategoria(String categoria) {
//        CategoriaProduto cat = CategoriaProduto.fromString(categoria);
//        return produtoRespository.findAllByDsCategoria(cat).stream()
//                .map(produto -> new ProdutoIdResponseDto(
//                        produto.nmProduto(),
//                        produto.vlProduto(),
//                        produto.dsProduto(),
//                        produto.categoria()
//                ))
//                .toList();
//    }

    //Provavel GET busca Imagem
    public ProdutoModel buscarProdutoPorId(Integer cdProduto) {
        return produtoRespository.findByCdProduto(cdProduto)
                .orElseThrow(
                        () -> new RuntimeException("Produto não encontrado"));
    }

    public Optional<ProdutoIdResponseDto> buscarProdutoId(Integer cdProduto) {
        return produtoRespository.findByCdProduto(cdProduto)
                .map(produto -> new ProdutoIdResponseDto(
                        produto.getNmProduto(),
                        produto.getVlProduto(),
                        produto.getDsProduto(),
                        produto.getDsCategoria()
                ));

    }

    @Transactional
    public ProdutoTextUpdateResponseDto atualizarProdutoTexto(ProdutoTextUpdateDto updateProdutoDto, Integer cdProduto) {
        ProdutoModel produtoModel = produtoRespository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + cdProduto));

        // Atualiza apenas os campos permitidos: nmProduto, vlProduto, dsProduto
        if (updateProdutoDto.getNmProduto() != null) {
            produtoModel.setNmProduto(updateProdutoDto.getNmProduto());
        }
        if (updateProdutoDto.getVlProduto() != null) {
            produtoModel.setVlProduto(updateProdutoDto.getVlProduto());
        }
        if (updateProdutoDto.getDsProduto() != null) {
            produtoModel.setDsProduto(updateProdutoDto.getDsProduto());
        }

        ProdutoModel salvo = produtoRespository.save(produtoModel);
        return new ProdutoTextUpdateResponseDto(
                salvo.getNmProduto(),
                salvo.getVlProduto(),
                salvo.getDsProduto()
        );
    }

    // Atualizar apenas imagem
    @Transactional
    public void atualizarImagemProduto(Integer cdProduto, MultipartFile imgProduto) {
        ProdutoModel produtoModel = produtoRespository.findByCdProduto(cdProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + cdProduto));

        if (imgProduto != null && !imgProduto.isEmpty()) {
            try {
                produtoModel.setImgProduto(imgProduto.getBytes());
                produtoRespository.save(produtoModel);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar imagem do produto", e);
            }
        }
    }

    //Verificar uso de Optional ou List
    public Optional<ProdutoNomeResponseDto> listarProdutoNome(String nmProduto) {
        return produtoRespository.findByNmProduto(nmProduto)
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

        produtoRespository.deleteById(cdProduto);
    }

}