package com.es.tcg_exchange.model;

import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import jakarta.persistence.*;

@Entity
@Table(name = "intercambios")
public class Intercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // optional afecta a jpa, nullable a bd (proteccion)
    @JoinColumn(name = "id_usuario_origen", nullable = false)
    private Usuario usuarioOrigen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario_destino", nullable = false)
    private Usuario usuarioDestino;

    // carta ofrecida por usuario origen:
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_carta_origen", nullable = false)
    private CartaFisica cartaOrigen;

    // carta ofrecida por usuario destinatario:
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_carta_destino", nullable = false)
    private CartaFisica cartaDestino;

    // estado (pendiente, aceptado, rechazado):
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIntercambio estado;

    public Intercambio() {
    }

    public Intercambio(Long id, Usuario usuarioOrigen, Usuario usuarioDestino, CartaFisica cartaOrigen, CartaFisica cartaDestino,
                       EstadoIntercambio estado) {
        this.id = id;
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.cartaOrigen = cartaOrigen;
        this.cartaDestino = cartaDestino;
        this.estado = estado;
    }

    public Intercambio(Usuario usuarioOrigen, Usuario usuarioDestino, CartaFisica cartaOrigen, CartaFisica cartaDestino,
                       EstadoIntercambio estado) {
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.cartaOrigen = cartaOrigen;
        this.cartaDestino = cartaDestino;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarioOrigen() {
        return usuarioOrigen;
    }

    public void setUsuarioOrigen(Usuario usuarioOrigen) {
        this.usuarioOrigen = usuarioOrigen;
    }

    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(Usuario usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public CartaFisica getCartaOrigen() {
        return cartaOrigen;
    }

    public void setCartaOrigen(CartaFisica cartaOrigen) {
        this.cartaOrigen = cartaOrigen;
    }

    public CartaFisica getCartaDestino() {
        return cartaDestino;
    }

    public void setCartaDestino(CartaFisica cartaDestino) {
        this.cartaDestino = cartaDestino;
    }

    public EstadoIntercambio getEstado() {
        return estado;
    }

    public void setEstado(EstadoIntercambio estado) {
        this.estado = estado;
    }
}
