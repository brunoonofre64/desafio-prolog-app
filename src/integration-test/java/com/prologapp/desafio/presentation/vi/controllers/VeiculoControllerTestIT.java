package com.prologapp.desafio.presentation.vi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prologapp.desafio.DesafioApiPrologAppApplication;
import com.prologapp.desafio.application.dtos.VeiculoRequestDTO;
import com.prologapp.desafio.domain.entities.VeiculoEntity;
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
class VeiculoControllerTestIT {


    @Autowired
    private JpaVeiculoRepository veiculoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBancoDeDados() {
        veiculoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve listar veículos com paginação e retornar status 200")
    void deveListarVeiculosComPaginacao() throws Exception {
        veiculoRepository.save(new VeiculoEntity(null, PLACA_VALIDA, MARCA, QUILOMETRAGEM, STATUS_VEICULO, List.of(), UUID));

        mockMvc.perform(get(ENDPOINT_VEICULOS)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].placa").value(PLACA_VALIDA))
                .andExpect(jsonPath("$.content[0].marca").value(MARCA))
                .andExpect(jsonPath("$.content[0].quilometragem").value(QUILOMETRAGEM))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve buscar veículo por placa e retornar status 200")
    void deveBuscarVeiculoPorPlaca() throws Exception {
        veiculoRepository.save(new VeiculoEntity(null, PLACA_VALIDA, MARCA, QUILOMETRAGEM, STATUS_VEICULO, List.of(), UUID));

        mockMvc.perform(get(ENDPOINT_VEICULOS + "/" + PLACA_VALIDA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value(PLACA_VALIDA))
                .andExpect(jsonPath("$.marca").value(MARCA))
                .andExpect(jsonPath("$.quilometragem").value(QUILOMETRAGEM))
                .andDo(print());
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar veículo com placa inexistente e retornar status 404")
    void deveRetornarErroParaPlacaInexistente() throws Exception {
        mockMvc.perform(get(ENDPOINT_VEICULOS + "/" + PLACA_INVALIDA)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve registrar um veículo e retornar status 201")
    void deveRegistrarVeiculo() throws Exception {
        VeiculoRequestDTO veiculoRequest = new VeiculoRequestDTO(PLACA_VALIDA, MARCA, QUILOMETRAGEM, STATUS_VEICULO);

        mockMvc.perform(post(ENDPOINT_VEICULOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoRequest)))
                .andExpect(status().isCreated())
                .andDo(print());

        List<VeiculoEntity> veiculosSalvos = veiculoRepository.findAll();
        assert !veiculosSalvos.isEmpty();
        assert veiculosSalvos.get(0).getPlaca().equals(PLACA_VALIDA);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar registrar veículo com dados inválidos e retornar status 400")
    void deveRetornarErroAoRegistrarVeiculoInvalido() throws Exception {
        VeiculoRequestDTO veiculoRequestInvalido = new VeiculoRequestDTO(null, MARCA, QUILOMETRAGEM, STATUS_VEICULO);

        mockMvc.perform(post(ENDPOINT_VEICULOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoRequestInvalido)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar veículo com placa fora do padrão")
    void deveLancarErroComPlacaForaDoPadrao() throws Exception {
        mockMvc.perform(get(ENDPOINT_VEICULOS + "/placa_invalida")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar veículo inexistente")
    void deveLancarErroAoBuscarVeiculoInexistente() throws Exception {
        mockMvc.perform(get(ENDPOINT_VEICULOS + "/ABC9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao registrar veículo com objeto nulo")
    void deveLancarErroComObjetoNulo() throws Exception {
        mockMvc.perform(post(ENDPOINT_VEICULOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("Deve lançar erro ao registrar veículo com placa já existente")
    void deveLancarErroComPlacaJaCadastrada() throws Exception {
        veiculoRepository.save(new VeiculoEntity(null, PLACA_VALIDA, MARCA, QUILOMETRAGEM, STATUS_VEICULO, List.of(), UUID));

        VeiculoRequestDTO veiculoRequest = new VeiculoRequestDTO(PLACA_VALIDA, MARCA, QUILOMETRAGEM, STATUS_VEICULO);

        mockMvc.perform(post(ENDPOINT_VEICULOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
