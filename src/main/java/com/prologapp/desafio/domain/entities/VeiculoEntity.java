package com.prologapp.desafio.domain.entities;

import com.prologapp.desafio.application.utils.StatusVeiculoConverter;
import com.prologapp.desafio.domain.enums.StatusVeiculo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "veiculo")
public class VeiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false)
    private Integer quilometragem;

    @Column(nullable = false)
    @Convert(converter = StatusVeiculoConverter.class)
    private StatusVeiculo status;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PneuEntity> pneus;

    @Column(nullable = false, length = 36)
    private String uuid;

    @PrePersist
    private void prePersis() {
        this.uuid = UUID.randomUUID().toString();
    }
}
