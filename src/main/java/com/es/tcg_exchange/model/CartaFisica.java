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

    // inicializar automaticamente en true.
    // tras intercambio aceptado: false.
    @Column(nullable = false)
    private boolean disponible;

    @Column(nullable = false)
    private String imagenUrl; // foto real subida por el usuario

    // Dueño de la carta
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Modelo conceptual de la carta
    @ManyToOne(optional = false)
    @JoinColumn(name = "carta_modelo_id", nullable = false)
    private CartaModelo cartaModelo;

}