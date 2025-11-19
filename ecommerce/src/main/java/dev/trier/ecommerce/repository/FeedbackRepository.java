package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.acoesUsuario.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Integer> {

    List<FeedbackModel> findByProduto_CdProduto(Integer cdProduto);

    List<FeedbackModel> findByUsuario_CdUsuario(Integer cdUsuario);

    List<FeedbackModel> findByProduto_CdProdutoAndNuAvaliacao(Integer cdProduto, Integer nuAvalaicao);
    }


