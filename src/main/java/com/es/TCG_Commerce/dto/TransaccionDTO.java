package com.es.TCG_Commerce.dto;

// apuntesDTO: si el id es autoincremental, no poner en el DTO
public class TransaccionDTO {
    private double precio;
    private Long id_vendedor;
    private Long id_comprador;
    private Long id_carta;

    public TransaccionDTO() {
    }

    public TransaccionDTO(double precio, Long id_vendedor, Long id_comprador, Long id_carta) {
        this.precio = precio;
        this.id_vendedor = id_vendedor;
        this.id_comprador = id_comprador;
        this.id_carta = id_carta;
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
