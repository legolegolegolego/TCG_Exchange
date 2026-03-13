package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EstadoIntercambio;

public class IntercambioDTO {
    private Long id;
    private String usernameOrigen;
    private String usernameDestino;
    private Long idCartaOrigen;
    private Long idCartaDestino;
    private String direccionOrigen;
    private String direccionDestino;
    private EstadoIntercambio estado;

    public IntercambioDTO() {
    }

    public IntercambioDTO(Long id, String usernameOrigen, String usernameDestino, Long idCartaOrigen,
                          Long idCartaDestino, String direccionOrigen, String direccionDestino, EstadoIntercambio estado) {
        this.id = id;
        this.usernameOrigen = usernameOrigen;
        this.usernameDestino = usernameDestino;
        this.idCartaOrigen = idCartaOrigen;
        this.idCartaDestino = idCartaDestino;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsernameOrigen() {
        return usernameOrigen;
    }

    public void setUsernameOrigen(String usernameOrigen) {
        this.usernameOrigen = usernameOrigen;
    }

    public String getUsernameDestino() {
        return usernameDestino;
    }

    public void setUsernameDestino(String usernameDestino) {
        this.usernameDestino = usernameDestino;
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

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }
}
