package dev.trier.ecommerce.repository;


import dev.trier.ecommerce.dto.produto.response.ListarProdutosResponseDto;
import dev.trier.ecommerce.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Integer> {
    Optional<ProdutoModel> findByCdProduto(Integer cdProduto);
    Optional<ProdutoModel> findByNmProduto(String nmProduto);


    @Modifying
    @Transactional
    @Query("delete from ProdutoModel p where p.empresa.cdEmpresa = :cdEmpresa")
    void deleteAllByEmpresa_CdEmpresa(Integer cdEmpresa);

}
