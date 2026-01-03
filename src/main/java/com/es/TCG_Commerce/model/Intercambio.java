package com.es.TCG_Commerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "intercambios")
public class Intercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private double precio;

    @ManyToOne
    @JoinColumn(name = "id_usuarioA", nullable = false)
    private Usuario usuarioA;

    @ManyToOne
    @JoinColumn(name = "id_usuarioB", nullable = false)
    private Usuario usuarioB;

    // carta ofrecida por usuario A: (cambiar)
    @ManyToOne
    @JoinColumn(name = "id_carta", nullable = false)
    private Carta carta;

    // carta ofrecida por usuario B:





    public Intercambio() {
    }

    public Intercambio(Long id, Usuario usuarioA, Usuario usuarioB, Carta carta) {
        this.id = id;
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.carta = carta;
    }

    public Intercambio(Usuario usuarioA, Usuario usuarioB, Carta carta) {
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.carta = carta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarioA() {
        return usuarioA;
    }

    public void setUsuarioA(Usuario usuarioA) {
        this.usuarioA = usuarioA;
    }

    public Usuario getUsuarioB() {
        return usuarioB;
    }

    public void setUsuarioB(Usuario usuarioB) {
        this.usuarioB = usuarioB;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }
}
