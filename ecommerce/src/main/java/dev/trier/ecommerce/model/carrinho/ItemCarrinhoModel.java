package dev.trier.ecommerce.model.carrinho;

import dev.trier.ecommerce.model.ProdutoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBITEMCARRINHO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrinhoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdItensCarrinho;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdCarrinho", nullable = false)
    private CarrinhoModel carrinho;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdProduto", nullable = false)
    private ProdutoModel produto;

    @Column(nullable = false)
    private Integer qtdItemCarrinho;


}
