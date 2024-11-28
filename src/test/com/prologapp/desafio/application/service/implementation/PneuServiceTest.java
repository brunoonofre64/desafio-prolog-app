package com.prologapp.desafio.application.service.implementation;

import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.VincularPneuDTO;
import com.prologapp.desafio.application.enums.CodigoErro;
import com.prologapp.desafio.application.exceptions.ApplicationException;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.application.exceptions.EntityNotFoundException;
import com.prologapp.desafio.application.exceptions.ObjectException;
import com.prologapp.desafio.application.mappers.PneuMapper;
import com.prologapp.desafio.application.services.implementation.PneuService;
import com.prologapp.desafio.domain.entities.PneuEntity;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaPneuRepository;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaVeiculoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.prologapp.desafio.constantes.TesteConstantes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PneuServiceTest {

    @Mock
    private JpaPneuRepository jpaPneuRepository;

    @Mock
    private JpaVeiculoRepository jpaVeiculoRepository;

    @Mock
    private PneuMapper mapper;

    @InjectMocks
    private PneuService pneuService;

    @Test
    void registrarPneu_comDadosValidos_salvaPneu() {
        PneuRequestDTO pneuRequest = new PneuRequestDTO(NUMERO_FOGO, MARCA_A, PRESSAO, STATUS_PNEU);
        PneuEntity pneuEntity = new PneuEntity();

        when(jpaPneuRepository.existsByNumeroFogo(NUMERO_FOGO)).thenReturn(false);
        when(mapper.toEntity(pneuRequest)).thenReturn(pneuEntity);

        Assertions.assertDoesNotThrow(() -> pneuService.registrarPneu(pneuRequest));
        Mockito.verify(jpaPneuRepository).save(pneuEntity);
    }

    @Test
    void registrarPneu_comDadosNulos_lancaExcecao() {
        Assertions.assertThrows(ObjectException.class, () -> pneuService.registrarPneu(null));
    }

    @Test
    void registrarPneu_comPneuJaCadastrado_lancaExcecao() {
        PneuRequestDTO pneuRequest = new PneuRequestDTO(NUMERO_FOGO, MARCA_A, PRESSAO, STATUS_PNEU);
        when(jpaPneuRepository.existsByNumeroFogo(NUMERO_FOGO)).thenReturn(true);

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> pneuService.registrarPneu(pneuRequest));
        Assertions.assertEquals(CodigoErro.PNEU_JA_CADASTRADO, exception.getCodigoErro());
    }

    @Test
    void registrarPneu_erroAoSalvar_lancaExcecao() {
        PneuRequestDTO pneuRequest = new PneuRequestDTO(NUMERO_FOGO, MARCA_A, PRESSAO, STATUS_PNEU);
        when(jpaPneuRepository.existsByNumeroFogo(NUMERO_FOGO)).thenReturn(false);
        when(mapper.toEntity(any())).thenThrow(new ApplicationException(CodigoErro.ERRO_AO_SALVAR));

        Assertions.assertThrows(ApplicationException.class, () -> pneuService.registrarPneu(pneuRequest));
    }

    @Test
    void vincularPneuVeiculo_comDadosValidos_vinculaPneu() {
        VincularPneuDTO vincularPneu = new VincularPneuDTO(PLACA_VALIDA_A, NUMERO_FOGO, POSICAO);
        VeiculoEntity veiculo = new VeiculoEntity();
        PneuEntity pneu = new PneuEntity();
        PneuEntity pneuVinculado = new PneuEntity();

        when(jpaPneuRepository.existsByNumeroFogoAndVeiculoNotNull(NUMERO_FOGO)).thenReturn(false);
        when(jpaVeiculoRepository.findVeiculoEntityByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(Optional.of(veiculo));
        when(jpaPneuRepository.findByNumeroFogo(NUMERO_FOGO)).thenReturn(Optional.of(pneu));
        when(mapper.vinculoPneuToEntity(pneu, veiculo, POSICAO)).thenReturn(pneuVinculado);

        Assertions.assertDoesNotThrow(() -> pneuService.vincularPneuVeiculo(vincularPneu));
        Mockito.verify(jpaPneuRepository).save(pneuVinculado);
    }

    @Test
    void vincularPneuVeiculo_comDadosNulos_lancaExcecao() {
        Assertions.assertThrows(ObjectException.class, () -> pneuService.vincularPneuVeiculo(null));
    }

    @Test
    void vincularPneuVeiculo_comPneuJaVinculado_lancaExcecao() {
        VincularPneuDTO vincularPneu = new VincularPneuDTO(PLACA_VALIDA_A, NUMERO_FOGO, POSICAO);
        when(jpaPneuRepository.existsByNumeroFogoAndVeiculoNotNull(NUMERO_FOGO)).thenReturn(true);

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> pneuService.vincularPneuVeiculo(vincularPneu));
        Assertions.assertEquals(CodigoErro.PNEU_JA_VINCULADO, exception.getCodigoErro());
    }

    @Test
    void vincularPneuVeiculo_comVeiculoNaoExistente_lancaExcecao() {
        VincularPneuDTO vincularPneu = new VincularPneuDTO(PLACA_VALIDA_A, NUMERO_FOGO, POSICAO);
        when(jpaVeiculoRepository.findVeiculoEntityByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> pneuService.vincularPneuVeiculo(vincularPneu));
        Assertions.assertEquals(CodigoErro.VEICULO_NAO_ENCONTRADO, exception.getCodigoErro());
    }

    @Test
    void desvincularPneuVeiculo_comDadosValidos_desvinculaPneu() {
        PneuEntity pneu = new PneuEntity();
        when(jpaPneuRepository.findByNumeroFogo(NUMERO_FOGO)).thenReturn(Optional.of(pneu));

        Assertions.assertDoesNotThrow(() -> pneuService.desvincularPneuVeiculo(NUMERO_FOGO));
        Mockito.verify(jpaPneuRepository).save(pneu);
    }

    @Test
    void desvincularPneuVeiculo_comDadosNulos_lancaExcecao() {
        Assertions.assertThrows(ObjectException.class, () -> pneuService.desvincularPneuVeiculo(null));
    }

    @Test
    void desvincularPneuVeiculo_comPneuNaoExistente_lancaExcecao() {
        when(jpaPneuRepository.findByNumeroFogo(NUMERO_FOGO)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> pneuService.desvincularPneuVeiculo(NUMERO_FOGO));
        Assertions.assertEquals(CodigoErro.PNEU_NAO_ENCONTRADO, exception.getCodigoErro());
    }
}
