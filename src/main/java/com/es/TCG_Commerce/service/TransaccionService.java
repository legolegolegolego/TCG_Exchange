package com.es.TCG_Commerce.service;

import com.es.TCG_Commerce.dto.TransaccionDTO;
import com.es.TCG_Commerce.error.exception.BadRequestException;
import com.es.TCG_Commerce.error.exception.DuplicateException;
import com.es.TCG_Commerce.error.exception.NotFoundException;
import com.es.TCG_Commerce.model.Carta;
import com.es.TCG_Commerce.model.Transaccion;
import com.es.TCG_Commerce.model.Usuario;
import com.es.TCG_Commerce.repository.CartaRepository;
import com.es.TCG_Commerce.repository.TransaccionRepository;
import com.es.TCG_Commerce.repository.UsuarioRepository;
import com.es.TCG_Commerce.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaRepository cartaRepository;

    public TransaccionDTO findById(Long id){

        if (id == null){
            throw new BadRequestException("El id no puede ser null");
        }

        Transaccion t = transaccionRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Transacción con id " + id + " no pudo ser encontrada"));

        return Mapper.entityToDTO(t);
    }

    public TransaccionDTO insert(TransaccionDTO tdto){

        if (tdto.getPrecio() <= 0){
            throw new BadRequestException("El precio debe ser mayor a 0");
        }

        // Comprobar si existen los usuarios y carta de la transaccion que se crea

        Usuario comprador = usuarioRepository.findByUsername(tdto.getComprador().getUsername()).orElseThrow(()
                -> new NotFoundException("El usuario comprador no existe en la BD"));

        Usuario vendedor = usuarioRepository.findByUsername(tdto.getVendedor().getUsername()).orElseThrow(()
                -> new NotFoundException("El usuario vendedor no existe en la BD"));

        // Asegurar que el comprador no sea el mismo usuario que el vendedor
        if (comprador.getId() == vendedor.getId()){
            throw new BadRequestException("El usuario comprador no puede ser el usuario vendedor");
        }

        Carta carta = cartaRepository.findById(tdto.getCarta().getId()).orElseThrow(()
                -> new NotFoundException("La carta no está en la BD"));

        transaccionRepository.save(Mapper.DTOToEntity(tdto));

        return tdto;
    }
    public TransaccionDTO update(Long id, TransaccionDTO tdto){

        if (tdto.getPrecio() <= 0){
            throw new BadRequestException("El precio debe ser mayor a 0");
        }

        // Comprobar si existen los usuarios y carta de la transaccion que se crea

        Usuario comprador = usuarioRepository.findByUsername(tdto.getComprador().getUsername()).orElseThrow(()
                -> new NotFoundException("El usuario comprador no existe en la BD"));

        Usuario vendedor = usuarioRepository.findByUsername(tdto.getVendedor().getUsername()).orElseThrow(()
                -> new NotFoundException("El usuario vendedor no existe en la BD"));

        // Asegurar que el comprador no sea el mismo usuario que el vendedor
        if (comprador.getId() == vendedor.getId()){
            throw new BadRequestException("El usuario comprador no puede ser el usuario vendedor");
        }

        Carta carta = cartaRepository.findById(tdto.getCarta().getId()).orElseThrow(()
                -> new NotFoundException("La carta no está en la BD"));


        Transaccion t = transaccionRepository.findById(id).orElseThrow(()
                -> new NotFoundException("No se encuentra la transacción que quiere actualizar"));

        // se puede hacer asi o con el Mapper
        t.setPrecio(tdto.getPrecio());
        t.setComprador(comprador);
        t.setVendedor(vendedor);
        t.setCarta(carta);
        transaccionRepository.save(t);

        return tdto;
    }

    public TransaccionDTO delete(Long id){

        Transaccion t = transaccionRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Transaccion inexistente en la bd"));

        TransaccionDTO tdto = Mapper.entityToDTO(t);

        transaccionRepository.delete(t);

        return tdto;

    }
}
