package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    Optional<UsuarioModel> findByCdUsuario(Integer cdUsuario);
    Optional<UsuarioModel> findByNmCliente(String nmCliente);
    Optional<UsuarioModel> findByNuCPF(String nuCPF);
    Optional<UsuarioModel> findByDsEmail(String dsEmail);
}
