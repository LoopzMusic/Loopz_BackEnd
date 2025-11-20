package dev.trier.ecommerce.repository.views;

import dev.trier.ecommerce.model.views.ProdutoCompradoJuntoModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutosCompradosJuntosRepository
        extends JpaRepository<ProdutoCompradoJuntoModel, Integer> {

    @Query(value = """
            SELECT *
            FROM vw_produtos_comprados_juntos
            WHERE produto_origem = :id
            """, nativeQuery = true)
    List<ProdutoCompradoJuntoModel> findRecomendados(@Param("id") Integer id);
}
