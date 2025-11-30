package dev.trier.ecommerce.repository;

import dev.trier.ecommerce.model.EmailCredenciais.EmailCredencialModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCredencialRepository extends JpaRepository<EmailCredencialModel, Long> {

}
