package com.es.TCG_Commerce.model;

import com.es.TCG_Commerce.model.enums.EstadoIntercambio;
import jakarta.persistence.*;

@Entity
@Table(name = "intercambios")
public class Intercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // optional afecta a jpa, nullable a bd (proteccion)
    @JoinColumn(name = "id_usuario_origen") // cambio nullable = false por optional arriba (nullable implicito)
    private Usuario usuarioOrigen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario_destino", nullable = false)
    private Usuario usuarioDestino;

    // carta ofrecida por usuario origen: (cambiar)
    @ManyToOne
    @JoinColumn(name = "id_carta_origen", nullable = false)
    private Carta cartaOrigen;

    // carta ofrecida por usuario destinatario:
    @ManyToOne
    @JoinColumn(name = "id_carta_destino", nullable = false)
    private Carta cartaDestino;

    // estado (pendiente, aceptado, rechazado):
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIntercambio estado;

    public Intercambio() {
    }

    public Intercambio(Long id, Usuario usuarioOrigen, Usuario usuarioDestino, Carta cartaOrigen, Carta cartaDestino,
                       EstadoIntercambio estado) {
        this.id = id;
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;
        this.cartaOrigen = cartaOrigen;
        this.cartaDestino = cartaDestino;
        this.estado = estado;
    }

    public Intercambio(Usuario usuarioOrigen, Usuario usuarioDestino, Carta cartaOrigen, Carta cartaDestino,
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


    public Carta getCartaOrigen() {
        return cartaOrigen;
    }

    public void setCartaOrigen(Carta cartaOrigen) {
        this.cartaOrigen = cartaOrigen;
    }

    public Carta getCartaDestino() {
        return cartaDestino;
    }

    public void setCartaDestino(Carta cartaDestino) {
        this.cartaDestino = cartaDestino;
    }

    public EstadoIntercambio getEstado() {
        return estado;
    }

    public void setEstado(EstadoIntercambio estado) {
        this.estado = estado;
    }
}
