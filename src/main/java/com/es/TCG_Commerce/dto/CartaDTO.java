package com.es.TCG_Commerce.dto;

import com.es.TCG_Commerce.model.Usuario;

public class CartaDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private int vida;
    private int ataque;
    private String username;

    public CartaDTO() {
    }

    public CartaDTO(Long id, String nombre, String tipo, int vida, int ataque, String username) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.vida = vida;
        this.ataque = ataque;
        this.username = username;
    }


    public CartaDTO(Long id, String nombre, String tipo, int vida, int ataque) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
