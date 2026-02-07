package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EstadoCarta;

public class CartaFisicaDTO {
    private Long id;
    private EstadoCarta estadoCarta;
    private boolean disponible;
    private String imagenUrl;
    private Long idUsuario; // ver con el tiempo si mejor username
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

    public CartaFisicaDTO(Long id, EstadoCarta estadoCarta, boolean disponible, String imagenUrl, Long idUsuario, Long idCartaModelo) {
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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
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
