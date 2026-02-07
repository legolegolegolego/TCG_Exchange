package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EstadoIntercambio;

// apuntesDTO: si el id es autoincremental, no poner en el DTO
public class IntercambioDTO {
    private Long idUsuarioOrigen;
    private Long idUsuarioDestino;
    private Long idCartaOrigen;
    private Long idCartaDestino;
    private EstadoIntercambio estado;

    public IntercambioDTO() {
    }

    public IntercambioDTO(Long idUsuarioOrigen, Long idUsuarioDestino, Long idCartaOrigen, Long idCartaDestino,
                          EstadoIntercambio estado) {
        this.idUsuarioOrigen = idUsuarioOrigen;
        this.idUsuarioDestino = idUsuarioDestino;
        this.idCartaOrigen = idCartaOrigen;
        this.idCartaDestino = idCartaDestino;
        this.estado = estado;
    }

    public Long getIdUsuarioOrigen() {
        return idUsuarioOrigen;
    }

    public void setIdUsuarioOrigen(Long idUsuarioOrigen) {
        this.idUsuarioOrigen = idUsuarioOrigen;
    }

    public Long getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public void setIdUsuarioDestino(Long idUsuarioDestino) {
        this.idUsuarioDestino = idUsuarioDestino;
    }


    public Long getIdCartaOrigen() {
        return idCartaOrigen;
    }

    public void setIdCartaOrigen(Long idCartaOrigen) {
        this.idCartaOrigen = idCartaOrigen;
    }

    public Long getIdCartaDestino() {
        return idCartaDestino;
    }

    public void setIdCartaDestino(Long idCartaDestino) {
        this.idCartaDestino = idCartaDestino;
    }

    public EstadoIntercambio getEstado() {
        return estado;
    }

    public void setEstado(EstadoIntercambio estado) {
        this.estado = estado;
    }
}
