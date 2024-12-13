package com.es.TCG_Commerce.error.exception;

public class UnauthorizedException extends RuntimeException {

    private static final String DESCRIPCION = "Not Authorized (401)";

    public UnauthorizedException(String mensaje) {
        super(DESCRIPCION +". "+ mensaje);
    }
}