package com.es.tcg_exchange.dto;

public class UsuarioRegisterDTO {
    private String username;
    private String password;
    private String password2;
    private String roles;


    public UsuarioRegisterDTO() {
    }

    public UsuarioRegisterDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UsuarioRegisterDTO(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.password2 = password2;
        this.roles = roles;
    }

    public UsuarioRegisterDTO(String username, String password, String password2, String roles) {
        this.username = username;
        this.password = password;
        this.password2 = password2;
        this.roles = roles;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
