package dev.trier.ecommerce.repository.carrinho;

import dev.trier.ecommerce.model.carrinho.CarrinhoModel;
import dev.trier.ecommerce.model.enums.StatusCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CarrinhoRepository extends JpaRepository<CarrinhoModel, Integer> {
    Optional<CarrinhoModel> findByCdCarrinho(Integer cdCarrinho);
    Optional<CarrinhoModel> findByUsuario_CdUsuarioAndStatusCarrinho(
            Integer cdUsuario,
            StatusCarrinho statusCarrinho
    );

}
