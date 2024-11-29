package com.prologapp.desafio.application.constantes;

/**
 * Utilizado o padrao UNICODE para substituir simbolos e letras com pontuação!!
 *
 * @author Bruno Onofre
 */
public class ApplicationConstantes {
    public static final String PLACA_OBRIGATORIA = "A placa do ve\u00EDculo \u00E9 obrigat\u00F3ria.";
    public static final String VALOR_NAO_CORRESPONDE_OPCOES_VALIDAS = "O valor n\u00E3o corresponde a nenhuma op\u00E7\u00E3o v\u00E1lida";
    public static final String QUILIMETRAGEM_OBRIGATORIA = "A quilometragem \u00E9 obrigat\u00F3ria.";
    public static final String QUILIMETRAGEM_NAO_NEGATIVA= "A quilometragem n\u00E3o pode ser negativa.";
    public static final String MARCA_OBRIGATORIA = "O campo marca \u00E9 obrigat\u00F3rio.";
    public static final String REGEX_VALIDACAO_PLACA = "^[A-Z]{3}\\d{4}$|^[A-Z]{3}\\d{1}[A-Z]{1}\\d{2}$";
    public static final String PADRAO_OBRIGATORIO_PLACA = "A placa deve estar no formato padr\u00E3o (ex.: ABC1234 ou ABC1D23).";
    public static final String STATUS_OBRIGATORIO = "O campo status \u00E9 obrigat\u00F3rio.";
    public static final String OPCOES_STATUS = "O status deve ser 'ATIVO', 'INATIVO', 'MANUTENCAO' ou 'SUCATEADO'";
    public static final String NUMERO_FOGO_OBRIGATORIO = "O n\u00FAmero do fogo \u00E9 obrigat\u00F3rio.";
    public static final String PRESSAO_OBRIGATORIA = "A press\u00E3o atual \u00E9 obrigat\u00F3ria.";
    public static final String POSICAO_PNEU_OBRIGATORIA = "A posi\u00E7\u00E3o do pneu \u00E9 obrigat\u00F3ria.";
    public static final String OPCOES_STATUS_PNEU = "O status deve ser uma das op\u00E7\u00F5es v\u00E1lidas - USADO, ESTOQUE, REFORMA, DESCARTADO";
    public static final String POSICOES_ACEITAS_PNEU = "As posi\u00E7\u00F5es que o pneu aceita s\u00E3o = A, B, C, D, E, F, G, H.";
    public static final String MAXIMO_CARACTERES_MARCA = "A marca do pneu deve ter no m\u00E1ximo 50 caracteres.";
    public static final String PRESSAO_FORA_DE_PADRAO = "A press\u00E3o deve ser no m\u00EDnima 0.0 ou no m\u00E1ximo 5 inteiros 2 n\u00FAmeros quebrados ex; 12345.12";
    public static final String PRESSAO_DEVE_SER_MAIOR_QUE_ZERO = "A press\u00E3o deve ser maior que 0.0";
    public static final String VALOR_POSICAO_PNEU_INVALIDA = "Valor inv\u00E1lido para PosicaoPneu:";
}
