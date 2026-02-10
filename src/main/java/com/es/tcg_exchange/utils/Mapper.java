package com.es.tcg_exchange.utils;

import com.es.tcg_exchange.dto.*;
import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.CartaModelo;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
public class Mapper {

    // ---------------- USUARIO ----------------
    // ---------------- entity/ies to DTO/s ----------------
    public static UsuarioDTO usuarioToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getUsername(),
                usuario.getRoles(),
                Mapper.cartasFisicasToDTO(usuario.getCartasFisicas())
        );
    }

    public static List<UsuarioDTO> usuariosToDTO(List<Usuario> usuarios) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();

        // Iterar sobre cada Usuario y convertirlo a UsuarioDTO
        usuarios.forEach(usuario -> {
            UsuarioDTO usuarioDTO = usuarioToDTO(usuario); // Utiliza el método de la propia clase para convertir
            usuariosDTO.add(usuarioDTO); // Agregar el DTO a la lista
        });

        return usuariosDTO; // Retorna la lista de DTOs
    }

    public static UsuarioLoginDTO usuarioToLoginDTO(Usuario u) {
    return new UsuarioLoginDTO(
            u.getUsername(),
            u.getPassword()
    );
    }

    public static UsuarioRegisterDTO usuarioToRegisterDTO(Usuario u) {
        return new UsuarioRegisterDTO(
                u.getUsername(),
                u.getPassword()
        );
    }

    // ---------------- DTO/s to entity/ies ----------------
//    public static Usuario usuarioDTOToModel(UsuarioDTO uDTO){
//        return new Usuario(
//                uDTO.getUsername(),
//                uDTO.getPassword(), // si programo null al final este atributo no se manda
//                uDTO.getRoles()
//                Mapper.DTOsToEntities(uDTO.getCartasFisicas())
//        );
//    }

    public static Usuario usuarioLoginDTOToModel(UsuarioLoginDTO ulDTO){
        return new Usuario(
                ulDTO.getUsername(),
                ulDTO.getPassword()
        );
    }

    public static Usuario usuarioRegisterDTOToModel(UsuarioRegisterDTO urDTO){
        return new Usuario(
                urDTO.getUsername(),
                urDTO.getPassword()
        );
    }

    // ---------------- CARTA FISICA ----------------
    // ---------------- entity/ies to DTO/s ----------------
    public static CartaFisicaDTO cartaFisicaToDTO(CartaFisica cartaFisica){

        return new CartaFisicaDTO(
                cartaFisica.getId(),
                cartaFisica.getEstadoCarta(),
                cartaFisica.getImagenUrl(),
                cartaFisica.getUsuario().getId(),
                cartaFisica.getCartaModelo().getId()
        );
    }

    public static List<CartaFisicaDTO> cartasFisicasToDTO(List<CartaFisica> cartasFisicas) {
        List<CartaFisicaDTO> cartasFisicasDTO = new ArrayList<>();

        cartasFisicas.forEach(cartaFisica -> {
            CartaFisicaDTO dto = cartaFisicaToDTO(cartaFisica); // Utiliza el método de la propia clase para convertir
            cartasFisicasDTO.add(dto); // Agregar el DTO a la lista
        });

        return cartasFisicasDTO; // Retorna la lista de DTOs
    }

    // ---------------- DTO/s to entity/ies ----------------
    // tengo que tener claro lo que necesito antes de seguir por ejemplo con este dto
    // lo que necesito pasarle, convertir, etc.
    // lo veré a medida que hago funciones de service y prosigo en la app

//    public static List<CartaFisica> DTOsToEntities(List<CartaFisicaDTO> cartasFisicasDTO) {
//        List<CartaFisica> cartasFisicas = new ArrayList<>();
//
//        cartasFisicasDTO.forEach(cartaFisicaDTO -> {
//            CartaFisica cartaFisica = DTOToEntity(cartaFisicaDTO); // Utiliza el método de la propia clase para convertir
//            cartasFisicas.add(cartaFisica); // Agregar el DTO a la lista
//        });
//
//        return cartasFisicas; // Retorna la lista de DTOs
//    }

//    public static CartaFisica DTOToEntity(CartaFisicaDTO cartaFisicaDTO){
//
//        return new CartaFisica(
//                cartaFisicaDTO.getId(),
//                cartaFisicaDTO.getEstadoCarta(),
//                cartaFisicaDTO.getImagenUrl(),
//                cartaFisicaDTO.getIdUsuario(),
//                cartaFisicaDTO.getIdCartaModelo()
//        );
//    }

    // ---------------- CARTA MODELO ----------------
    // ---------------- entity/ies to DTO/s ----------------
    public static CartaModeloDTO cartaModeloToDTO(CartaModelo cartaModelo) {
        return new CartaModeloDTO(
                cartaModelo.getId(),
                cartaModelo.getNombre(),
                cartaModelo.getTipoCarta(),
                cartaModelo.getRareza(),
                cartaModelo.getImagenUrl(),
                cartaModelo.getTipoPokemon(),
                cartaModelo.getEvolucion()
        );
    }

    // ---------------- DTO/s to entity/ies ----------------
    public static CartaModelo cartaModeloDTOToModel(CartaModeloDTO cartaModeloDTO) {
        // aqui lo hago con set en vez de con constructor para mostrar un mecanismo diferente simplemente
        CartaModelo cartaModelo = new CartaModelo();
        cartaModelo.setId(cartaModeloDTO.getId());
        cartaModelo.setNombre(cartaModeloDTO.getNombre());
        cartaModelo.setTipoCarta(cartaModeloDTO.getTipoCarta());
        cartaModelo.setRareza(cartaModeloDTO.getRareza());
        cartaModelo.setImagenUrl(cartaModeloDTO.getImagenUrl());
        cartaModelo.setTipoPokemon(cartaModeloDTO.getTipoPokemon());
        cartaModelo.setEvolucion(cartaModeloDTO.getEvolucion());
        return cartaModelo;
    }

    // ---------------- INTERCAMBIO ----------------
    // ---------------- entity/ies to DTO/s ----------------
    public static IntercambioDTO intercambioToDTO(Intercambio intercambio) {
        return new IntercambioDTO(
                intercambio.getUsuarioOrigen().getId(),
                intercambio.getUsuarioDestino().getId(),
                intercambio.getCartaOrigen().getId(),
                intercambio.getCartaDestino().getId(),
                intercambio.getEstado()
        );
    }

    // ---------------- DTO/s to entity/ies ----------------
    // lo mismo que en cartaFisica: hace falta un dto to entity de intercambio?

}
