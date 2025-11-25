package dev.trier.ecommerce.repository.carrinho;

import dev.trier.ecommerce.model.carrinho.ItemCarrinhoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinhoModel, Integer> {
    Optional<ItemCarrinhoModel> findByCarrinho_CdCarrinhoAndProduto_CdProduto(Integer cdCarrinho, Integer cdProduto);
}
