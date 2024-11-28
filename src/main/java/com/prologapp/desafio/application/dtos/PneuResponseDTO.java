package com.prologapp.desafio.application.dtos;

import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.domain.enums.StatusPneu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PneuResponseDTO {
    private Long numeroFogo;
    private String marca;
    private BigDecimal pressaoAtual;
    private StatusPneu status;
    private PosicaoPneu posicao;
    private VeiculoResponseDTO veiculo;

    public PneuResponseDTO(Long numeroFogo, String marca, BigDecimal pressaoAtual,
                           StatusPneu status, PosicaoPneu posicao) {
        this.numeroFogo = numeroFogo;
        this.marca = marca;
        this.pressaoAtual = pressaoAtual;
        this.status = status;
        this.posicao = posicao;
    }
}
