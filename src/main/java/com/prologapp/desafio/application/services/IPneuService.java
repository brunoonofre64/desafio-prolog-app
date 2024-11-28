package com.prologapp.desafio.application.services;

import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.VincularPneuDTO;
import org.springframework.stereotype.Service;

@Service
public interface IPneuService {
    void registrarPneu(PneuRequestDTO pneuRequest);
    void vincularPneuVeiculo(VincularPneuDTO vincularPneu);
    void desvincularPneuVeiculo(Long numeroFogo);
}
