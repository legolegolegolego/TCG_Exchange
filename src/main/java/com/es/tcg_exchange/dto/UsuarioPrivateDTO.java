package com.es.tcg_exchange.dto;


import java.util.List;

public class UsuarioPrivateDTO {
    private String username;
    private String roles;
    private boolean desactivado;
    private List<CartaFisicaDTO> cartasFisicasDTO;


    public UsuarioPrivateDTO() {
    }

    public UsuarioPrivateDTO(String username, String roles, List<CartaFisicaDTO> cartasFisicasDTO) {
        this.username = username;
        this.roles = roles;
        this.cartasFisicasDTO = cartasFisicasDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<CartaFisicaDTO> getCartasFisicas() {
        return cartasFisicasDTO;
    }

    public void setCartasFisicas(List<CartaFisicaDTO> cartasFisicasDTO) {
        this.cartasFisicasDTO = cartasFisicasDTO;
    }
}
