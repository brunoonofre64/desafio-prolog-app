package com.prologapp.desafio.application.dtos;

import com.prologapp.desafio.application.anotations.ValidaPressao;
import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.application.anotations.EnumValidation;
import com.prologapp.desafio.domain.enums.StatusPneu;
import com.prologapp.desafio.presentation.v1.constantes.PresentationConstantes;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PneuRequestDTO {

    @NotNull(message = ApplicationConstantes.NUMERO_FOGO_OBRIGATORIO)
    @Min(value = 1, message = PresentationConstantes.NUMERO_FOGO_INVALIDO)
    private Long numeroFogo;

    @NotBlank(message = ApplicationConstantes.MARCA_OBRIGATORIA)
    @Size(max = 50, message = ApplicationConstantes.MAXIMO_CARACTERES_MARCA)
    private String marca;

    @NotNull(message = ApplicationConstantes.PRESSAO_OBRIGATORIA)
    @Min(value = 1, message = ApplicationConstantes.PRESSAO_DEVE_SER_MAIOR_QUE_ZERO)
    @ValidaPressao
    private BigDecimal pressaoAtual;

    @NotNull(message = ApplicationConstantes.STATUS_OBRIGATORIO)
    @EnumValidation(enumClass = StatusPneu.class, message = ApplicationConstantes.OPCOES_STATUS_PNEU)
    private StatusPneu status;
}
