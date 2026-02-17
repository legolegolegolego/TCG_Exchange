package com.es.tcg_exchange.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Rol {
    ADMIN,
    USER;

    // @JsonCreator permite a Jackson mapear automáticamente cualquier string del JSON al enum, ignorando mayus/minus.
    @JsonCreator
    public static Rol fromString(String key) {
        if (key == null) return null;
        return Rol.valueOf(key.trim().toUpperCase());
    }

    // toJson() devuelve siempre el nombre estándar cuando se serializa de vuelta a JSON.
    @JsonValue
    public String toJson() {
        return this.name();
    }
}
