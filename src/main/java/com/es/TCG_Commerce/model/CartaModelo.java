package com.es.TCG_Commerce.model;

import com.es.TCG_Commerce.model.enums.EtapaEvolucion;
import com.es.TCG_Commerce.model.enums.Rareza;
import com.es.TCG_Commerce.model.enums.TipoCarta;
import com.es.TCG_Commerce.model.enums.TipoPokemon;
import jakarta.persistence.*;

// Carta conceptual: idea de carta
@Entity
@Table(name = "cartas_modelo")
public class CartaModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre único dentro del set
    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCarta tipoCarta;

    // ---- SOLO PARA POKÉMON ----

    @Enumerated(EnumType.STRING)
    private TipoPokemon tipoPokemon;

    @Enumerated(EnumType.STRING)
    private EtapaEvolucion etapa;

    // uso de Integer en lugar de int para claridad en la no recepción de valores:
    // ya que un Integer puede ser null tal cual, y puedo verificar entonces si el campo se rellenó
    // si utilizo int, 0 equivale al valor 0 y a null
    private Integer hp;

    private Integer costeRetirada;

    // Debilidad y resistencia (Base Set es simple)
    @Enumerated(EnumType.STRING)
    private TipoPokemon debilidad;

    @Enumerated(EnumType.STRING)
    private TipoPokemon resistencia;

    private Integer valorResistencia; // normalmente 30

    // ---- COMÚN A TODAS ----

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rareza rareza;
}
