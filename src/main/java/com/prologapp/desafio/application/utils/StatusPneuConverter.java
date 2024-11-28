package com.prologapp.desafio.application.utils;

import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.domain.enums.StatusPneu;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusPneuConverter implements AttributeConverter<StatusPneu, String> {
    @Override
    public String convertToDatabaseColumn(StatusPneu statusPneu) {
        if (statusPneu == null) {
            return null;
        }
        return statusPneu.getValue();
    }

    @Override
    public StatusPneu convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "U" -> StatusPneu.USADO;
            case "E" -> StatusPneu.ESTOQUE;
            case "N" -> StatusPneu.INUTILIZAVEL;
            case "R" -> StatusPneu.REFORMA;
            case "D" -> StatusPneu.DESCARTADO;
            default -> throw new IllegalArgumentException(ApplicationConstantes.VALOR_NAO_CORRESPONDE_OPCOES_VALIDAS + value);
        };
    }
}
