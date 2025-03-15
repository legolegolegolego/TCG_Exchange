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
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "id_comprador", nullable = false)
    private Usuario comprador;

    @ManyToOne // una carta solo por transaccion
    @JoinColumn(name = "id_carta", nullable = false)
    private Carta carta;

    public Transaccion() {
    }

    public Transaccion(Long id, double precio, Usuario vendedor, Usuario comprador, Carta carta) {
        this.id = id;
        this.precio = precio;
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.carta = carta;
    }

    public Transaccion(double precio, Usuario vendedor, Usuario comprador, Carta carta) {
        this.precio = precio;
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.carta = carta;
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

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }
}
