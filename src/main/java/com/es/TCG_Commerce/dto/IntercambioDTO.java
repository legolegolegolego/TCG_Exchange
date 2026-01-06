package com.es.TCG_Commerce.dto;

import com.es.TCG_Commerce.model.enums.EstadoIntercambio;

// apuntesDTO: si el id es autoincremental, no poner en el DTO
public class IntercambioDTO {
    private Long usuarioOrigen;
    private Long usuarioDestino;
    private Long cartaOrigen;
    private Long cartaDestino;
    private EstadoIntercambio estado;

    public IntercambioDTO() {
    }

    public IntercambioDTO(Long usuarioOrigen, Long usuarioDestino, Long cartaOrigen, Long cartaDestino,
                          EstadoIntercambio estado) {
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.cartaOrigen = cartaOrigen;
        this.cartaDestino = cartaDestino;
        this.estado = estado;
    }

    public Long getUsuarioOrigen() {
        return usuarioOrigen;
    }

    public void setUsuarioOrigen(Long usuarioOrigen) {
        this.usuarioOrigen = usuarioOrigen;
    }

    public Long getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Long usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }


    public Long getCartaOrigen() {
        return cartaOrigen;
    }

    public void setCartaOrigen(Long cartaOrigen) {
        this.cartaOrigen = cartaOrigen;
    }

    public Long getCartaDestino() {
        return cartaDestino;
    }

    public void setCartaDestino(Long cartaDestino) {
        this.cartaDestino = cartaDestino;
    }

    public EstadoIntercambio getEstado() {
        return estado;
    }

    public void setEstado(EstadoIntercambio estado) {
        this.estado = estado;
    }
}
