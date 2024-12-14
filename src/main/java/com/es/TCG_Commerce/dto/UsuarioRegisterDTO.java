package com.es.TCG_Commerce.dto;

public class UsuarioRegisterDTO {
    private String username;
    private String password;
    private String password2;
    // no le pongo roles pq no quiero que tenga la posibilidad de elegir,
    // solo el admin podra crear otro admin desde la bd directamente


    public UsuarioRegisterDTO() {
    }

    public UsuarioRegisterDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UsuarioRegisterDTO(String username, String password, String password2) {
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
