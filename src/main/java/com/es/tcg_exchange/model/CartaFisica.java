package com.es.tcg_exchange.model;

import com.es.tcg_exchange.model.enums.EstadoCarta;
import jakarta.persistence.*;

// Carta física / oferta de intercambio
@Entity
@Table(name = "cartas_fisicas")
public class CartaFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarta estadoCarta;

    // Dueño actual de la carta (oferta)
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Modelo conceptual de la carta
    @ManyToOne(optional = false)
    @JoinColumn(name = "carta_modelo_id", nullable = false)
    private CartaModelo cartaModelo;

}