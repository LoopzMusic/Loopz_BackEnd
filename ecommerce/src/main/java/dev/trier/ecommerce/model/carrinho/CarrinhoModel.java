package dev.trier.ecommerce.model.carrinho;

import dev.trier.ecommerce.model.UsuarioModel;
import dev.trier.ecommerce.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TBCARRINHO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CarrinhoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdCarrinho;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdUsuario", nullable = false)
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinhoModel> itensCarrinho;

    @CreatedDate
    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusCarrinho;

    @LastModifiedDate
    private LocalDateTime atualizadoEm;


}
