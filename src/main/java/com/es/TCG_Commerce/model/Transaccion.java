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
    @JoinColumn(name = "id_vendedor", nullable = false)
    private Long id_vendedor;

    @ManyToOne
    @JoinColumn(name = "id_comprador", nullable = false)
    private Long id_comprador;

    @ManyToOne // una carta solo por transaccion
    @JoinColumn(name = "id_carta", nullable = false)
    private Long id_carta;

    public Transaccion() {
    }

    public Transaccion(double precio, Long id_vendedor, Long id_comprador, Long id_carta) {
        this.precio = precio;
        this.id_vendedor = id_vendedor;
        this.id_comprador = id_comprador;
        this.id_carta = id_carta;
    }

    public Transaccion(Long id, double precio, Long id_vendedor, Long id_comprador, Long id_carta) {
        this.id = id;
        this.precio = precio;
        this.id_vendedor = id_vendedor;
        this.id_comprador = id_comprador;
        this.id_carta = id_carta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Long getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(Long id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public Long getId_comprador() {
        return id_comprador;
    }

    public void setId_comprador(Long id_comprador) {
        this.id_comprador = id_comprador;
    }

    public Long getId_carta() {
        return id_carta;
    }

    public void setId_carta(Long id_carta) {
        this.id_carta = id_carta;
    }
}
