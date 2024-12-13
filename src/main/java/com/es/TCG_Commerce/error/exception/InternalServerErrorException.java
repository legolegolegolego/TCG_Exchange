package com.es.TCG_Commerce.error.exception;

public class InternalServerErrorException extends RuntimeException{

    private static final String DESCRIPCION = "Internal Server Error (509)";

    public InternalServerErrorException(String mensaje) {
        super(DESCRIPCION +". "+ mensaje);
    }
}
