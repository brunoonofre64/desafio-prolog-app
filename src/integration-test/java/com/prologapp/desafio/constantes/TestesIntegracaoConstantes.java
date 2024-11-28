package com.prologapp.desafio.constantes;

import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.domain.enums.StatusPneu;
import com.prologapp.desafio.domain.enums.StatusVeiculo;

import java.math.BigDecimal;

public class TestesIntegracaoConstantes {
    public static final String ENDPOINT_VEICULOS = "/api/v1/veiculos";
    public static final String ENDPOINT_PNEUS = "/api/v1/pneus";
    public static final String PLACA_VALIDA = "ABC1234";
    public static final String PLACA_INVALIDA = "ZZZ0000";
    public static final String MARCA = "Marca A";
    public static final String MARCA_PNEU = "Marca Pneu A";
    public static final Long NUMERO_FOGO = 123456L;
    public static final int QUILOMETRAGEM = 10000;
    public static final StatusVeiculo STATUS_VEICULO = StatusVeiculo.ATIVO;
    public static final StatusPneu STATUS_PNEU = StatusPneu.USADO;
    public static final String UUID = "uuid-1";
    public static final BigDecimal PRESSAO = new BigDecimal(12);
    public static final PosicaoPneu POSICAO_PNEU = PosicaoPneu.A;
}
