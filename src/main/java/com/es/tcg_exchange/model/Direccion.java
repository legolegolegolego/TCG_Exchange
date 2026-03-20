package com.es.tcg_exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnore // previene StackOverflowError por recursión infinita: Usuario -> Direccion -> Usuario...
    private Usuario usuario;

    // nombre de la persona o entidad/empresa de la direccion
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String calleYNumero;

    // puede ser null
    private String pisoYPuerta;

    @Column(nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    public Direccion() {
    }

    public Direccion(Usuario usuario, String nombre, String calleYNumero, String pisoYPuerta,
                     String codigoPostal, String ciudad, String pais) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.calleYNumero = calleYNumero;
        this.pisoYPuerta = pisoYPuerta;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCalleYNumero() {
        return calleYNumero;
    }

    public void setCalleYNumero(String calleYNumero) {
        this.calleYNumero = calleYNumero;
    }

    public String getPisoYPuerta() {
        return pisoYPuerta;
    }

    public void setPisoYPuerta(String pisoYPuerta) {
        this.pisoYPuerta = pisoYPuerta;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
