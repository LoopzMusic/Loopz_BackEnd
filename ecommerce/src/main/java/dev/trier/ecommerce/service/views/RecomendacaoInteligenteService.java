package dev.trier.ecommerce.service.views;

import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.repository.ProdutoRespository;
import dev.trier.ecommerce.repository.views.ProdutosCompradosJuntosRepository;
import dev.trier.ecommerce.repository.views.ProdutosMaisVendidosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecomendacaoInteligenteService {
    private final ProdutosCompradosJuntosRepository produtosCompradosJuntosRepository;
    private final ProdutosMaisVendidosRepository produtosMaisVendidosRepository;
    private final ProdutoRespository produtoRepository;

    public List<ProdutoModel> recomendar(Integer idProduto) {
        var relacionados = produtosCompradosJuntosRepository.findRecomendados(idProduto);

        return relacionados.stream()
                .map(r -> produtoRepository.findById(r.getProdutoRecomendado()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ProdutoModel> listarMaisVendidos() {
        var lista = produtosMaisVendidosRepository.findAll();

        return lista.stream()
                .map(v -> produtoRepository.findById(v.getCdProduto()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }
}
