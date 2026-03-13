package com.es.tcg_exchange.dto;

public record DireccionCreateDTO(
        String calleYNumero,
        String pisoYPuerta,
        String codigoPostal,
        String ciudad,
        String pais
) {
}
