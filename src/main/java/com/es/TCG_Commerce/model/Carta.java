package com.es.TCG_Commerce.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cartas")
public class Carta {

    @Id
    // los id en las cartas son asignados, no autogenerados
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo; // FUEGO, AGUA, RAYO, LUCHA, DRAGON, PSIQUICO, PLANTA, OSCURO, METAL, NORMAL.

    @Column(nullable = false)
    private int vida;

    @Column(nullable = false)
    private int ataque;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;

    public Carta() {
    }

    public Carta(Long id, String nombre, String tipo, int vida, int ataque, Usuario vendedor) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.vida = vida;
        this.ataque = ataque;
        this.vendedor = vendedor;
    }

    public Carta(Long id, String nombre, String tipo, int vida, int ataque) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.vida = vida;
        this.ataque = ataque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }
}
