package com.prologapp.desafio.domain.enums;

import lombok.Getter;

@Getter
public enum StatusPneu {
    USADO("U"),
    ESTOQUE("E"),
    REFORMA("R"),
    INUTILIZAVEL("N"),
    DESCARTADO("D");

    final String value;

    StatusPneu(String value) {
        this.value = value;
    }
}
