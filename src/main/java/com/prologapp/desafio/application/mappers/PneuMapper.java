package com.prologapp.desafio.application.mappers;

import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.PneuResponseDTO;
import com.prologapp.desafio.application.dtos.VeiculoResponseDTO;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.domain.entities.PneuEntity;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import org.springframework.stereotype.Component;

@Component
public class PneuMapper {

    public PneuEntity vinculoPneuToEntity(PneuEntity pneu, VeiculoEntity veiculo, PosicaoPneu posicaoPneu) {
        return PneuEntity.builder()
                .id(pneu.getId())
                .numeroFogo(pneu.getNumeroFogo())
                .marca(pneu.getMarca())
                .pressaoAtual(pneu.getPressaoAtual())
                .status(pneu.getStatus())
                .posicao(posicaoPneu)
                .veiculo(veiculo)
                .uuid(pneu.getUuid())
                .build();
    }

    public PneuEntity toEntity(PneuRequestDTO dto) {
        return PneuEntity
                .builder()
                .numeroFogo(dto.getNumeroFogo())
                .marca(dto.getMarca().toUpperCase())
                .status(dto.getStatus())
                .pressaoAtual(dto.getPressaoAtual())
                .build();
    }

    public PneuResponseDTO toDTO(PneuEntity entity) {
        return toDTO(entity, false);
    }

    public PneuResponseDTO toDTO(PneuEntity entity, boolean comVeiculo) {
        VeiculoResponseDTO veiculoResponse = null;
        if (entity.getVeiculo() != null && comVeiculo) {
            veiculoResponse = this.toVeiculoDTO(entity.getVeiculo());
        }

        return new PneuResponseDTO(entity.getNumeroFogo(), entity.getMarca(),
                entity.getPressaoAtual(), entity.getStatus(), entity.getPosicao(), veiculoResponse);
    }

    private VeiculoResponseDTO toVeiculoDTO(VeiculoEntity entity) {
        return new VeiculoResponseDTO(entity.getPlaca(), entity.getMarca(),
                entity.getQuilometragem(), entity.getStatus(), entity.getUuid());
    }
}
