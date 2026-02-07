package com.es.tcg_exchange.utils;

import com.es.tcg_exchange.dto.*;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.Rol;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

    public static UsuarioDTO entityToDTO(Usuario u) {
        return new UsuarioDTO(
                u.getUsername(),
                u.getPassword(),
                u.getRoles(),
                Mapper.cartasToDTOs(u.getCartasFisicas())
        );
    }

    // usuarioS
    public static List<UsuarioDTO> entitiesToDTOs(List<Usuario> usuarios) {
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();

        // Iterar sobre cada Usuario y convertirlo a UsuarioDTO
        usuarios.forEach(u -> {
            UsuarioDTO dto = entityToDTO(u); // Utiliza el método de la propia clase para convertir
            usuarioDTOs.add(dto); // Agregar el DTO a la lista
        });

        return usuarioDTOs; // Retorna la lista de DTOs
    }

        public static UsuarioLoginDTO entityLoginToDTO(Usuario u) {
        return new UsuarioLoginDTO(
                u.getUsername(),
                u.getPassword()
        );
    }

    public static UsuarioRegisterDTO entityRegisterToDTO(Usuario u) {
        return new UsuarioRegisterDTO(
                u.getUsername(),
                u.getPassword(),
                u.getRol().name()
        );
    }

    public static Usuario DTOToEntity(UsuarioDTO uDTO){
        return new Usuario(
                uDTO.getUsername(),
                uDTO.getPassword(),
                uDTO.getRoles(),
                Mapper.DTOsToEntities(uDTO.getCartas())
        );
    }

    public static Usuario DTOToEntity(UsuarioLoginDTO ulDTO){
        return new Usuario(
                ulDTO.getUsername(),
                ulDTO.getPassword()
        );
    }

    public static Usuario DTOToEntity(UsuarioRegisterDTO urDTO){
        return new Usuario(
                urDTO.getUsername(),
                urDTO.getPassword(),
                urDTO.getPassword2(),
                Rol.valueOf(urDTO.getRoles())
        );
    }

    public static CartaDTO entityToDTO(Carta c){

        // si no tiene usuario asignado no se inicializa con nombre
        if (c.getUsuario() != null){
            return new CartaDTO(
                    c.getId(),
                    c.getNombre(),
                    c.getTipo(),
                    c.getVida(),
                    c.getAtaque(),
                    c.getUsuario().getUsername()
            );
        } else {
            return new CartaDTO(
                    c.getId(),
                    c.getNombre(),
                    c.getTipo(),
                    c.getVida(),
                    c.getAtaque()
            );
        }

    }

    // cartaS
    public static List<CartaDTO> cartasToDTOs(List<Carta> cartas) {
        List<CartaDTO> cartasDTO = new ArrayList<>();

        cartas.forEach(c -> {
            CartaDTO dto = entityToDTO(c); // Utiliza el método de la propia clase para convertir
            cartasDTO.add(dto); // Agregar el DTO a la lista
        });

        return cartasDTO; // Retorna la lista de DTOs
    }

    // cartaS
    public static List<Carta> DTOsToEntities(List<CartaDTO> cartasDTO) {
        List<Carta> cartas = new ArrayList<>();

        cartasDTO.forEach(dto -> {
            Carta c = DTOToEntity(dto); // Utiliza el método de la propia clase para convertir
            cartas.add(c); // Agregar el DTO a la lista
        });

        return cartas; // Retorna la lista de DTOs
    }

    public static Carta DTOToEntity(CartaDTO cDTO){

        return new Carta(
                cDTO.getId(),
                cDTO.getNombre(),
                cDTO.getTipo(),
                cDTO.getVida(),
                cDTO.getAtaque()
        );
    }

    public static IntercambioDTO entityToDTO(Intercambio t) {
        return new IntercambioDTO(
                t.getUsuarioOrigen().getId(),
                t.getUsuarioDestino().getId(),
                t.getCartaOrigen().getId(),
                t.getCartaDestino().getId(),
                t.getEstado()
        );
    }


}
