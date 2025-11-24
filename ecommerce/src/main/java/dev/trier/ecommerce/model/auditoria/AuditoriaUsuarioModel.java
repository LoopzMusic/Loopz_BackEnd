package dev.trier.ecommerce.model.auditoria;

import dev.trier.ecommerce.security.JWTUserData;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;


public class AuditoriaUsuarioModel implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditoriaModel revisao = (AuditoriaModel) revisionEntity;

        var auth = SecurityContextHolder.getContext().getAuthentication();

        String usuario = "sistema";

        if (auth != null && auth.getPrincipal() instanceof JWTUserData data) {
            usuario = String.valueOf(data.cdUsuario());
        }

        revisao.setCdUsuario(usuario);
        revisao.setDataAlteracao(Instant.now());
    }
}


