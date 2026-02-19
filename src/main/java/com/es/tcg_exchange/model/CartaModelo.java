package com.es.tcg_exchange.model;

import com.es.tcg_exchange.model.enums.EtapaEvolucion;
import com.es.tcg_exchange.model.enums.Rareza;
import com.es.tcg_exchange.model.enums.TipoCarta;
import com.es.tcg_exchange.model.enums.TipoPokemon;
import jakarta.persistence.*;

// Carta conceptual: idea de carta
@Entity
@Table(name = "cartas_modelo")
public class CartaModelo {
    // id interno autogenerado
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // número oficial de la carta
    @Column(nullable = false, unique = true)
    private Long numero;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCarta tipoCarta;

    // ---- COMÚN A TODAS ----

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rareza rareza;

    @Column(nullable = false)
    private String imagenUrl;

    // ---- SOLO PARA POKÉMON ----

    // nullable true implícito
    // si tipoCarta = ENTRENADOR, estos dos DEBEN ser null
    @Enumerated(EnumType.STRING)
    private TipoPokemon tipoPokemon;

    @Enumerated(EnumType.STRING)
    private EtapaEvolucion evolucion;

    @Column(nullable = false)
    private boolean activo = true;

    public CartaModelo() {
    }

    public CartaModelo(Long id, String nombre, TipoCarta tipoCarta, Rareza rareza, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCarta = tipoCarta;
        this.rareza = rareza;
        this.imagenUrl = imagenUrl;
    }

    public CartaModelo(Long id, String nombre, TipoCarta tipoCarta, Rareza rareza, String imagenUrl,
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
