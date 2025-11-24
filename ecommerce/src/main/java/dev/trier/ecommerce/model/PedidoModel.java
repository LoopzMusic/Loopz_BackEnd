package dev.trier.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.trier.ecommerce.model.enums.FormaPagamento;
import dev.trier.ecommerce.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Audited
@Table(name = "TBPEDIDO")
public class PedidoModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdPedido;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cdUsuario")
    private UsuarioModel usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;

    @Column(nullable = false)
    private Double vlFrete;

    @Column(nullable = false)
    private Double vlTotalPedido;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private StatusPedido statusPedido = StatusPedido.ABERTO;

    @NotAudited
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoModel> itensPedido;
}
