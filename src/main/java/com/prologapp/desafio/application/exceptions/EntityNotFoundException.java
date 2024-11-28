package com.prologapp.desafio.application.exceptions;

import com.prologapp.desafio.application.enums.CodigoErro;

public class EntityNotFoundException extends RuntimeException {
    final CodigoErro codigoErro;
    final Object[] args;

    public EntityNotFoundException(CodigoErro codigoErro, Object... args) {
        this.codigoErro = codigoErro;
        this.args = args != null ? args : new Object[0];
    }

    public CodigoErro getCodigoErro() {
        return codigoErro;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        return codigoErro.toString();
    }
}

