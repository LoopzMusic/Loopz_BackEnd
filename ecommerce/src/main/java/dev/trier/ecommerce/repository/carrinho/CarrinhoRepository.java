package dev.trier.ecommerce.repository.carrinho;

import dev.trier.ecommerce.model.carrinho.CarrinhoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CarrinhoRepository extends JpaRepository<CarrinhoModel, Integer> {
    Optional<CarrinhoRepository> findByCdCarrinho(Integer cdCarrinho);
}
