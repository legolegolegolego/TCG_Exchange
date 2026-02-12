package com.es.tcg_exchange.dto;


import java.util.List;

public class UsuarioFullDTO {
    private Long id;
    private String username;
    private String roles;
    private boolean desactivado;
    private List<CartaFisicaDTO> cartasFisicasDTO;


    public UsuarioFullDTO() {
    }

    public UsuarioFullDTO(Long id, String username, String roles, boolean desactivado, List<CartaFisicaDTO> cartasFisicasDTO) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.desactivado = desactivado;
        this.cartasFisicasDTO = cartasFisicasDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDesactivado() {
        return desactivado;
    }

    public void setDesactivado(boolean desactivado) {
        this.desactivado = desactivado;
    }

    public List<CartaFisicaDTO> getCartasFisicasDTO() {
        return cartasFisicasDTO;
    }

    public void setCartasFisicasDTO(List<CartaFisicaDTO> cartasFisicasDTO) {
        this.cartasFisicasDTO = cartasFisicasDTO;
    }
}
