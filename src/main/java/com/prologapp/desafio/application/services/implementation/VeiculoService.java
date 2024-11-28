package com.prologapp.desafio.application.services.implementation;

import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.application.dtos.PagedDataDTO;
import com.prologapp.desafio.application.dtos.VeiculoRequestDTO;
import com.prologapp.desafio.application.dtos.VeiculoResponseDTO;
import com.prologapp.desafio.application.exceptions.ApplicationException;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.application.exceptions.EntityNotFoundException;
import com.prologapp.desafio.application.exceptions.ObjectException;
import com.prologapp.desafio.application.mappers.VeiculoMapper;
import com.prologapp.desafio.application.services.IVeiculoService;
import com.prologapp.desafio.application.enums.CodigoErro;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.repositories.IVeiculoRepository;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaVeiculoRepository;
import lombok.AllArgsConstructor;
import org.flywaydb.core.internal.util.CollectionsUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VeiculoService implements IVeiculoService {

    private IVeiculoRepository veiculoRepository;
    private JpaVeiculoRepository jpaRepository;
    private VeiculoMapper mapper;

    @Override
    public PagedDataDTO<VeiculoResponseDTO> obterPneusPaginados(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new BusinessException(CodigoErro.PARAMETROS_PAGINACAO_INVALIDOS, page, size);
        }

        int offset = page * size;
        Long totalElementos = veiculoRepository.contaTotalRegistrosVeiculo();
        List<VeiculoEntity> veiculos = veiculoRepository.obterListaVeiculosSemPneus(offset, size);

        if (!CollectionsUtils.hasItems(veiculos)) {
            throw new EntityNotFoundException(CodigoErro.LISTA_VEICULOS_VAZIA);
        }

        List<VeiculoResponseDTO> veiculosResponse = veiculos
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return PagedDataDTO.of(veiculosResponse, totalElementos, page, size);
    }

    @Override
    public VeiculoResponseDTO obterVeiculoEspecifico(String placa) {
        if (placa != null && !placa.matches(ApplicationConstantes.REGEX_VALIDACAO_PLACA)) {
            throw new BusinessException(CodigoErro.PLACA_FORA_DO_PADRAO);
        }

        VeiculoEntity veiculo = jpaRepository.findVeiculoEntityByPlacaIgnoreCase(placa)
                .orElseThrow(() -> new EntityNotFoundException(CodigoErro.VEICULO_NAO_ENCONTRADO, placa));

        boolean comPneus = true;
        return mapper.toDTO(veiculo, comPneus);
    }

    @Override
    public void regisrarVeiculo(VeiculoRequestDTO veiculoRequest) {
        if (veiculoRequest == null) {
            throw new ObjectException(CodigoErro.OBJETO_NULO, VeiculoResponseDTO.class.getName());
        }

        String placa = veiculoRequest.getPlaca();
        if (jpaRepository.existsByPlacaIgnoreCase(placa)) {
            throw new BusinessException(CodigoErro.VEICULO_JA_CADASTRADO, placa);
        }

        try {
            VeiculoEntity veiculo = mapper.toEntity(veiculoRequest);
            jpaRepository.save(veiculo);
        } catch (Throwable ex) {
            throw new ApplicationException(CodigoErro.ERRO_AO_SALVAR,
                    VeiculoService.class.getName());
        }
    }
}
