package dev.trier.ecommerce.model.auditoria;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Entity
@Getter
@Setter
@NoArgsConstructor
@RevisionEntity(AuditoriaUsuarioModel.class)
public class AuditoriaModel {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @RevisionNumber
    private Integer id;

    @RevisionTimestamp
    private long revisionTimestamp;

    private String cdUsuario;

    private Instant dataAlteracao;


    public LocalDateTime getDataAlteracao() {
        return LocalDateTime.ofInstant(dataAlteracao, ZoneId.systemDefault());
    }

}
