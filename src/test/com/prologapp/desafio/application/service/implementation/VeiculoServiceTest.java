package com.prologapp.desafio.application.service.implementation;

import com.prologapp.desafio.application.dtos.PagedDataDTO;
import com.prologapp.desafio.application.dtos.VeiculoRequestDTO;
import com.prologapp.desafio.application.dtos.VeiculoResponseDTO;
import com.prologapp.desafio.application.enums.CodigoErro;
import com.prologapp.desafio.application.exceptions.BusinessException;
import com.prologapp.desafio.application.exceptions.EntityNotFoundException;
import com.prologapp.desafio.application.exceptions.ObjectException;
import com.prologapp.desafio.application.mappers.VeiculoMapper;
import com.prologapp.desafio.application.services.implementation.VeiculoService;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.enums.StatusVeiculo;
import com.prologapp.desafio.domain.repositories.IVeiculoRepository;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaVeiculoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.prologapp.desafio.constantes.TesteConstantes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class VeiculoServiceTest {

    @Mock
    private IVeiculoRepository veiculoRepository;

    @Mock
    private JpaVeiculoRepository jpaRepository;

    @Mock
    private VeiculoMapper mapper;

    @InjectMocks
    private VeiculoService veiculoService;

    @Test
    void obterPneusPaginados_comParametrosValidos_retornaPaginacao() {
        List<VeiculoEntity> veiculos = List.of(
                new VeiculoEntity(1L, PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO, List.of(), UUID_A),
                new VeiculoEntity(2L, PLACA_VALIDA_B, MARCA_B, 15000, StatusVeiculo.INATIVO, List.of(), UUID_B)
        );
        List<VeiculoResponseDTO> veiculosResponse = List.of(
                new VeiculoResponseDTO(PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO, UUID_A),
                new VeiculoResponseDTO(PLACA_VALIDA_B, MARCA_B, 15000, StatusVeiculo.INATIVO, UUID_B)
        );

        when(veiculoRepository.contaTotalRegistrosVeiculo()).thenReturn(TOTAL_REGISTROS);
        when(veiculoRepository.obterListaVeiculosSemPneus(OFFSET, SIZE)).thenReturn(veiculos);
        when(mapper.toDTO(any(VeiculoEntity.class))).thenReturn(veiculosResponse.get(0), veiculosResponse.get(1));

        PagedDataDTO<VeiculoResponseDTO> result = veiculoService.obterPneusPaginados(PAGE, SIZE);

        Assertions.assertEquals(TOTAL_REGISTROS, result.getTotalElements());
        Assertions.assertEquals(SIZE, result.getContent().size());
        Assertions.assertEquals(PLACA_VALIDA_A, result.getContent().get(0).getPlaca());
        Mockito.verify(veiculoRepository).contaTotalRegistrosVeiculo();
        Mockito.verify(veiculoRepository).obterListaVeiculosSemPneus(OFFSET, SIZE);
    }

    @Test
    void obterPneusPaginados_comListaVazia_lancaExcecao() {
        when(veiculoRepository.obterListaVeiculosSemPneus(OFFSET, SIZE)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> veiculoService.obterPneusPaginados(PAGE, SIZE)
        );
        Assertions.assertEquals(CodigoErro.LISTA_VEICULOS_VAZIA, exception.getCodigoErro());
    }

    @Test
    void obterPneusPaginados_comParametrosInvalidos_lancaExcecao() {
        int invalidPage = -1;
        int invalidSize = 0;

        BusinessException exception = Assertions.assertThrows(
                BusinessException.class,
                () -> veiculoService.obterPneusPaginados(invalidPage, invalidSize)
        );
        Assertions.assertEquals(CodigoErro.PARAMETROS_PAGINACAO_INVALIDOS, exception.getCodigoErro());
    }

    @Test
    void obterVeiculoEspecifico_comPlacaValida_retornaVeiculo() {
        VeiculoEntity veiculo = new VeiculoEntity(1L, PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO, List.of(), "uuid-1");
        VeiculoResponseDTO veiculoResponse = new VeiculoResponseDTO(PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO, "uuid-1");

        when(jpaRepository.findVeiculoEntityByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(Optional.of(veiculo));
        when(mapper.toDTO(veiculo, true)).thenReturn(veiculoResponse);

        VeiculoResponseDTO result = veiculoService.obterVeiculoEspecifico(PLACA_VALIDA_A);

        Assertions.assertEquals(PLACA_VALIDA_A, result.getPlaca());
        Mockito.verify(jpaRepository).findVeiculoEntityByPlacaIgnoreCase(PLACA_VALIDA_A);
        Mockito.verify(mapper).toDTO(veiculo, true);
    }

    @Test
    void obterVeiculoEspecifico_comPlacaNaoExistente_lancaExcecao() {
        when(jpaRepository.findVeiculoEntityByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> veiculoService.obterVeiculoEspecifico(PLACA_VALIDA_A)
        );
        Assertions.assertEquals(CodigoErro.VEICULO_NAO_ENCONTRADO, exception.getCodigoErro());
    }

    @Test
    void obterVeiculoEspecifico_comPlacaInvalida_lancaExcecao() {
        BusinessException exception = Assertions.assertThrows(
                BusinessException.class,
                () -> veiculoService.obterVeiculoEspecifico(PLACA_INVALIDA)
        );
        Assertions.assertEquals(CodigoErro.PLACA_FORA_DO_PADRAO, exception.getCodigoErro());
    }

    @Test
    void regisrarVeiculo_comDadosValidos_salvaVeiculo() {
        VeiculoRequestDTO veiculoRequest = new VeiculoRequestDTO(PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO);
        VeiculoEntity veiculoEntity = new VeiculoEntity(1L, PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO, List.of(), "uuid-1");

        when(jpaRepository.existsByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(false);
        when(mapper.toEntity(veiculoRequest)).thenReturn(veiculoEntity);
        when(jpaRepository.save(veiculoEntity)).thenReturn(veiculoEntity);

        Assertions.assertDoesNotThrow(() -> veiculoService.regisrarVeiculo(veiculoRequest));

        Mockito.verify(jpaRepository).existsByPlacaIgnoreCase(PLACA_VALIDA_A);
        Mockito.verify(mapper).toEntity(veiculoRequest);
        Mockito.verify(jpaRepository).save(veiculoEntity);
    }

    @Test
    void regisrarVeiculo_comDadosNulos_lancaExcecao() {
        ObjectException exception = Assertions.assertThrows(
                ObjectException.class,
                () -> veiculoService.regisrarVeiculo(null)
        );
        Assertions.assertEquals(ObjectException.class, exception.getClass());
    }

    @Test
    void regisrarVeiculo_comVeiculoJaCadastrado_lancaExcecao() {
        VeiculoRequestDTO veiculoRequest = new VeiculoRequestDTO(PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO);

        when(jpaRepository.existsByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(true);

        BusinessException exception = Assertions.assertThrows(
                BusinessException.class,
                () -> veiculoService.regisrarVeiculo(veiculoRequest)
        );
        Assertions.assertEquals(CodigoErro.VEICULO_JA_CADASTRADO, exception.getCodigoErro());
        Mockito.verify(jpaRepository).existsByPlacaIgnoreCase(PLACA_VALIDA_A);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void regisrarVeiculo_erroAoSalvar_lancaExcecao() {
        VeiculoRequestDTO veiculoRequest = new VeiculoRequestDTO(PLACA_VALIDA_A, MARCA_A, 10000, StatusVeiculo.ATIVO);
        when(jpaRepository.existsByPlacaIgnoreCase(PLACA_VALIDA_A)).thenReturn(false);
        when(mapper.toEntity(any())).thenThrow(new RuntimeException("Erro ao salvar"));

        Assertions.assertThrows(
                RuntimeException.class,
                () -> veiculoService.regisrarVeiculo(veiculoRequest)
        );
    }
}
