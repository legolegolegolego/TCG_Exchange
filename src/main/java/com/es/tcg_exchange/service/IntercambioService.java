package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.IntercambioCreateDTO;
import com.es.tcg_exchange.dto.IntercambioDTO;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.ForbiddenException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import com.es.tcg_exchange.repository.CartaFisicaRepository;
import com.es.tcg_exchange.repository.IntercambioRepository;
import com.es.tcg_exchange.repository.UsuarioRepository;
import com.es.tcg_exchange.utils.Mapper;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntercambioService {

    @Autowired
    private IntercambioRepository intercambioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaFisicaRepository cfRepository;

    /**
     * Obtener intercambios de un usuario, opcionalmente filtrando por estado
     * @param username usuario a consultar
     * @param estado estado de los intercambios (opcional)
     * @param auth autenticación del usuario que hace la petición
     * @return lista de intercambios
     */
    public List<IntercambioDTO> getIntercambiosByUsuario(String username, EstadoIntercambio estado, Authentication auth) {

        // Verificar que el usuario existe
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario con username " + username + " no encontrado"));

        // Verificar permisos: admin o el propio usuario
        SecurityUtils.checkAdminOrSelf(auth, username);

        List<Intercambio> intercambios;

        if (estado != null) {
            // Filtrar por estado si se proporcionó
            intercambios = intercambioRepository.findByUsuarioAndEstado(usuario.getId(), estado);
        } else {
            // De lo contrario, devolver todos los intercambios del usuario
            intercambios = intercambioRepository.findByUsuario(usuario.getId());
        }

        return intercambios.stream()
                .map(Mapper::intercambioToDTO)
                .toList();
    }

    public IntercambioDTO findById(Long id, Authentication authentication) {
        Intercambio intercambio = intercambioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Intercambio con id " + id + " no encontrado"));

        // Validar permisos
        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado para acceder a este recurso");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isParticipante = intercambio.getUsuarioOrigen().getUsername().equals(authentication.getName()) ||
                intercambio.getUsuarioDestino().getUsername().equals(authentication.getName());

        if (!isAdmin && !isParticipante) {
            throw new ForbiddenException("No tienes los permisos para acceder al recurso");
        }

        return Mapper.intercambioToDTO(intercambio);
    }

    public IntercambioDTO create(IntercambioCreateDTO dto, Authentication authentication) {

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado para crear un intercambio");
        }

        if (dto.cartaOrigenId() == null || dto.cartaDestinoId() == null) {
            throw new BadRequestException("Ambos IDs de cartas son obligatorios");
        }

        // Obtener usuario autenticado
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Obtener cartas
        CartaFisica cartaOrigen = cfRepository.findById(dto.cartaOrigenId())
                .orElseThrow(() -> new NotFoundException("Carta origen no encontrada"));

        CartaFisica cartaDestino = cfRepository.findById(dto.cartaDestinoId())
                .orElseThrow(() -> new NotFoundException("Carta destino no encontrada"));

        // Validar propiedad de la cartaOrigen
        if (!cartaOrigen.getUsuario().getId().equals(usuario.getId())) {
            throw new ForbiddenException("Solo puedes ofrecer tus propias cartas");
        }

        // Validar disponibilidad
        if (!cartaOrigen.isDisponible()) {
            throw new BadRequestException("La carta origen no está disponible para intercambio");
        }
        if (!cartaDestino.isDisponible()) {
            throw new BadRequestException("La carta destino no está disponible para intercambio");
        }

        // Crear intercambio
        Intercambio intercambio = new Intercambio();
        intercambio.setUsuarioOrigen(usuario);
        intercambio.setUsuarioDestino(cartaDestino.getUsuario());
        intercambio.setCartaOrigen(cartaOrigen);
        intercambio.setCartaDestino(cartaDestino);
        intercambio.setEstado(EstadoIntercambio.PENDIENTE);

        intercambioRepository.save(intercambio);

        return Mapper.intercambioToDTO(intercambio);
    }

    public IntercambioDTO aceptarIntercambio(Long id, Authentication authentication) {

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        Intercambio intercambio = intercambioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Intercambio con id " + id + " no encontrado"));

        String username = authentication.getName();

        // Solo el destinatario puede aceptar
        if (!intercambio.getUsuarioDestino().getUsername().equals(username)) {
            throw new ForbiddenException("Solo el destinatario puede aceptar este intercambio");
        }

        // Actualizar estado
        intercambio.setEstado(EstadoIntercambio.ACEPTADO);

        // Marcar cartas como no disponibles
        intercambio.getCartaOrigen().setDisponible(false);
        intercambio.getCartaDestino().setDisponible(false);
        cfRepository.save(intercambio.getCartaOrigen());
        cfRepository.save(intercambio.getCartaDestino());

        // Rechazar automáticamente otros intercambios pendientes que involucren estas cartas
        List<Intercambio> otrosPendientes = intercambioRepository.findByCartaOrigenIdAndEstado(intercambio.getCartaOrigen().getId(), EstadoIntercambio.PENDIENTE);
        otrosPendientes.addAll(intercambioRepository.findByCartaDestinoIdAndEstado(intercambio.getCartaOrigen().getId(), EstadoIntercambio.PENDIENTE));
        otrosPendientes.addAll(intercambioRepository.findByCartaOrigenIdAndEstado(intercambio.getCartaDestino().getId(), EstadoIntercambio.PENDIENTE));
        otrosPendientes.addAll(intercambioRepository.findByCartaDestinoIdAndEstado(intercambio.getCartaDestino().getId(), EstadoIntercambio.PENDIENTE));

        for (Intercambio otro : otrosPendientes) {
            otro.setEstado(EstadoIntercambio.RECHAZADO);
        }
        intercambioRepository.saveAll(otrosPendientes);

        intercambioRepository.save(intercambio);

        return Mapper.intercambioToDTO(intercambio);
    }

    public IntercambioDTO rechazarIntercambio(Long id, Authentication authentication) {

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        Intercambio intercambio = intercambioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Intercambio con id " + id + " no encontrado"));

        String username = authentication.getName();

        // Solo el destinatario puede rechazar
        if (!intercambio.getUsuarioDestino().getUsername().equals(username)) {
            throw new ForbiddenException("Solo el destinatario puede rechazar este intercambio");
        }

        // Actualizar estado
        intercambio.setEstado(EstadoIntercambio.RECHAZADO);

        intercambioRepository.save(intercambio);

        return Mapper.intercambioToDTO(intercambio);
    }

}
