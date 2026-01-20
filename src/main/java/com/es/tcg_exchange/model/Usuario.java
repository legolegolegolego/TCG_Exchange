package com.es.tcg_exchange.model;

import com.es.tcg_exchange.model.enums.Rol;
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

    @Column(nullable = false)
    private Rol rol;
//    private String rol; // USER o ADMIN"

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<CartaFisica> cartaFisicas;

    public Usuario() {
//        this.rol = "USER";
        this.rol = Rol.USER;
    }

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
        this.rol = Rol.USER;
    }

    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(Long id, String username, String password, Rol rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(Long id, String username, String password, Rol rol, List<CartaFisica> cartaFisicas) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.cartaFisicas = cartaFisicas;
    }

    public Usuario(String username, String password, Rol rol, List<CartaFisica> cartaFisicas) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.cartaFisicas = cartaFisicas;
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

//    version String:
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Stream.of(this.getRol().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }

//    version null:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<CartaFisica> getCartaFisicas() {
        return cartaFisicas;
    }

    public void setCartaFisicas(List<CartaFisica> cartaFisicas) {
        this.cartaFisicas = cartaFisicas;
    }
}
