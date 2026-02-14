package com.es.tcg_exchange.dto;

public class UsernameUpdateDTO {
    private String nuevoUsername;

    public UsernameUpdateDTO() {
    }

    public UsernameUpdateDTO(String nuevoUsername) {
        this.nuevoUsername = nuevoUsername;
    }

    public String getNuevoUsername() {
        return nuevoUsername;
    }

    public void setNuevoUsername(String nuevoUsername) {
        this.nuevoUsername = nuevoUsername;
    }
}
