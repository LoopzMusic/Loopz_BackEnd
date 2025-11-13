package dev.trier.ecommerce.model.acoesUsuario;

import dev.trier.ecommerce.model.ProdutoModel;
import dev.trier.ecommerce.model.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBFEEDBACK")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdFeedBack;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdUsuario", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cdProduto", nullable = false)
    private ProdutoModel produto;

    @Column(nullable = false)
    private Integer nuAvaliacao;

    @Column(length = 2000)
    private String dsComentario;

}
