package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.PagamentoModel;
import dev.trier.ecommerce.model.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoModel, Integer> {
    Optional<PagamentoModel> findByPreferenceId(String preferenceId);
    Optional<PagamentoModel> findByPaymentId(String paymentId);
    Optional<PagamentoModel> findByPedido_CdPedido(Integer cdPedido);
    List<PagamentoModel> findByStatus(StatusPagamento status);
}