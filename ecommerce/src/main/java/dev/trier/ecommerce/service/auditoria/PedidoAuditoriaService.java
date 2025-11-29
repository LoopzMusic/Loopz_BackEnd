package dev.trier.ecommerce.service.auditoria;

import dev.trier.ecommerce.dto.auditoria.PedidoAuditoriaDto;
import dev.trier.ecommerce.model.ItemPedidoModel;
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
import java.util.*;

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

        PedidoModel current = auditReader.find(PedidoModel.class, entityId, revisionId);
        PedidoModel previous = null;
        if (currentIndex > 0) {
            Number previousRevisionId = revisions.get(currentIndex - 1);
            previous = auditReader.find(PedidoModel.class, entityId, previousRevisionId);
        }

        for (Field field : PedidoModel.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object currentValue = field.get(current);
                Object previousValue = previous != null ? field.get(previous) : null;

                boolean changed = (currentValue != null && !currentValue.equals(previousValue)) ||
                        (currentValue == null && previousValue != null) ||
                        (currentValue != null && previousValue == null);


                if ("itensPedido".equals(field.getName())) {
                    PedidoModel pedidoOriginal = entityManager.find(PedidoModel.class, entityId);
                    List<Map<String, Object>> itensMap = new ArrayList<>();
                    for (ItemPedidoModel item : pedidoOriginal.getItensPedido()) {
                        Map<String, Object> itemMap = new HashMap<>();
                        if (item.getProduto() != null) {
                            itemMap.put("cdProduto", item.getProduto().getCdProduto());
                            itemMap.put("nmProduto", item.getProduto().getNmProduto());
                        }
                        itemMap.put("qtItem", item.getQtItem());
                        itemMap.put("vlItemPedido", item.getVlItemPedido());
                        itensMap.add(itemMap);
                    }
                    changes.put("itensPedido", itensMap);
                }



                else if (field.getType().isPrimitive()
                        || field.getType().getName().startsWith("java.lang")
                        || field.getType().getName().startsWith("dev.trier.ecommerce.model.enums")) {
                    if (changed) {
                        changes.put(field.getName(), currentValue);
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return changes;
    }
}
