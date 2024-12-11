package com.es.TCG_Commerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double precio;

    @ManyToOne
    private Long id_vendedor;

    @ManyToOne
    private Long id_comprador;

    @ManyToOne // una carta solo por transaccion
    private Long id_carta;

}
