package dev.trier.ecommerce.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "TBITEMPEDIDO")
public class ItemPedidoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdItemPedido;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "cdPedido")
    private PedidoModel pedido;

    @ManyToOne
    @JoinColumn(name = "cdProduto")
    private ProdutoModel produto;

    @Column(nullable = false)
    private Double vlItemPedido;

    private Integer qtItem;
}
