package com.es.tcg_exchange.model;

import com.es.tcg_exchange.model.enums.EstadoCarta;
import jakarta.persistence.*;

// Carta física / oferta de intercambio
@Entity
@Table(name = "cartas_fisicas")
public class CartaFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarta estadoCarta;

    // inicializar automaticamente en true.
    // tras intercambio aceptado: false.
    @Column(nullable = false)
    private boolean disponible = true;

    @Column(nullable = false)
    private String imagenUrl; // foto real subida por el usuario

    // Dueño de la carta
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Modelo conceptual de la carta
    @ManyToOne(optional = false)
    @JoinColumn(name = "carta_modelo_id", nullable = false)
    private CartaModelo cartaModelo;

    public CartaFisica() {
    }

    public CartaFisica(Long id, EstadoCarta estadoCarta, String imagenUrl, Usuario usuario, CartaModelo cartaModelo) {
        this.id = id;
        this.estadoCarta = estadoCarta;
        this.imagenUrl = imagenUrl;
        this.usuario = usuario;
        this.cartaModelo = cartaModelo;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CartaModelo getCartaModelo() {
        return cartaModelo;
    }

    public void setCartaModelo(CartaModelo cartaModelo) {
        this.cartaModelo = cartaModelo;
    }
}