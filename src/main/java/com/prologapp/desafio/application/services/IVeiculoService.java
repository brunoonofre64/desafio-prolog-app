package com.prologapp.desafio.application.services;

import com.prologapp.desafio.application.dtos.PagedDataDTO;
import com.prologapp.desafio.application.dtos.VeiculoRequestDTO;
import com.prologapp.desafio.application.dtos.VeiculoResponseDTO;

public interface IVeiculoService {
    PagedDataDTO<VeiculoResponseDTO> obterPneusPaginados(int page, int size);
    VeiculoResponseDTO obterVeiculoEspecifico(String placa);
    void regisrarVeiculo(VeiculoRequestDTO request);
}
