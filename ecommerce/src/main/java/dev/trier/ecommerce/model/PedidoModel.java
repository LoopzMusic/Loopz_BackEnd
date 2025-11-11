package dev.trier.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.trier.ecommerce.model.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "TBPEDIDO")
public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdPedido;

    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    @JoinColumn(name = "cdUsuario")
    private UsuarioModel usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPagamento formaPagamento;

    @Column(nullable = false)
    private Double vlFrete;

    @Column(nullable = false)
    private Double vlTotalPedido;

    private String flAtivo = "S";

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoModel> itensPedido;
}
