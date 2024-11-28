package com.prologapp.desafio.domain.repositories;

import com.prologapp.desafio.domain.entities.VeiculoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVeiculoRepository {
    List<VeiculoEntity> obterListaVeiculosSemPneus(int offset, int size);
    Long contaTotalRegistrosVeiculo();
}
