package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {
    Optional<PedidoModel> findByCdPedido(Integer cdPedido);

    @Query("""
    SELECT DISTINCT p FROM PedidoModel p
    LEFT JOIN FETCH p.itensPedido i
    LEFT JOIN FETCH i.produto prod
    WHERE p.usuario.cdUsuario = :cdUsuario
""")
    List<PedidoModel> findByCdUsuarioComItem(@Param("cdUsuario") Integer cdUsuario);
}
