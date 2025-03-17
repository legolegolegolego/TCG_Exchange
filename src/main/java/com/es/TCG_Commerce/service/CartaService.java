package com.es.TCG_Commerce.service;

import com.es.TCG_Commerce.dto.CartaDTO;
import com.es.TCG_Commerce.error.exception.BadRequestException;
import com.es.TCG_Commerce.error.exception.DuplicateException;
import com.es.TCG_Commerce.error.exception.NotFoundException;
import com.es.TCG_Commerce.model.Carta;
import com.es.TCG_Commerce.model.Usuario;
import com.es.TCG_Commerce.repository.CartaRepository;
import com.es.TCG_Commerce.repository.UsuarioRepository;
import com.es.TCG_Commerce.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaService {

    @Autowired
    private CartaRepository cartaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // esto se he visto que se pone pero me he informado de que no hace falta
    // la clase es estática y sus métodos también, no hace falta inyectar nada para usarla
//    @Autowired
//    private Mapper mapper;

    public List<CartaDTO> getAll(){
        List<Carta>cartas = cartaRepository.findAll();
        return Mapper.cartasToDTOs(cartas);
    }

    public CartaDTO findById(Long id){

        if (id == null){
            throw new BadRequestException("El id no puede ser null");
        }

        Carta c = cartaRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Carta con id " + id + " no pudo ser encontrada"));

        return Mapper.entityToDTO(c);
    }

    public CartaDTO findByNombre(String nombre){
        if (nombre.isBlank()){
            throw new BadRequestException("El nombre no puede estar vacío");
        }
        Carta c = cartaRepository
                .findByNombre(nombre)
                .orElseThrow(() -> new NotFoundException("Carta con nombre " + nombre + " no pudo ser encontrada"));

        return Mapper.entityToDTO(c);
    }

    public CartaDTO insert(CartaDTO cdto){
        if (cartaRepository.findById(cdto.getId()).isPresent()){
            throw new DuplicateException("Error. El id de la carta ya existe en otra.");
        }
        if (cartaRepository.findByNombre(cdto.getNombre()).isPresent()){
            throw new DuplicateException("Ese nombre ya lo tiene otra carta");
        }
        if (!(cdto.getTipo().equals("FUEGO")  || cdto.getTipo().equals("PLANTA")  || cdto.getTipo().equals("AGUA")  ||
                cdto.getTipo().equals("RAYO")  || cdto.getTipo().equals("PSIQUICO")  || cdto.getTipo().equals("LUCHA")  ||
                cdto.getTipo().equals("OSCURO")  || cdto.getTipo().equals("DRAGON")  || cdto.getTipo().equals("METALICO")  ||
                cdto.getTipo().equals("INCOLORO") )){
            throw new BadRequestException("No has introducido un tipo válido");
        }
        if (cdto.getVida() < 10 || cdto.getVida() > 300){
            throw new BadRequestException("No has introducido una vida válida");
        }
        if (cdto.getAtaque() < 10 || cdto.getAtaque() > 300){
            throw new BadRequestException("No has introducido un ataque válida");
        }
        if (cdto.getUsername() != null){
            throw new BadRequestException("Una nueva carta no puede tener un usuario asignado, tiene que comprarla");
        }

        cartaRepository.save(Mapper.DTOToEntity(cdto));

        return cdto;
    }

    public CartaDTO update(Long id, CartaDTO cdto){

        Carta c = cartaRepository.findById(id).orElseThrow(()
                -> new NotFoundException("La carta no está en la bd"));

        // si el id de la carta que se quiere cambiar es distinto del que se pasa por json:
        // esto permite que se actualice una carta con el mismo id sin cambiarlo
        if (id != cdto.getId()){
            if (cartaRepository.findById(cdto.getId()).isPresent()){
                throw new DuplicateException("Error. El id de la carta ya existe en  otra.");
            }
        }

        // &&... para no compararse con ella misma, permitir el mismo nombre si ya es suyo
        if (cartaRepository.findByNombre(cdto.getNombre()).isPresent() && !c.getNombre().equals(cdto.getNombre())){
            throw new DuplicateException("Ese nombre ya lo tiene otra carta");
        }
        if (!(cdto.getTipo().equals("FUEGO")  || cdto.getTipo().equals("PLANTA")  || cdto.getTipo().equals("AGUA")  ||
                cdto.getTipo().equals("RAYO")  || cdto.getTipo().equals("PSIQUICO")  || cdto.getTipo().equals("LUCHA")  ||
                cdto.getTipo().equals("OSCURO")  || cdto.getTipo().equals("DRAGON")  || cdto.getTipo().equals("METALICO")  ||
                cdto.getTipo().equals("INCOLORO") )){
            throw new BadRequestException("No has introducido un tipo válido");
        }
        if (cdto.getVida() < 10 || cdto.getVida() > 300){
            throw new BadRequestException("No has introducido una vida válida");
        }
        if (cdto.getAtaque() < 10 || cdto.getAtaque() > 300){
            throw new BadRequestException("No has introducido un ataque válida");
        }
        if (cdto.getUsername() != null){
            if (!cartaRepository.findByNombre(Mapper.DTOToEntity(cdto).getNombre()).isPresent()){
                throw new BadRequestException("El usuario de la carta no está en la base de datos");
            }
        }




        c.setNombre(cdto.getNombre());
        c.setTipo(cdto.getTipo());
        c.setVida(cdto.getVida());
        c.setAtaque(cdto.getAtaque());


        if (cdto.getUsername() != null){ // si mandan el nombre:
            Usuario u = usuarioRepository.findByUsername(cdto.getUsername()).orElseThrow(()
                    -> new NotFoundException("El usuario no se encuentra en la BD"));
            c.setUsuario(u);

        }

        cartaRepository.save(c);

        return cdto;
    }

    public CartaDTO delete(Long id){
        Carta c = cartaRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Carta inexistente en la bd"));

        CartaDTO cdto = Mapper.entityToDTO(c);

        cartaRepository.delete(c);

        return cdto;

    }


}
