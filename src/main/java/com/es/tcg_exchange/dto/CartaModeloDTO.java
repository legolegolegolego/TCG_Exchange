package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EtapaEvolucion;
import com.es.tcg_exchange.model.enums.Rareza;
import com.es.tcg_exchange.model.enums.TipoCarta;
import com.es.tcg_exchange.model.enums.TipoPokemon;

public class CartaModeloDTO {
    private Long id;
    private String nombre;
    private TipoCarta tipoCarta;
    private Rareza rareza;
    private String imagenUrl;
    // segun tipocarta:
    private TipoPokemon tipoPokemon;
    private EtapaEvolucion evolucion;

    public CartaModeloDTO() {
    }

    public CartaModeloDTO(Long id, String nombre, TipoCarta tipoCarta, Rareza rareza, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCarta = tipoCarta;
        this.rareza = rareza;
        this.imagenUrl = imagenUrl;
    }

    public CartaModeloDTO(Long id, String nombre, TipoCarta tipoCarta, Rareza rareza, String imagenUrl,
                          TipoPokemon tipoPokemon, EtapaEvolucion evolucion) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCarta = tipoCarta;
        this.rareza = rareza;
        this.imagenUrl = imagenUrl;
        this.tipoPokemon = tipoPokemon;
        this.evolucion = evolucion;
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

    public TipoCarta getTipoCarta() {
        return tipoCarta;
    }

    public void setTipoCarta(TipoCarta tipoCarta) {
        this.tipoCarta = tipoCarta;
    }

    public Rareza getRareza() {
        return rareza;
    }

    public void setRareza(Rareza rareza) {
        this.rareza = rareza;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public TipoPokemon getTipoPokemon() {
        return tipoPokemon;
    }

    public void setTipoPokemon(TipoPokemon tipoPokemon) {
        this.tipoPokemon = tipoPokemon;
    }

    public EtapaEvolucion getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(EtapaEvolucion evolucion) {
        this.evolucion = evolucion;
    }
}
