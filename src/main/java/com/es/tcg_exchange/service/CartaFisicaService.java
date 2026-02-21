package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.CartaFisicaCreateDTO;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.ForbiddenException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import com.es.tcg_exchange.model.CartaModelo;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import com.es.tcg_exchange.repository.CartaFisicaRepository;
import com.es.tcg_exchange.repository.CartaModeloRepository;
import com.es.tcg_exchange.repository.IntercambioRepository;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.es.tcg_exchange.dto.CartaFisicaDTO;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.repository.UsuarioRepository;
import com.es.tcg_exchange.utils.Mapper;
import org.springframework.security.core.Authentication;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class CartaFisicaService {

    @Autowired
    private CartaFisicaRepository cfRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartaModeloRepository cmRepository;

    @Autowired
    private IntercambioRepository intercambioRepository;

    /**
     * Obtener cartas físicas disponibles de un usuario
     */
    public List<CartaFisicaDTO> findDisponiblesByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario con nombre " + username + " no encontrado"));

        List<CartaFisica> cartas = cfRepository.findByUsuarioIdAndDisponible(usuario.getId(), true);

        return Mapper.cartasFisicasToDTO(cartas);
    }

    /**
     * Obtener cartas físicas NO disponibles de un usuario
     * Solo accesible por el propio usuario o admin
     */
    public List<CartaFisicaDTO> findNoDisponiblesByUsername(String username, Authentication authentication) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario con nombre " + username + " no encontrado"));

        // Check permisos: admin o propio usuario
        SecurityUtils.checkAdminOrSelf(authentication, username);

        List<CartaFisica> cartas = cfRepository.findByUsuarioIdAndDisponible(usuario.getId(), false);

        return Mapper.cartasFisicasToDTO(cartas);
    }

    /**
     * Obtener carta física por id
     * @param id
     * @param authentication Si disponible = false, solo admin o propio usuario autenticado
     * @return
     */
    public CartaFisicaDTO findById(Long id, Authentication authentication) {
        CartaFisica carta = cfRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta física con id " + id + " no encontrada"));

        // Si la carta NO está disponible, se requiere autenticación
        if (!carta.isDisponible()) {

            if (authentication == null) {
                // Usuario no autenticado
                throw new UnauthorizedException("Necesitas estar autenticado para ver esta carta");
            }

            // Comprobamos permisos: admin o propietario de la carta
            String usernamePropietario = carta.getUsuario().getUsername();
            SecurityUtils.checkAdminOrSelf(authentication, usernamePropietario);
        }

        return Mapper.cartaFisicaToDTO(carta);
    }

    /**
     * Crear una nueva carta física
     * @param cartaParaCrear
     * @param authentication
     * @return CartaFisicaDTO (carta modelo creada mapeada a dto)
     */
    public CartaFisicaDTO create(
            CartaFisicaCreateDTO cartaParaCrear,
            Authentication authentication) {

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        if (cartaParaCrear.idCartaModelo() == null) {
            throw new BadRequestException("El id de carta modelo no puede ser null");
        }

        if (cartaParaCrear.estadoCarta() == null) {
            throw new BadRequestException("El estado de la carta es obligatorio");
        }

        if (cartaParaCrear.imagenUrl() == null || cartaParaCrear.imagenUrl().isBlank()) {
            throw new BadRequestException("La imagen es obligatoria");
        }

        // Validar formato de URL
        try {
            new URL(cartaParaCrear.imagenUrl());
        } catch (MalformedURLException e) {
            throw new BadRequestException("La URL de la imagen no es válida");
        }

        // Validar extensión de imagen
        /*
        Funciona para ambos casos:
        https://img.com/foto.png
        https://img.com/foto.png?size=large
         */
        if (!cartaParaCrear.imagenUrl().toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)(\\?.*)?$")) {
            throw new BadRequestException("La imagen debe tener formato válido (jpg, jpeg, png, webp)");
        }

        // Obtener usuario autenticado
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Obtener carta modelo
        CartaModelo modelo = cmRepository.findById(cartaParaCrear.idCartaModelo())
                .orElseThrow(() -> new NotFoundException(
                        "Carta modelo con id " + cartaParaCrear.idCartaModelo() + " no encontrada"
                ));

        if (!modelo.isActivo()){
            throw new BadRequestException("No se puede enlazar a una carta modelo inactiva");
        }

        // Crear entidad
        CartaFisica carta = new CartaFisica();
        carta.setCartaModelo(modelo);
        carta.setEstadoCarta(cartaParaCrear.estadoCarta());
        carta.setImagenUrl(cartaParaCrear.imagenUrl());
        carta.setUsuario(usuario);

        cfRepository.save(carta);

        return Mapper.cartaFisicaToDTO(carta);
    }

    public CartaFisicaDTO update(
            Long id,
            CartaFisicaCreateDTO createDTO,
            Authentication authentication) {

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado");
        }

        if (id == null) {
            throw new BadRequestException("El id no puede ser null");
        }

        if (createDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        // Buscar carta
        CartaFisica carta = cfRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Carta física con id " + id + " no encontrada"
                ));

        // Permisos: solo propietario
        String username = authentication.getName();
        if (!carta.getUsuario().getUsername().equals(username)) {
            throw new ForbiddenException("No tienes permisos para modificar esta carta");
        }

        // ===== VALIDACIONES =====

        if (!carta.isDisponible()) {
            throw new BadRequestException("No se puede actualizar una carta no disponible");
        }

        if (createDTO.estadoCarta() == null) {
            throw new BadRequestException("El estado de la carta es obligatorio");
        }

        if (createDTO.imagenUrl() == null || createDTO.imagenUrl().isBlank()) {
            throw new BadRequestException("La imagen es obligatoria");
        }
        try {
            new URL(createDTO.imagenUrl());
        } catch (MalformedURLException e) {
            throw new BadRequestException("La URL de la imagen no es válida");
        }
        if (!createDTO.imagenUrl().toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)(\\?.*)?$")) {
            throw new BadRequestException("La imagen debe tener formato válido (jpg, jpeg, png, webp)");
        }

        if (createDTO.idCartaModelo() == null) {
            throw new BadRequestException("El id de carta modelo no puede ser null");
        }

        CartaModelo modelo = cmRepository.findById(createDTO.idCartaModelo())
                .orElseThrow(() -> new NotFoundException(
                        "Carta modelo con id " + createDTO.idCartaModelo() + " no encontrada"
                ));

        if (!modelo.isActivo()) {
            throw new BadRequestException("No se puede asociar a una carta modelo inactiva");
        }

        // ===== ACTUALIZACIÓN =====

        carta.setEstadoCarta(createDTO.estadoCarta());
        carta.setImagenUrl(createDTO.imagenUrl());
        carta.setCartaModelo(modelo);

        cfRepository.save(carta);

        return Mapper.cartaFisicaToDTO(carta);
    }

    public void delete(Long id, Authentication authentication) {

        if (id == null) {
            throw new BadRequestException("El id no puede ser null");
        }

        // Buscar la carta física
        CartaFisica carta = cfRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Carta física con id " + id + " no encontrada"
                ));

        if (authentication == null) {
            throw new UnauthorizedException("Debes estar autenticado para borrar la carta");
        }

        // Verificar permisos: propietario o admin
        SecurityUtils.checkAdminOrSelf(authentication, carta.getUsuario().getUsername());

        // Obtener todos los intercambios asociados a la carta
        List<Intercambio> intercambios = intercambioRepository.findByCartaOrigenOrCartaDestino(carta, carta);

        // Si tiene intercambios:
        if (!intercambios.isEmpty()) {
            // Rechazar automáticamente los intercambios pendientes
            intercambios.stream()
                    .filter(i -> i.getEstado() == EstadoIntercambio.PENDIENTE)
                    .forEach(i -> {
                        i.setEstado(EstadoIntercambio.RECHAZADO);
                        intercambioRepository.save(i);
                    });

            // Marcar la carta como no disponible
            carta.setDisponible(false);
            cfRepository.save(carta);


            return; // No se elimina físicamente
        }

        // Si no hay intercambios asociados, se puede borrar físicamente
        cfRepository.delete(carta);
    }
}
