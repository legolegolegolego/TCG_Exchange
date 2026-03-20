package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.DireccionCreateDTO;
import com.es.tcg_exchange.dto.DireccionDTO;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.DuplicateException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import com.es.tcg_exchange.model.Direccion;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.repository.DireccionRepository;
import com.es.tcg_exchange.repository.UsuarioRepository;
import com.es.tcg_exchange.utils.Mapper;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DireccionDTO create(
            DireccionCreateDTO createDTO,
            Authentication authentication
    ){
        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        Usuario usuario = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (direccionRepository.existsByUsuario(usuario)){
            throw new DuplicateException("El usuario ya tiene una dirección asociada");
        }

        // Validaciones
        validarDireccion(createDTO);

        // Creacion

        Direccion direccion = Mapper.direccionCreateDTOToModel(createDTO, usuario);
        direccionRepository.save(direccion);

        return Mapper.direccionToDTO(direccion);
    }

    public DireccionDTO update(
            DireccionCreateDTO createDTO,
            Authentication authentication
    ){
        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        Usuario usuario = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Direccion direccion = direccionRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));

        // Validaciones
        validarDireccion(createDTO);

        // ACTUALIZACIÓN
        direccion.setNombre(createDTO.nombre());
        direccion.setCalleYNumero(createDTO.calleYNumero());
        direccion.setPisoYPuerta(createDTO.pisoYPuerta());
        direccion.setCodigoPostal(createDTO.codigoPostal());
        direccion.setCiudad(createDTO.ciudad());
        direccion.setPais(createDTO.pais());

        direccion = direccionRepository.save(direccion);

        return Mapper.direccionToDTO(direccion);
    }



    private void validarDireccion(DireccionCreateDTO createDTO){
        // Validaciones esenciales direccion

        if (createDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        if (createDTO.nombre() == null || createDTO.nombre().isBlank()){
            throw new BadRequestException("El nombre asociado a la dirección es obligatorio");
        }

        if ( createDTO.calleYNumero() == null || createDTO.calleYNumero().isBlank()) {
            throw new BadRequestException("Calle y número son obligatorios");
        }

        // pisoypuerta puede ser null, no es obligatorio

        if ( createDTO.codigoPostal() == null || createDTO.codigoPostal().isBlank()) {
            throw new BadRequestException("El código postal es obligatorio");
        }

        if ( createDTO.ciudad() == null || createDTO.ciudad().isBlank()) {
            throw new BadRequestException("La ciudad es obligatoria");
        }

        if ( createDTO.pais() == null || createDTO.pais().isBlank()) {
            throw new BadRequestException("El país es obligatorio");
        }

        // Validaciones formato

        if (createDTO.codigoPostal().length() > 10){
            throw new BadRequestException("El código postal debe tener como máximo 10 caracteres");
        }
    }

    public DireccionDTO findByUsername(String username, Authentication authentication){

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario con username " + username + " no encontrado"));

        SecurityUtils.checkAdminOrSelf(authentication, username);

        Direccion direccion = direccionRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));

        return Mapper.direccionToDTO(direccion);
    }
}
