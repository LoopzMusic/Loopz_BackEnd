package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaModel, Integer> {
    Optional<EmpresaModel> findByCdEmpresa(Integer cdEmpresa);
    Optional<EmpresaModel> findByNuCNPJ (String nuCNPJ);
    Optional<EmpresaModel> findByNmRazao (String nmRazao);
    Optional<EmpresaModel> findByNmFantasia (String nmFantasia);

}
