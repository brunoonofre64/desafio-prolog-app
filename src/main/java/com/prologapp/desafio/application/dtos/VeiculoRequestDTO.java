package com.prologapp.desafio.application.dtos;


import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.application.anotations.EnumValidation;
import com.prologapp.desafio.domain.enums.StatusVeiculo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoRequestDTO {
    @NotBlank(message = ApplicationConstantes.PLACA_OBRIGATORIA)
    @Pattern(regexp = ApplicationConstantes.REGEX_VALIDACAO_PLACA, message = ApplicationConstantes.PADRAO_OBRIGATORIO_PLACA)
    private String placa;

    @NotBlank(message = ApplicationConstantes.MARCA_OBRIGATORIA)
    private String marca;

    @NotNull(message = ApplicationConstantes.QUILIMETRAGEM_OBRIGATORIA)
    @Min(value = 0, message = ApplicationConstantes.QUILIMETRAGEM_NAO_NEGATIVA)
    private Integer quilometragem;

    @NotNull(message = ApplicationConstantes.STATUS_OBRIGATORIO)
    @EnumValidation(enumClass = StatusVeiculo.class, message = ApplicationConstantes.OPCOES_STATUS)
    private StatusVeiculo status;
}
