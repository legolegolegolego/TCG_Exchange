package com.es.TCG_Commerce.dto;

import java.util.List;

public class CartaDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private int vida;
    private int ataque;
    private List<UsuarioDTO> vendedores;

    public CartaDTO() {
    }

    public CartaDTO(Long id, String nombre, String tipo, int vida, int ataque, List<UsuarioDTO> vendedores) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.vida = vida;
        this.ataque = ataque;
        this.vendedores = vendedores;
    }

    public CartaDTO(String nombre, String tipo, int vida, int ataque, List<UsuarioDTO> vendedores) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.vida = vida;
        this.ataque = ataque;
        this.vendedores = vendedores;
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

    public String getTipo() {
        return tipo;
    }

    public int getVida() {
        return vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public List<UsuarioDTO> getVendedores() {
        return vendedores;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void setVendedores(List<UsuarioDTO> vendedores) {
        this.vendedores = vendedores;
    }
}
