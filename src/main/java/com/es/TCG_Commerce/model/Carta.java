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
    private String tipo; // FUEGO, AGUA, PLANTA

    @Column(nullable = false)
    private int vida;

    @Column(nullable = false)
    private int ataque;

    // cambiar a many to many (n usuarios pueden tener n cartas y n cartas pueden tenerlas n usuarios)
    // por lo tanto: cambiar a lista de usuarios
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private Usuario usuario;

    public Carta() {
    }

    public Carta(Long id, String nombre, String tipo, int vida, int ataque, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.vida = vida;
        this.ataque = ataque;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
