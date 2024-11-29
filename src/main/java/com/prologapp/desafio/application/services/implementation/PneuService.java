package com.prologapp.desafio.application.services.implementation;

import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.VincularPneuDTO;
import com.prologapp.desafio.application.enums.CodigoErro;
import com.prologapp.desafio.application.exceptions.ApplicationException;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.application.exceptions.EntityNotFoundException;
import com.prologapp.desafio.application.exceptions.ObjectException;
import com.prologapp.desafio.application.mappers.PneuMapper;
import com.prologapp.desafio.application.services.IPneuService;
import com.prologapp.desafio.domain.entities.PneuEntity;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaPneuRepository;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaVeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PneuService implements IPneuService {

    private JpaPneuRepository jpaPneuRepository;
    private JpaVeiculoRepository jpaVeiculoRepository;
    private PneuMapper mapper;

    @Override
    public void registrarPneu(PneuRequestDTO pneuRequest) {
        if (pneuRequest == null) {
            throw new ObjectException(CodigoErro.OBJETO_NULO, PneuRequestDTO.class.getName());
        }

        Long numeroFogo = pneuRequest.getNumeroFogo();
        if (jpaPneuRepository.existsByNumeroFogo(numeroFogo)) {
            throw new BusinessException(CodigoErro.PNEU_JA_CADASTRADO, numeroFogo);
        }

        try {
            PneuEntity pneu = mapper.toEntity(pneuRequest);
            jpaPneuRepository.save(pneu);
        } catch (ApplicationException ex) {
            throw new ApplicationException(CodigoErro.ERRO_AO_SALVAR, PneuService.class.getName());
        }
    }

    @Override
    public void vincularPneuVeiculo(VincularPneuDTO vincularPneu) {
        if (vincularPneu == null) {
            throw new ObjectException(CodigoErro.OBJETO_NULO, PneuRequestDTO.class.getName());
        }

        Long numeroFogo = vincularPneu.getNumeroFogo();
        if (jpaPneuRepository.existsByNumeroFogoAndVeiculoNotNull(numeroFogo)) {
            throw new BusinessException(CodigoErro.PNEU_JA_VINCULADO, numeroFogo);
        }

        String placaVeiculo = vincularPneu.getPlacaVeiculo();
        VeiculoEntity veiculo = jpaVeiculoRepository.findVeiculoEntityByPlacaIgnoreCase(placaVeiculo)
                .orElseThrow(() -> new EntityNotFoundException(CodigoErro.VEICULO_NAO_ENCONTRADO, placaVeiculo));

        this.validaPosicaoDisponivel(vincularPneu, veiculo);

        PneuEntity pneu = jpaPneuRepository.findByNumeroFogo(numeroFogo)
                .orElseThrow(() -> new EntityNotFoundException(CodigoErro.PNEU_NAO_ENCONTRADO, numeroFogo));

        PosicaoPneu posicaoPneu = vincularPneu.getPosicao();
        try {
            PneuEntity pneuToSave = mapper.vinculoPneuToEntity(pneu, veiculo, posicaoPneu);
            jpaPneuRepository.save(pneuToSave);
        } catch (ApplicationException ex) {
            throw new ApplicationException(CodigoErro.ERRO_AO_SALVAR, PneuService.class.getName());
        }
    }

    @Override
    public void desvincularPneuVeiculo(Long numeroFogo) {
        if (numeroFogo == null) {
            throw new ObjectException(CodigoErro.OBJETO_NULO, PneuRequestDTO.class.getName());
        }

        PneuEntity pneu = jpaPneuRepository.findByNumeroFogo(numeroFogo)
                .orElseThrow(() -> new EntityNotFoundException(CodigoErro.PNEU_NAO_ENCONTRADO));

        pneu.setPosicao(null);
        pneu.setVeiculo(null);

        try {
            jpaPneuRepository.save(pneu);
        } catch (ApplicationException ex) {
            throw new ApplicationException(CodigoErro.ERRO_AO_SALVAR, PneuService.class.getName());
        }
    }

    private void validaPosicaoDisponivel(VincularPneuDTO vincularPneu, VeiculoEntity veiculo) {
        List<PneuEntity> listaPneusVeiculo = veiculo.getPneus();
        if (listaPneusVeiculo != null && !listaPneusVeiculo.isEmpty()) {
            boolean posicaoOcupada = listaPneusVeiculo.stream()
                    .anyMatch(pneu -> pneu.getPosicao() != null &&
                            pneu.getPosicao().equals(vincularPneu.getPosicao()));

            if (posicaoOcupada) {
                throw new BusinessException(CodigoErro.POSICAO_OCUPADA, vincularPneu.getPosicao());
            }
        }
    }
}
