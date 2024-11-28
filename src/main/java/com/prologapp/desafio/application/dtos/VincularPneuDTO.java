package com.prologapp.desafio.application.dtos;

import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.application.anotations.EnumValidation;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import com.prologapp.desafio.presentation.v1.constantes.PresentationConstantes;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VincularPneuDTO {
    @NotBlank(message = ApplicationConstantes.PLACA_OBRIGATORIA)
    @Pattern(regexp = ApplicationConstantes.REGEX_VALIDACAO_PLACA, message = ApplicationConstantes.PADRAO_OBRIGATORIO_PLACA)
    private String placaVeiculo;

    @NotNull(message = ApplicationConstantes.NUMERO_FOGO_OBRIGATORIO)
    @Min(value = 1, message = PresentationConstantes.NUMERO_FOGO_INVALIDO)
    private Long numeroFogo;

    @NotNull(message = ApplicationConstantes.POSICAO_PNEU_OBRIGATORIA)
    @EnumValidation(enumClass = PosicaoPneu.class, message = ApplicationConstantes.POSICOES_ACEITAS_PNEU)
    private PosicaoPneu posicao;
}
