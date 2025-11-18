package dev.trier.ecommerce.repository.views;


import dev.trier.ecommerce.model.views.ProdutoMaisVendidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosMaisVendidosRepository
        extends JpaRepository<ProdutoMaisVendidoModel, Integer> {
}
