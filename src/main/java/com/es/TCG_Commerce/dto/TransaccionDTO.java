package com.es.TCG_Commerce.dto;

// apuntesDTO: si el id es autoincremental, no poner en el DTO
public class TransaccionDTO {
    private double precio;
    private Long vendedor;
    private Long comprador;
    private Long carta;

    public TransaccionDTO() {
    }

    public TransaccionDTO(double precio, Long vendedor, Long comprador, Long carta) {
        this.precio = precio;
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.carta = carta;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Long getVendedor() {
        return vendedor;
    }

    public void setVendedor(Long vendedor) {
        this.vendedor = vendedor;
    }

    public Long getComprador() {
        return comprador;
    }

    public void setComprador(Long comprador) {
        this.comprador = comprador;
    }

    public Long getCarta() {
        return carta;
    }

    public void setCarta(Long carta) {
        this.carta = carta;
    }
}
