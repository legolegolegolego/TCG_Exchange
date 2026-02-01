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
    @Id
    private Long id;

    // Nombre único dentro del set
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

}
