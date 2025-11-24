package dev.trier.ecommerce.service.auditoria;
import dev.trier.ecommerce.dto.auditoria.PedidoAuditoriaDto;
import dev.trier.ecommerce.model.PedidoModel;
import dev.trier.ecommerce.model.auditoria.AuditoriaModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoAuditoriaService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PedidoAuditoriaDto> findAllRevisions() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(PedidoModel.class, false, true)
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();

        List<PedidoAuditoriaDto> dtos = new ArrayList<>();

        for (Object[] revision : revisions) {
            PedidoModel pedido = (PedidoModel) revision[0];
            AuditoriaModel auditoria = (AuditoriaModel) revision[1];
            RevisionType revisionType = (RevisionType) revision[2];


            Map<String, Object> alteracoes = getChanges(auditReader, pedido.getCdPedido(), auditoria.getId());


            PedidoAuditoriaDto dto = new PedidoAuditoriaDto(
                    auditoria.getId(),
                    auditoria.getDataAlteracao(),
                    auditoria.getCdUsuario(),
                    revisionType.name(),
                    alteracoes
            );
            dtos.add(dto);
        }

        return dtos;
    }


    private Map<String, Object> getChanges(AuditReader auditReader, Integer entityId, Integer revisionId) {
        Map<String, Object> changes = new HashMap<>();


        List<Number> revisions = auditReader.getRevisions(PedidoModel.class, entityId);
        int currentIndex = revisions.indexOf(revisionId);


        if (currentIndex == 0) {
            PedidoModel current = auditReader.find(PedidoModel.class, entityId, revisionId);
            for (Field field : PedidoModel.class.getDeclaredFields()) {
                try {
                    field.setAccessible(true);

                    if (field.getType().isPrimitive() || field.getType().getName().startsWith("java.lang") || field.getType().getName().startsWith("dev.trier.ecommerce.model.enums")) {
                        changes.put(field.getName(), field.get(current));
                    }
                } catch (IllegalAccessException e) {

                }
            }
            return changes;
        }


        if (currentIndex > 0) {
            Number previousRevisionId = revisions.get(currentIndex - 1);

            PedidoModel current = auditReader.find(PedidoModel.class, entityId, revisionId);
            PedidoModel previous = auditReader.find(PedidoModel.class, entityId, previousRevisionId);

            for (Field field : PedidoModel.class.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object currentValue = field.get(current);
                    Object previousValue = field.get(previous);


                    boolean changed = false;
                    if (currentValue == null && previousValue != null) {
                        changed = true;
                    } else if (currentValue != null && previousValue == null) {
                        changed = true;
                    } else if (currentValue != null && !currentValue.equals(previousValue)) {
                        changed = true;
                    }

                    if (changed) {

                        changes.put(field.getName(), currentValue);
                    }
                } catch (IllegalAccessException e) {

                }
            }
        }

        return changes;
    }
}
