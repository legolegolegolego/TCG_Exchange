package com.es.tcg_exchange.error.exception;

public class ForbiddenException extends RuntimeException {

    private static final String DESCRIPCION = "Forbidden (403)";

    public ForbiddenException(String mensaje) {
        super(DESCRIPCION + ". " + mensaje);
    }
}
