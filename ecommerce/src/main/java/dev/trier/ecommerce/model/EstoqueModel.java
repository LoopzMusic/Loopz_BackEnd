package dev.trier.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "TBESTOQUE")
public class EstoqueModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdEstoque;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cdProduto")
    private ProdutoModel produto;

    private Integer qtdEstoqueProduto;

    private String flAtivo = "S";
}
