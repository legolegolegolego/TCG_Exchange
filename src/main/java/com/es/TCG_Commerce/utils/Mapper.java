package com.es.TCG_Commerce.utils;

import com.es.TCG_Commerce.dto.CartaDTO;
import com.es.TCG_Commerce.dto.UsuarioDTO;
import com.es.TCG_Commerce.model.Carta;
import com.es.TCG_Commerce.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public UsuarioDTO entityToDTO(Usuario u) {
        return new UsuarioDTO(
                u.getUsername(),
                u.getPassword(),
                u.getRoles()
        );
    }

    public Usuario DTOToEntity(UsuarioDTO uDTO){
        return new Usuario(
                uDTO.getUsername(),
                uDTO.getPassword(),
                uDTO.getRoles()
        );
    }

    public CartaDTO entityToDTO(Carta c){
        return new CartaDTO(
                c.getNombre(),
                c.getTipo(),
                c.getVida(),
                c.getAtaque(),
                c.getVendedores()
        );
    }

    public Carta DTOToEntity(CartaDTO cDTO){
        return new Carta(
                cDTO.getId,
                cDTO.getNombre(),
                cDTO.getTipo(),
                cDTO.getVida(),
                cDTO.getAtaque(),
                cDTO.getVendedores()
        );
    }

}
