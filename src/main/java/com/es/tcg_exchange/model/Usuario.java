package com.es.tcg_exchange.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Rol del usuario: "USER" o "ADMIN"
    // Se inicia por defecto como USER (excepto en constructor específico de roles)
    @Column(nullable = false)
    private String roles = "USER"; // o ADMIN"

    // por defecto un usuario no está desactivado, solo cuando se intente borrar y haya restricciones para su borrado en BD
    @Column(nullable = false)
    private boolean desactivado = false;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<CartaFisica> cartasFisicas;

    public Usuario() {
    }

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Usuario(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Usuario(Long id, String username, String password, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(this.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<CartaFisica> getCartasFisicas() {
        return cartasFisicas;
    }

    public void setCartasFisicas(List<CartaFisica> cartasFisicas) {
        this.cartasFisicas = cartasFisicas;
    }
}
