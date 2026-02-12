package com.es.tcg_exchange.dto;

public class UsuarioDetailDTO {
    private Long id;
    private String username;
    private String roles;
    private boolean desactivado;

    public UsuarioDetailDTO() {
    }

    public UsuarioDetailDTO(Long id, String username, String roles, boolean desactivado) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.desactivado = desactivado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isDesactivado() {
        return desactivado;
    }

    public void setDesactivado(boolean desactivado) {
        this.desactivado = desactivado;
    }
}
