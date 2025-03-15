package com.es.TCG_Commerce.utils;

import com.es.TCG_Commerce.dto.*;
import com.es.TCG_Commerce.model.Carta;
import com.es.TCG_Commerce.model.Transaccion;
import com.es.TCG_Commerce.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

    public static UsuarioDTO entityToDTO(Usuario u) {
        return new UsuarioDTO(
                u.getUsername(),
                u.getPassword(),
                u.getRoles()
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
                u.getRoles()
        );
    }

    public static Usuario DTOToEntity(UsuarioDTO uDTO){
        return new Usuario(
                uDTO.getUsername(),
                uDTO.getPassword(),
                uDTO.getRoles()
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
                urDTO.getRoles()
        );
    }

    public static CartaDTO entityToDTO(Carta c){

        // tengo que mapear antes la lista de usuarios, puesto que en cartaDTO son UsuariosDTO:
//        List<UsuarioDTO> uDTOs = new ArrayList<>();
//        for (Usuario u : c.getVendedores()) {
//            uDTOs.add(this.entityToDTO(u));
//        }

        return new CartaDTO(
                c.getId(),
                c.getNombre(),
                c.getTipo(),
                c.getVida(),
                c.getAtaque(),
                Mapper.entityToDTO(c.getUsuario())
        );
    }

    public static Carta DTOToEntity(CartaDTO cDTO){

/*        List<Usuario> us = new ArrayList<>();
        for (UsuarioDTO uDTO : cDTO.getVendedores()) {
            us.add(this.DTOToEntity(uDTO));
        }*/

        return new Carta(
                cDTO.getId(),
                cDTO.getNombre(),
                cDTO.getTipo(),
                cDTO.getVida(),
                cDTO.getAtaque(),
                Mapper.DTOToEntity(cDTO.getUsuarioDTO())
        );
    }

    public static TransaccionDTO entityToDTO(Transaccion t) {
        return new TransaccionDTO(
                t.getPrecio(),
                Mapper.entityToDTO(t.getVendedor()),
                Mapper.entityToDTO(t.getComprador()),
                Mapper.entityToDTO(t.getCarta())
        );
    }

    public static Transaccion DTOToEntity(TransaccionDTO tDTO) {
        return new Transaccion(
                tDTO.getPrecio(),
                Mapper.DTOToEntity(tDTO.getVendedor()),
                Mapper.DTOToEntity(tDTO.getComprador()),
                Mapper.DTOToEntity(tDTO.getCarta())
        );
    }


}
