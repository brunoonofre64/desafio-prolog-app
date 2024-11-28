package com.prologapp.desafio.application.utils;

import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.domain.enums.StatusVeiculo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusVeiculoConverter implements AttributeConverter<StatusVeiculo, String> {

    @Override
    public String convertToDatabaseColumn(StatusVeiculo statusVeiculo) {
        if (statusVeiculo == null) {
            return null;
        }
        return statusVeiculo.getValue();
    }

    @Override
    public StatusVeiculo convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "A" -> StatusVeiculo.ATIVO;
            case "I" -> StatusVeiculo.INATIVO;
            case "M" -> StatusVeiculo.MANUTENCAO;
            case "S" -> StatusVeiculo.SUCATEADO;
            default -> throw new IllegalArgumentException(ApplicationConstantes.VALOR_NAO_CORRESPONDE_OPCOES_VALIDAS + value);
        };
    }
}
