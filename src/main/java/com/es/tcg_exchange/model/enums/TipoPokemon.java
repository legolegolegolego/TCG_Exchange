package com.es.tcg_exchange.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoPokemon {
    PLANTA,
    FUEGO,
    AGUA,
    ELECTRICO,
    PSIQUICO,
    LUCHA,
    INCOLORO;

    // @JsonCreator permite a Jackson mapear automáticamente cualquier string del JSON al enum, ignorando mayus/minus.
    @JsonCreator
    public static TipoPokemon fromString(String key) {
        if (key == null) return null;
        return TipoPokemon.valueOf(key.trim().toUpperCase());
    }

    // toJson() devuelve siempre el nombre estándar cuando se serializa de vuelta a JSON.
    @JsonValue
    public String toJson() {
        return this.name();
    }
}
