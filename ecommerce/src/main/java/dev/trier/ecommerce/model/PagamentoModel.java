package dev.trier.ecommerce.model;

import dev.trier.ecommerce.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBPAGAMENTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cdPagamento;

    @OneToOne
    @JoinColumn(name = "cdPedido", nullable = false, unique = true)
    private PedidoModel pedido;

    @Column(nullable = false)
    private String preferenceId;

    @Column
    private String paymentId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column
    private String initPoint;

    @Column
    private String sandboxInitPoint;

    @Column(length = 100)
    private String metodoPagamento;

    @Column(length = 50)
    private String statusDetalhe;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedDate
    @Column
    private LocalDateTime atualizadoEm;

    @Column
    private LocalDateTime pagamentoAprovadoEm;
}
