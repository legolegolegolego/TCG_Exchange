package com.es.TCG_Commerce.model;

import com.es.TCG_Commerce.model.enums.TipoEnergia;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Ataque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // puede ser null si el ataque no hace daño
    private Integer danio;

    @Column(length = 500)
    private String efecto;

    // coste energético
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TipoEnergia> costeEnergia;

    @ManyToOne(optional = false)
    private CartaModelo cartaModelo;
}
