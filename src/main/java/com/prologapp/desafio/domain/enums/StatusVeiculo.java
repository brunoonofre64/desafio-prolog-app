package com.prologapp.desafio.domain.enums;

import lombok.Getter;

@Getter
public enum StatusVeiculo {
    ATIVO("A"),
    INATIVO("I"),
    MANUTENCAO("M"),
    SUCATEADO("S");

    final String value;

    StatusVeiculo(String value) {
        this.value = value;
    }
}
