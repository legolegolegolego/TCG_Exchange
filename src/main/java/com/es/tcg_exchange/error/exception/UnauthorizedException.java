package com.es.tcg_exchange.error.exception;

public class UnauthorizedException extends RuntimeException {

    private static final String DESCRIPCION = "Unauthorized (401)";

    public UnauthorizedException(String mensaje) {
        super(DESCRIPCION +". "+ mensaje);
    }
}