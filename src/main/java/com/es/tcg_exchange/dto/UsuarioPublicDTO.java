package com.es.tcg_exchange.dto;

import java.util.List;

public class UsuarioPublicDTO {
    private String username;
    private List<CartaFisicaDTO> cartasFisicasDTO;

    public UsuarioPublicDTO() {
    }

    public UsuarioPublicDTO(String username, List<CartaFisicaDTO> cartasFisicasDTO) {
        this.username = username;
        this.cartasFisicasDTO = cartasFisicasDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartaFisicaDTO> getCartasFisicasDTO() {
        return cartasFisicasDTO;
    }

    public void setCartasFisicasDTO(List<CartaFisicaDTO> cartasFisicasDTO) {
        this.cartasFisicasDTO = cartasFisicasDTO;
    }
}
