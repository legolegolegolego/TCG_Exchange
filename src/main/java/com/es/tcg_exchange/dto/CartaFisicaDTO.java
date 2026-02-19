package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EstadoCarta;

public class CartaFisicaDTO {
    private Long id;
    private EstadoCarta estadoCarta;
    private Boolean disponible;
    private String imagenUrl;
    private Long idUsuario;
    private Long idCartaModelo;

    public CartaFisicaDTO() {
    }

    public CartaFisicaDTO(Long id, EstadoCarta estadoCarta, String imagenUrl, Long idUsuario, Long idCartaModelo) {
        this.id = id;
        this.estadoCarta = estadoCarta;
        this.imagenUrl = imagenUrl;
        this.idUsuario = idUsuario;
        this.idCartaModelo = idCartaModelo;
    }

    public CartaFisicaDTO(Long id, EstadoCarta estadoCarta, Boolean disponible, String imagenUrl, Long idUsuario,
                          Long idCartaModelo) {
        this.id = id;
        this.estadoCarta = estadoCarta;
        this.disponible = disponible;
        this.imagenUrl = imagenUrl;
        this.idUsuario = idUsuario;
        this.idCartaModelo = idCartaModelo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoCarta getEstadoCarta() {
        return estadoCarta;
    }

    public void setEstadoCarta(EstadoCarta estadoCarta) {
        this.estadoCarta = estadoCarta;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdCartaModelo() {
        return idCartaModelo;
    }

    public void setIdCartaModelo(Long idCartaModelo) {
        this.idCartaModelo = idCartaModelo;
    }
}
