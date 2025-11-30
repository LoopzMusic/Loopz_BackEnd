package dev.trier.ecommerce.repository.SecretKeyGoogleFrete;

import dev.trier.ecommerce.model.ApiSecretKeyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiSecretKeyRepository extends JpaRepository<ApiSecretKeyModel, Long> {
    Optional<ApiSecretKeyModel> findById(Long id);
}
