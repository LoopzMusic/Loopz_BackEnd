package dev.trier.ecommerce.repository.acoesUsuario;

import dev.trier.ecommerce.model.acoesUsuario.FavoritosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritosRepository extends JpaRepository<FavoritosModel, Integer> {
    Optional<FavoritosModel> findByUsuario_CdUsuarioAndProduto_CdProduto(Integer cdUsuario, Integer cdProduto);

    boolean existsByUsuario_CdUsuarioAndProduto_CdProduto(Integer cdUsuario, Integer cdProduto);

    void deleteByUsuario_CdUsuarioAndProduto_CdProduto(Integer cdUsuario, Integer cdProduto);


    List<FavoritosModel> findByUsuario_CdUsuario(Integer cdUsuario);
}
