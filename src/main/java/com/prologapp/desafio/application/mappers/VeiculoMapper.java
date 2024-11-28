package com.prologapp.desafio.application.mappers;

import com.prologapp.desafio.application.dtos.PneuResponseDTO;
import com.prologapp.desafio.application.dtos.VeiculoRequestDTO;
import com.prologapp.desafio.application.dtos.VeiculoResponseDTO;
import com.prologapp.desafio.domain.entities.PneuEntity;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VeiculoMapper {
    public VeiculoEntity toEntity(VeiculoRequestDTO dto) {
        return VeiculoEntity
                .builder()
                .placa(dto.getPlaca().toUpperCase())
                .marca(dto.getMarca().toUpperCase())
                .quilometragem(dto.getQuilometragem())
                .status(dto.getStatus())
                .build();
    }

    public VeiculoResponseDTO toDTO(VeiculoEntity entity) {
        return toDTO(entity, false);
    }

    public VeiculoResponseDTO toDTO(VeiculoEntity entity, boolean comPneus) {
        List<PneuResponseDTO> pneus = new ArrayList<>();
        if (entity.getPneus() != null && !entity.getPneus().isEmpty() && comPneus) {
            pneus = entity.getPneus()
                    .stream()
                    .map(VeiculoMapper::toPneuDTO)
                    .collect(Collectors.toList());
        }

        return new VeiculoResponseDTO(entity.getPlaca(), entity.getMarca(),
                entity.getQuilometragem(), entity.getStatus(), entity.getUuid(), pneus);
    }

    private static PneuResponseDTO toPneuDTO(PneuEntity entity) {
        return new PneuResponseDTO(entity.getNumeroFogo(), entity.getMarca(),
                entity.getPressaoAtual(), entity.getStatus(), entity.getPosicao());
    }
}
