package dev.trier.ecommerce.model.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "vw_produtos_mais_vendidos")
public class ProdutoMaisVendidoModel {

    @Id
    @Column(name = "cd_produto")
    private Integer cdProduto;

    @Column(name = "total_vendido")
    private Integer totalVendido;
}
