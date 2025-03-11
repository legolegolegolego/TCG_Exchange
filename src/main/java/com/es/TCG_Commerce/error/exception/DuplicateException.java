package com.es.TCG_Commerce.error.exception;

public class DuplicateException extends RuntimeException {
    private static final String DESCRIPCION = "Duplicate (419)";

    public DuplicateException(String mensaje) {
        super(DESCRIPCION +". "+ mensaje);
    }
}
