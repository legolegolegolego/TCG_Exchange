package com.es.TCG_Commerce.service;

import com.es.TCG_Commerce.dto.IntercambioDTO;
import com.es.TCG_Commerce.error.exception.BadRequestException;
import com.es.TCG_Commerce.error.exception.NotFoundException;
import com.es.TCG_Commerce.model.Carta;
import com.es.TCG_Commerce.model.Intercambio;
import com.es.TCG_Commerce.model.Usuario;
import com.es.TCG_Commerce.repository.CartaRepository;
import com.es.TCG_Commerce.repository.IntercambioRepository;
import com.es.TCG_Commerce.repository.UsuarioRepository;
import com.es.TCG_Commerce.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntercambioService {

    @Autowired
    private IntercambioRepository intercambioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaRepository cartaRepository;

    public IntercambioDTO findById(Long id){

        if (id == null){
            throw new BadRequestException("El id no puede ser null");
        }

        Intercambio t = intercambioRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Transacción con id " + id + " no pudo ser encontrada"));

        return Mapper.entityToDTO(t);
    }

    public IntercambioDTO insert(IntercambioDTO tdto){

//        if (tdto.getPrecio() <= 0){
//            throw new BadRequestException("El precio debe ser mayor a 0");
//        }

        // Comprobar si existen los usuarios y carta de la transaccion que se crea

        Usuario usuarioA = usuarioRepository.findById(tdto.getUsuarioDestino()).orElseThrow(()
                -> new NotFoundException("El usuario A no existe en la BD"));

        Usuario usuarioB = usuarioRepository.findById(tdto.getUsuarioOrigen()).orElseThrow(()
                -> new NotFoundException("El usuario B no existe en la BD"));

        // Asegurar que el usuarioA no sea el mismo usuario que el usuarioB
        if (usuarioA.getId() == usuarioB.getId()){
            throw new BadRequestException("El usuario A no puede ser el usuario B");
        }

        Carta carta = cartaRepository.findById(tdto.getCarta()).orElseThrow(()
                -> new NotFoundException("La carta no está en la BD"));

        intercambioRepository.save(new Intercambio(usuarioB, usuarioA, cartaOrigen, cartaDestino));

        return tdto;
    }
    public IntercambioDTO update(Long id, IntercambioDTO tdto){

//        if (tdto.getPrecio() <= 0){
//            throw new BadRequestException("El precio debe ser mayor a 0");
//        }

        // Comprobar si existen los usuarios y carta de la transaccion que se crea

        Usuario usuarioA = usuarioRepository.findById(tdto.getUsuarioDestino()).orElseThrow(()
                -> new NotFoundException("El usuario A no existe en la BD"));

        Usuario usuarioB = usuarioRepository.findById(tdto.getUsuarioOrigen()).orElseThrow(()
                -> new NotFoundException("El usuario B no existe en la BD"));

        // Asegurar que el usuarioA no sea el mismo que el usuarioB
        if (usuarioA.getId() == usuarioB.getId()){
            throw new BadRequestException("El usuario usuarioA no puede ser el usuario usuarioB");
        }

        Carta carta = cartaRepository.findById(tdto.getCarta()).orElseThrow(()
                -> new NotFoundException("La carta no está en la BD"));


        Intercambio t = intercambioRepository.findById(id).orElseThrow(()
                -> new NotFoundException("No se encuentra la transacción que quiere actualizar"));

        // se puede hacer asi o con el Mapper
        t.setUsuarioDestino(usuarioA);
        t.setUsuarioOrigen(usuarioB);
        t.setCarta(carta);
        intercambioRepository.save(t);

        return tdto;
    }

    public IntercambioDTO delete(Long id){

        Intercambio t = intercambioRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Transaccion inexistente en la bd"));

        IntercambioDTO tdto = Mapper.entityToDTO(t);

        intercambioRepository.delete(t);

        return tdto;

    }
}
