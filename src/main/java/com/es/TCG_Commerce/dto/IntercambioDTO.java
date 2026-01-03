package com.es.TCG_Commerce.dto;

// apuntesDTO: si el id es autoincremental, no poner en el DTO
public class IntercambioDTO {
    private Long usuarioA;
    private Long usuarioB;
    private Long carta;

    public IntercambioDTO() {
    }

    public IntercambioDTO(Long usuarioA, Long usuarioB, Long carta) {
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.carta = carta;
    }

    public Long getUsuarioA() {
        return usuarioA;
    }

    public void setUsuarioA(Long usuarioA) {
        this.usuarioA = usuarioA;
    }

    public Long getUsuarioB() {
        return usuarioB;
    }

    public void setUsuarioB(Long usuarioB) {
        this.usuarioB = usuarioB;
    }

    public Long getCarta() {
        return carta;
    }

    public void setCarta(Long carta) {
        this.carta = carta;
    }
}
