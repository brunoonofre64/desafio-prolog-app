package com.prologapp.desafio.constantes;

import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.domain.enums.StatusPneu;

import java.math.BigDecimal;

public class TesteConstantes {
    public static final String PLACA_VALIDA_A = "ABC1234";
    public static final String PLACA_VALIDA_B = "XYZ5678";
    public static final String PLACA_INVALIDA = "INVALIDO";
    public static final String MARCA_A = "Marca A";
    public static final String MARCA_B = "Marca B";
    public static final String UUID_A = "uuid-A";
    public static final String UUID_B = "uuid-B";
    public static final long TOTAL_REGISTROS = 5L;
    public static final int PAGE = 0;
    public static final int SIZE = 2;
    public static final int OFFSET = PAGE * SIZE;
    public static final Long NUMERO_FOGO = 12345L;
    public static final BigDecimal PRESSAO = new BigDecimal(12.3);
    public static final StatusPneu STATUS_PNEU = StatusPneu.USADO;
    public static final PosicaoPneu POSICAO = PosicaoPneu.A;
}
