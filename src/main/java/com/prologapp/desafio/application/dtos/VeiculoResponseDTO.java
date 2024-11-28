package com.prologapp.desafio.application.dtos;

import com.prologapp.desafio.domain.enums.StatusVeiculo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoResponseDTO {
    private String placa;
    private String marca;
    private Integer quilometragem;
    private StatusVeiculo status;
    private String uuid;
    public List<PneuResponseDTO> pneus;

    public VeiculoResponseDTO(String placa, String marca, Integer quilometragem,
                              StatusVeiculo status, String uuid) {
        this.placa = placa;
        this.marca = marca;
        this.quilometragem = quilometragem;
        this.status = status;
        this.uuid = uuid;
    }
}
