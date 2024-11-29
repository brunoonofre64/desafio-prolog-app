package com.prologapp.desafio.application.service.implementation;

import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.VincularPneuDTO;
import com.prologapp.desafio.application.enums.CodigoErro;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.application.exceptions.EntityNotFoundException;
import com.prologapp.desafio.application.exceptions.ObjectException;
import com.prologapp.desafio.application.mappers.PneuMapper;
import com.prologapp.desafio.application.services.implementation.PneuService;
import com.prologapp.desafio.constantes.TesteConstantes;
import com.prologapp.desafio.domain.entities.PneuEntity;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaPneuRepository;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaVeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        PneuRequestDTO pneuRequest = new PneuRequestDTO(
                TesteConstantes.NUMERO_FOGO,
                TesteConstantes.MARCA_A,
                TesteConstantes.PRESSAO,
                TesteConstantes.STATUS_PNEU
        );
        PneuEntity pneuEntity = new PneuEntity();

        when(jpaPneuRepository.existsByNumeroFogo(TesteConstantes.NUMERO_FOGO)).thenReturn(false);
        when(mapper.toEntity(pneuRequest)).thenReturn(pneuEntity);

        assertDoesNotThrow(() -> pneuService.registrarPneu(pneuRequest));

        verify(jpaPneuRepository).save(pneuEntity);
    }

    @Test
    void registrarPneu_comDadosNulos_lancaExcecao() {
        ObjectException exception = assertThrows(
                ObjectException.class,
                () -> pneuService.registrarPneu(null)
        );
        assertEquals(CodigoErro.OBJETO_NULO, exception.getCodigoErro());
    }

    @Test
    void registrarPneu_comPneuJaCadastrado_lancaExcecao() {
        PneuRequestDTO pneuRequest = new PneuRequestDTO(
                TesteConstantes.NUMERO_FOGO,
                TesteConstantes.MARCA_A,
                TesteConstantes.PRESSAO,
                TesteConstantes.STATUS_PNEU
        );

        when(jpaPneuRepository.existsByNumeroFogo(TesteConstantes.NUMERO_FOGO)).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> pneuService.registrarPneu(pneuRequest)
        );
        assertEquals(CodigoErro.PNEU_JA_CADASTRADO, exception.getCodigoErro());
    }

    @Test
    void vincularPneuVeiculo_comDadosValidos_vinculaPneu() {
        VincularPneuDTO vincularPneu = new VincularPneuDTO(
                TesteConstantes.PLACA_VALIDA_A,
                TesteConstantes.NUMERO_FOGO,
                PosicaoPneu.A
        );
        VeiculoEntity veiculo = new VeiculoEntity();
        PneuEntity pneu = new PneuEntity();
        PneuEntity pneuVinculado = new PneuEntity();

        when(jpaPneuRepository.existsByNumeroFogoAndVeiculoNotNull(TesteConstantes.NUMERO_FOGO)).thenReturn(false);
        when(jpaVeiculoRepository.findVeiculoEntityByPlacaIgnoreCase(TesteConstantes.PLACA_VALIDA_A)).thenReturn(Optional.of(veiculo));
        when(jpaPneuRepository.findByNumeroFogo(TesteConstantes.NUMERO_FOGO)).thenReturn(Optional.of(pneu));
        when(mapper.vinculoPneuToEntity(pneu, veiculo, PosicaoPneu.A)).thenReturn(pneuVinculado);

        assertDoesNotThrow(() -> pneuService.vincularPneuVeiculo(vincularPneu));

        verify(jpaPneuRepository).save(pneuVinculado);
    }

    @Test
    void vincularPneuVeiculo_comDadosNulos_lancaExcecao() {
        ObjectException exception = assertThrows(
                ObjectException.class,
                () -> pneuService.vincularPneuVeiculo(null)
        );
        assertEquals(CodigoErro.OBJETO_NULO, exception.getCodigoErro());
    }

    @Test
    void vincularPneuVeiculo_comPneuJaVinculado_lancaExcecao() {
        VincularPneuDTO vincularPneu = new VincularPneuDTO(
                TesteConstantes.PLACA_VALIDA_A,
                TesteConstantes.NUMERO_FOGO,
                PosicaoPneu.A
        );

        when(jpaPneuRepository.existsByNumeroFogoAndVeiculoNotNull(TesteConstantes.NUMERO_FOGO)).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> pneuService.vincularPneuVeiculo(vincularPneu)
        );
        assertEquals(CodigoErro.PNEU_JA_VINCULADO, exception.getCodigoErro());
    }

    @Test
    void vincularPneuVeiculo_comPosicaoOcupada_lancaExcecao() {
        VincularPneuDTO vincularPneu = new VincularPneuDTO(
                TesteConstantes.PLACA_VALIDA_A,
                TesteConstantes.NUMERO_FOGO,
                PosicaoPneu.A
        );
        VeiculoEntity veiculo = new VeiculoEntity();
        PneuEntity pneuExistente = new PneuEntity();
        pneuExistente.setPosicao(PosicaoPneu.A);

        veiculo.setPneus(List.of(pneuExistente));

        when(jpaVeiculoRepository.findVeiculoEntityByPlacaIgnoreCase(TesteConstantes.PLACA_VALIDA_A))
                .thenReturn(Optional.of(veiculo));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> pneuService.vincularPneuVeiculo(vincularPneu)
        );
        assertEquals(CodigoErro.POSICAO_OCUPADA, exception.getCodigoErro());
    }

    @Test
    void desvincularPneuVeiculo_comDadosValidos_desvinculaPneu() {
        PneuEntity pneu = new PneuEntity();
        when(jpaPneuRepository.findByNumeroFogo(TesteConstantes.NUMERO_FOGO)).thenReturn(Optional.of(pneu));

        assertDoesNotThrow(() -> pneuService.desvincularPneuVeiculo(TesteConstantes.NUMERO_FOGO));

        verify(jpaPneuRepository).save(pneu);
    }

    @Test
    void desvincularPneuVeiculo_comDadosNulos_lancaExcecao() {
        ObjectException exception = assertThrows(
                ObjectException.class,
                () -> pneuService.desvincularPneuVeiculo(null)
        );
        assertEquals(CodigoErro.OBJETO_NULO, exception.getCodigoErro());
    }

    @Test
    void desvincularPneuVeiculo_comPneuNaoExistente_lancaExcecao() {
        when(jpaPneuRepository.findByNumeroFogo(TesteConstantes.NUMERO_FOGO)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> pneuService.desvincularPneuVeiculo(TesteConstantes.NUMERO_FOGO)
        );
        assertEquals(CodigoErro.PNEU_NAO_ENCONTRADO, exception.getCodigoErro());
    }
}
