package dev.trier.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.trier.ecommerce.model.enums.UsersRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "TBUSUARIO",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dsEmail"}),
        @UniqueConstraint(columnNames = {"nuCPF"}),
        @UniqueConstraint(columnNames = {"nuRG"}),
        @UniqueConstraint(columnNames = {"nuTelefone"})
}
)
public class UsuarioModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdUsuario;

    @Enumerated(EnumType.STRING) // Tem que colocar ou admin ou user no banco
    private UsersRole userRole;

    private String nmCliente;

    private String dsEmail;

    private String dsSenha;

    private String nuCPF;

    private String nuTelefone;

    private String nuRG;

    private String dsCidade;

    private String dsEstado;

    private String dsEndereco;

    private String nuEndereco;

    private String flAtivo = "S";

    @JsonManagedReference
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoModel> pedidos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userRole == null) return List.of();
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getPassword() {
        return dsSenha;
    }

    @Override
    public String getUsername() {
        return dsEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "S".equalsIgnoreCase(flAtivo);
    }
}
