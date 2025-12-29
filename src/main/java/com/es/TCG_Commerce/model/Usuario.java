package com.es.TCG_Commerce.model;

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

    // Se establece por defecto como USER en los distintos constructores (menos el específico para pasarle uno)
    // que hago con lo de user y admin para mi aplicacion?? dejo seleccionar?
    private String roles; // USER o ADMIN"

    // !!cambiar a Many to Many, muchas cartas tienen muchos usuarios
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Carta> cartas;

    public Usuario() {
        this.roles = "USER";
    }

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = "USER";
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

    public Usuario(Long id, String username, String password, String roles, List<Carta> cartas) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.cartas = cartas;
    }

    public Usuario(String username, String password, String roles, List<Carta> cartas) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.cartas = cartas;
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(this.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}
