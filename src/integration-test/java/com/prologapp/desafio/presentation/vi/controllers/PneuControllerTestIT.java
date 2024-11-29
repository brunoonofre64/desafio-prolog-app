package com.prologapp.desafio.presentation.vi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prologapp.desafio.DesafioApiPrologAppApplication;
import com.prologapp.desafio.application.dtos.PneuRequestDTO;
import com.prologapp.desafio.application.dtos.VincularPneuDTO;
import com.prologapp.desafio.domain.entities.PneuEntity;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaPneuRepository;
import com.prologapp.desafio.insfraestructure.persistence.jpa.JpaVeiculoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.prologapp.desafio.constantes.TestesIntegracaoConstantes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest(classes = DesafioApiPrologAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST-H2")
@AutoConfigureMockMvc
class PneuControllerTestIT {

    @Autowired
    private JpaPneuRepository pneuRepository;

    @Autowired
    private JpaVeiculoRepository jpaVeiculoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private VeiculoEntity veiculo;

    @BeforeEach
    void limparBancoDeDados() {
        pneuRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve registrar um pneu e retornar status 201")
    void deveRegistrarPneu() throws Exception {
        PneuRequestDTO pneuRequest = new PneuRequestDTO(NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU);

        mockMvc.perform(post(ENDPOINT_PNEUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pneuRequest)))
                .andExpect(status().isCreated())
                .andDo(print());

        List<PneuEntity> pneusSalvos = pneuRepository.findAll();
        assert !pneusSalvos.isEmpty();
        assert pneusSalvos.get(0).getNumeroFogo().equals(NUMERO_FOGO);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar registrar pneu com dados inválidos e retornar status 400")
    void deveRetornarErroAoRegistrarPneuInvalido() throws Exception {
        PneuRequestDTO pneuRequestInvalido =
                new PneuRequestDTO(null, MARCA_PNEU, PRESSAO, STATUS_PNEU);

        mockMvc.perform(post(ENDPOINT_PNEUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pneuRequestInvalido)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve vincular pneu ao veículo e retornar status 204")
    void deveVincularPneuVeiculo() throws Exception {
        veiculo = this.montaVeiculoEntity();
        jpaVeiculoRepository.save(veiculo);

        pneuRepository.save(new PneuEntity(null, NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU, null, POSICAO_PNEU, UUID));

        VincularPneuDTO vincularPneu = new VincularPneuDTO(PLACA_VALIDA, NUMERO_FOGO, POSICAO_PNEU);

        mockMvc.perform(put(ENDPOINT_PNEUS + "/vinculacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vincularPneu)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    @Test
    @DisplayName("Deve desvincular pneu do veículo e retornar status 204")
    void deveDesvincularPneuVeiculo() throws Exception {
        pneuRepository.save(new PneuEntity(null, NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU, veiculo, POSICAO_PNEU, UUID));

        mockMvc.perform(put(ENDPOINT_PNEUS + "/desvinculacao/" + NUMERO_FOGO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao registrar pneu já cadastrado")
    void deveLancarErroAoRegistrarPneuJaCadastrado() throws Exception {
        pneuRepository.save(new PneuEntity(null, NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU, null, POSICAO_PNEU, UUID));

        PneuRequestDTO pneuRequest = new PneuRequestDTO(NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU);

        mockMvc.perform(post(ENDPOINT_PNEUS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pneuRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar vincular pneu já vinculado a outro veículo")
    void deveLancarErroAoVincularPneuJaVinculado() throws Exception {
        veiculo = this.montaVeiculoEntity();
        jpaVeiculoRepository.save(veiculo);

        pneuRepository.save(new PneuEntity(null, NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU, veiculo, POSICAO_PNEU, UUID));

        VincularPneuDTO vincularPneu = new VincularPneuDTO(PLACA_VALIDA, NUMERO_FOGO, POSICAO_PNEU);

        mockMvc.perform(put(ENDPOINT_PNEUS + "/vinculacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vincularPneu)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar vincular pneu a posição já ocupada no veículo")
    void deveLancarErroAoVincularPneuEmPosicaoOcupada() throws Exception {
        veiculo = this.montaVeiculoEntity();
        jpaVeiculoRepository.save(veiculo);

        pneuRepository.save(new PneuEntity(null, NUMERO_FOGO, MARCA_PNEU, PRESSAO, STATUS_PNEU, veiculo, PosicaoPneu.A, UUID));

        VincularPneuDTO vincularPneu = new VincularPneuDTO(PLACA_VALIDA, NUMERO_FOGO, PosicaoPneu.A);

        mockMvc.perform(put(ENDPOINT_PNEUS + "/vinculacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vincularPneu)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar desvincular pneu inexistente")
    void deveLancarErroAoDesvincularPneuInexistente() throws Exception {
        mockMvc.perform(put(ENDPOINT_PNEUS + "/desvinculacao/" + NUMERO_FOGO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private VeiculoEntity montaVeiculoEntity() {
        return new VeiculoEntity(null, PLACA_VALIDA, MARCA,
                QUILOMETRAGEM, STATUS_VEICULO, List.of(), UUID);
    }
}
