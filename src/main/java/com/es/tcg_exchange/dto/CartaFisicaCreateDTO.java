package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EstadoCarta;

public record CartaFisicaCreateDTO(Long idCartaModelo, EstadoCarta estadoCarta, String imagenUrl) {
}
