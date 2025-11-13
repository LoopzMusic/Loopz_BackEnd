package dev.trier.ecommerce.model.acoesUsuario;

import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBFAVORITOS",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"cdUsuario", "cdProduto"})}
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdFavoritos;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdUsuario", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdProduto", nullable = false)
    private ProdutoModel produto;


    private Integer nuAvaliacao;


}
