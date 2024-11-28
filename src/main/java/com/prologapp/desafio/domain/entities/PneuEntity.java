package com.prologapp.desafio.domain.entities;

import com.prologapp.desafio.application.utils.StatusPneuConverter;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.domain.enums.StatusPneu;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pneu")
public class PneuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_fogo", nullable = false, unique = true, length = 20)
    private Long numeroFogo;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(name = "pressao_atual", nullable = false, precision = 7, scale = 2)
    private BigDecimal pressaoAtual;

    @Column(nullable = false)
    @Convert(converter = StatusPneuConverter.class)
    private StatusPneu status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private VeiculoEntity veiculo;

    @Column
    @Enumerated(EnumType.STRING)
    private PosicaoPneu posicao;

    @Column(nullable = false, length = 36)
    private String uuid;

    @PrePersist
    private void prePersis() {
        this.uuid = UUID.randomUUID().toString();
    }
}
