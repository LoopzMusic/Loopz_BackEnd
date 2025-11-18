package dev.trier.ecommerce.repository.carrinho;

import dev.trier.ecommerce.model.carrinho.ItemCarrinhoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinhoModel, Integer> {
    Optional<ItemCarrinhoRepository> findByCdItemCarrinho(Integer cdItemCarrinho);
}
