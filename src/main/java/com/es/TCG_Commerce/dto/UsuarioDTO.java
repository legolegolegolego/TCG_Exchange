package com.es.TCG_Commerce.dto;


import java.util.List;

public class UsuarioDTO {

    private String username;
    private String password;
    private String roles;
    private List<CartaDTO> cartas;


    public UsuarioDTO(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UsuarioDTO(String username, String password, String roles, List<CartaDTO> cartas) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.cartas = cartas;
    }

    public UsuarioDTO() {
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<CartaDTO> getCartas() {
        return cartas;
    }

    public void setCartas(List<CartaDTO> cartas) {
        this.cartas = cartas;
    }
}
