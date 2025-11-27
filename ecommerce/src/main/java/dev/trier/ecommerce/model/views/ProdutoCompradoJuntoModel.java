package dev.trier.ecommerce.model.views;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
@IdClass(ProdutoCompradoJuntoId.class)
@Getter
@Entity
@Immutable
@Table(name = "vw_produtos_comprados_juntos")
public class ProdutoCompradoJuntoModel {

    @Id
    @Column(name = "produto_origem")
    private Integer produtoOrigem;

    @Column(name = "produto_recomendado")
    private Integer produtoRecomendado;

    @Column(name = "vezes_comprados_juntos")
    private Integer vezesCompradosJuntos;
}
