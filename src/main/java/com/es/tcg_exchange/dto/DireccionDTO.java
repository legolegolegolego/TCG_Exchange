package com.es.tcg_exchange.dto;

public record DireccionDTO(
        Long id,
        Long idUsuario,
        String calleYNumero,
        String pisoYPuerta,
        String codigoPostal,
        String ciudad,
        String pais
) {
}
