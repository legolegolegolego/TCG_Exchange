package com.es.tcg_exchange.dto;

public class UsuarioLoginDTO {
    private String identifier; // username o email
    private String password;

    public UsuarioLoginDTO() {
    }

    public UsuarioLoginDTO(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
