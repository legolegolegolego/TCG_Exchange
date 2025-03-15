package com.es.TCG_Commerce.dto;

// apuntesDTO: si el id es autoincremental, no poner en el DTO
public class TransaccionDTO {
    private double precio;
    private UsuarioDTO vendedor;
    private UsuarioDTO comprador;
    private CartaDTO carta;

    public TransaccionDTO() {
    }

    public TransaccionDTO(double precio, UsuarioDTO vendedor, UsuarioDTO comprador, CartaDTO carta) {
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

    public UsuarioDTO getVendedor() {
        return vendedor;
    }

    public void setVendedor(UsuarioDTO vendedor) {
        this.vendedor = vendedor;
    }

    public UsuarioDTO getComprador() {
        return comprador;
    }

    public void setComprador(UsuarioDTO comprador) {
        this.comprador = comprador;
    }

    public CartaDTO getCarta() {
        return carta;
    }

    public void setCarta(CartaDTO carta) {
        this.carta = carta;
    }
}
