package dev.trier.ecommerce.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "TBEMPRESA",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nuCPNJ"}),
                @UniqueConstraint(columnNames = {"nmRazao"}),
                @UniqueConstraint(columnNames = {"nuTelefone"})
        })
public class EmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdEmpresa;

    @Column(nullable = false)
    private String nmFantasia;

    @Column(nullable = false)
    private String nmRazao;

    @Column(nullable = false)
    private String nuCNPJ;

    @Column(nullable = false)
    private String nuTelefone;

    @Column(nullable = false)
    private String dsCidade;

    @Column(nullable = false)
    private String dsEstado;

    @Column(nullable = false)
    private String dsEndereco;

    @Column(nullable = false)
    private String nuEndereco;

    private String flAtivo = "S";

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoModel> produtos;
}
