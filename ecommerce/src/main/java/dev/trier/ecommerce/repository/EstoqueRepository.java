package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.EstoqueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueModel, Integer> {
    Optional<EstoqueModel> findByCdEstoque(Integer cdEstoque);
    EstoqueModel findByProduto_CdProduto(Integer cdProduto);

}
