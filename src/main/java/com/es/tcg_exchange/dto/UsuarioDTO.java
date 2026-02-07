package com.es.tcg_exchange.dto;


import java.util.List;

public class UsuarioDTO {
    private String username;
    private String password;
    private String roles;
    private boolean desactivado;
    private List<CartaFisicaDTO> cartasFisicas;


    public UsuarioDTO() {
    }

    public UsuarioDTO(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UsuarioDTO(String username, String password, String roles, List<CartaFisicaDTO> cartasFisicas) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.cartasFisicas = cartasFisicas;
    }

    public UsuarioDTO(String username, String password, String roles, boolean desactivado, List<CartaFisicaDTO> cartasFisicas) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.desactivado = desactivado;
        this.cartasFisicas = cartasFisicas;
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

    public List<CartaFisicaDTO> getCartasFisicas() {
        return cartasFisicas;
    }

    public void setCartasFisicas(List<CartaFisicaDTO> cartasFisicas) {
        this.cartasFisicas = cartasFisicas;
    }
}
