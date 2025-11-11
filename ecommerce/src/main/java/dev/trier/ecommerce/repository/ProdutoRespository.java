package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.dto.produto.response.ProdutoIdResponseDto;
import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.enums.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRespository extends JpaRepository<ProdutoModel, Integer> {
    Optional<ProdutoModel> findByCdProduto(Integer cdProduto);
    Optional<ProdutoModel> findByNmProduto(String nmProduto);

//    @Query("SELECT new dev.trier.ecommerce.dto.produto.response.ProdutoIdResponseDto(" +
//            "p.nmProduto, p.vlProduto, p.dsProduto, p.dsCategoria) " +
//            "FROM ProdutoModel p WHERE p.dsCategoria = :categoria")
//    List<ProdutoIdResponseDto> findAllByDsCategoria(@Param("categoria") CategoriaProduto categoria);

    // Remove todos os produtos pertencentes a uma empresa (usado ao remover empresa quando não houver referência em ItemPedido)
    @Modifying
    @Transactional
    @Query("delete from ProdutoModel p where p.empresa.cdEmpresa = :cdEmpresa")
    void deleteAllByEmpresa_CdEmpresa(Integer cdEmpresa);



}
