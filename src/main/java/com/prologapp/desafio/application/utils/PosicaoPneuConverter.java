package com.prologapp.desafio.application.utils;

import com.prologapp.desafio.application.constantes.ApplicationConstantes;
import com.prologapp.desafio.domain.enums.PosicaoPneu;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PosicaoPneuConverter implements AttributeConverter<PosicaoPneu, String> {
    @Override
    public String convertToDatabaseColumn(PosicaoPneu posicaoPneu) {
        if (posicaoPneu == null) {
            return null;
        }
        return posicaoPneu.name();
    }

    @Override
    public PosicaoPneu convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        try {
            return PosicaoPneu.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ApplicationConstantes.VALOR_POSICAO_PNEU_INVALIDA + value);
        }
    }
}
