package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.CartaModeloDTO;
import com.es.tcg_exchange.dto.UsuarioDTO;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.DuplicateException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.CartaModelo;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.*;
import com.es.tcg_exchange.repository.CartaFisicaRepository;
import com.es.tcg_exchange.repository.CartaModeloRepository;
import com.es.tcg_exchange.repository.IntercambioRepository;
import com.es.tcg_exchange.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class CartaModeloService {

    @Autowired
    private CartaModeloRepository cmRepository;

    @Autowired
    private CartaFisicaRepository cfRepository;

    @Autowired
    private IntercambioRepository intercambioRepository;

    /**
     * Buscar cartas modelo por filtros (opcionales)
     * @param numeroMin
     * @param numeroMax
     * @param nombre
     * @param tipoCarta
     * @param rareza
     * @param tipoPokemon
     * @param evolucion
     * @return dto de cartaModelo
     */
    public Page<CartaModeloDTO> findAll(
            Long numeroMin,
            Long numeroMax,
            String nombre,
            TipoCarta tipoCarta,
            Rareza rareza,
            TipoPokemon tipoPokemon,
            EtapaEvolucion evolucion,
            Pageable pageable,
            Authentication authentication
    ) {

        /* versión 1

        List<CartaModelo> cartas = cmRepository.findAll();

        return cartas.stream()

                .filter(c -> numeroMin == null || c.getNumero() >= numeroMin)
                .filter(c -> numeroMax == null || c.getNumero() <= numeroMax)
                .filter(c -> tipoCarta == null || c.getTipoCarta() == tipoCarta)
                .filter(c -> rareza == null || c.getRareza() == rareza)
                .filter(c -> tipoPokemon == null ||
                        (c.getTipoPokemon() != null && c.getTipoPokemon() == tipoPokemon))
                .filter(c -> evolucion == null ||
                        (c.getEvolucion() != null && c.getEvolucion() == evolucion))

                .map(Mapper::cartaModeloToDTO)
                .toList();

         */

        // versión 2 (profesional)

        if (numeroMin != null && numeroMax != null && numeroMin > numeroMax) {
            throw new BadRequestException("numeroMin no puede ser mayor que numeroMax");
        }

        if (pageable.getPageSize() > 50) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    50,
                    pageable.getSort()
            );
        }

        Specification<CartaModelo> spec = Specification.where(null);

        if (numeroMin != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("numero"), numeroMin));
        }

        if (numeroMax != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("numero"), numeroMax));
        }

        if (nombre != null && !nombre.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(
                            cb.lower(root.get("nombre")),
                            "%" + nombre.toLowerCase() + "%"
                    ));
        }

        if (tipoCarta != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("tipoCarta"), tipoCarta));
        }

        if (rareza != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("rareza"), rareza));
        }

        if (tipoPokemon != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("tipoPokemon"), tipoPokemon));
        }

        if (evolucion != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("evolucion"), evolucion));
        }

        boolean isAdmin = authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // si no es ADMIN solo coge las cartas activas
        if (!isAdmin) {
            spec = spec.and((root, query, cb) ->
                    cb.isTrue(root.get("activo")));
        }

        return cmRepository.findAll(spec, pageable)
                .map(Mapper::cartaModeloToDTO);
    }

    /**
     * Obtener cartaModelo por id
     * @param id
     * @return dto de cartaModelo
     */
    public CartaModeloDTO findById(Long id, Authentication authentication) {

        /*
            Si el id viene por path (/{id}), Spring nunca enviará null.

            Si no se envía id → no matchea la ruta.

            Si no es numérico → lanza MethodArgumentTypeMismatchException.

            Asi que, innecesario el if:
         */

//        if (id == null) {
//            throw new BadRequestException("El id no puede ser null");
//        }

        boolean isAdmin = authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        CartaModelo cartaModelo = cmRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Carta modelo con id " + id + " no encontrada"));

        // Devuelvo 404 en lugar de 403 para no revelar que existe a un usuario no ADMIN
        if (!cartaModelo.isActivo() && !isAdmin) {
            throw new NotFoundException("Carta no encontrada");
        }

        return Mapper.cartaModeloToDTO(cartaModelo);
    }

    public List<UsuarioDTO> findUsuariosConCartaModelo(Long cartaModeloId) {

        if (cartaModeloId == null) {
            throw new BadRequestException("El id de la carta modelo no puede ser null");
        }

        // Verificar que existe la carta modelo
        if (!cmRepository.existsById(cartaModeloId)) {
            throw new NotFoundException("Carta modelo con id " + cartaModeloId + " no encontrada");
        }

        // Obtener usuarios
        List<Usuario> usuarios = cfRepository.findUsuariosConCartaModelo(cartaModeloId);

        // Mapear a DTO (solo id y username)
        return usuarios.stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getUsername()))
                .toList();
    }

    public CartaModeloDTO create(CartaModeloDTO dto) {

        if (dto == null) {
            throw new BadRequestException("El cuerpo de la petición no puede ser null");
        }

        // Validar numero carta
        if (dto.getNumero() == null) {
            throw new BadRequestException("El número oficial es obligatorio");
        }
        if (cmRepository.existsByNumero(dto.getNumero())) {
            throw new DuplicateException("Ya existe una carta modelo con número " + dto.getNumero());
        }

        // Validar nombre
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new BadRequestException("El nombre de la carta modelo es obligatorio");
        }

        // Validar tipoCarta
        if (dto.getTipoCarta() == null) {
            throw new BadRequestException("El tipo de carta es obligatorio");
        }
        // se controla automaticamente en cada enum
//        if (!(dto.getTipoCarta() == TipoCarta.POKEMON || dto.getTipoCarta() == TipoCarta.ENTRENADOR)) {
//            throw new BadRequestException("Tipo de carta inválido. Valores permitidos: POKEMON, ENTRENADOR");
//        }

        // Validar rareza
        if (dto.getRareza() == null) {
            throw new BadRequestException("La rareza es obligatoria");
        }

        // Validar imagenUrl
        if (dto.getImagenUrl() == null || dto.getImagenUrl().isBlank()) {
            throw new BadRequestException("La imagen de la carta es obligatoria");
        }
        // Validar formato de URL
        try {
            new URL(dto.getImagenUrl());
        } catch (MalformedURLException e) {
            throw new BadRequestException("La URL de la imagen no es válida");
        }
        // en carta modelo no valida extensión porque al consumir de api externa puede no corresponder a formatos típicos


        // Validar campos específicos de POKEMON
        if (dto.getTipoCarta() == TipoCarta.POKEMON) {
            if (dto.getTipoPokemon() == null) {
                throw new BadRequestException("El tipo de Pokémon es obligatorio para cartas Pokémon");
            }
            if (dto.getEvolucion() == null) {
                throw new BadRequestException("La etapa de evolución es obligatoria para cartas Pokémon");
            }
        } else {
            // Otros tipos no deberían tener estos campos
            dto.setTipoPokemon(null);
            dto.setEvolucion(null);
        }

        // Guardar en base de datos
        CartaModelo entity = Mapper.cartaModeloDTOToModel(dto);
        CartaModelo saved = cmRepository.save(entity);

        // Devolver DTO persistido
        return Mapper.cartaModeloToDTO(saved);
    }

    public CartaModeloDTO update(Long id, CartaModeloDTO dto) {

        if (id == null) {
            throw new BadRequestException("El id del path es obligatorio");
        }

        if (dto == null) {
            throw new BadRequestException("El cuerpo de la petición no puede ser null");
        }

        // Coherencia id path vs body, no es necesario pero por si acaso
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new BadRequestException("El id del body debe coincidir con el id del path");
        }

        CartaModelo existing = cmRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Carta modelo con id " + id + " no encontrada"));

        // ===== VALIDACIONES DTO =====

        // el id no se comprueba pq se coge el del path (si es null ya falla, controlado arriba)

        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new BadRequestException("El nombre es obligatorio");
        }

        if (dto.getNumero() == null) {
            throw new BadRequestException("El número oficial es obligatorio");
        }
        // Si el número cambia
        if (!dto.getNumero().equals(existing.getNumero())) {

            // Verificar que no lo tenga otra carta
            if (cmRepository.existsByNumeroAndIdNot(dto.getNumero(), id)) {
                throw new DuplicateException(
                        "Ya existe otra carta modelo con número " + dto.getNumero()
                );
            }
        }

        if (dto.getTipoCarta() == null) {
            throw new BadRequestException("TipoCarta es obligatorio");
        }

        if (dto.getRareza() == null) {
            throw new BadRequestException("Rareza es obligatoria");
        }

        // Validar imagen
        if (dto.getImagenUrl() == null || dto.getImagenUrl().isBlank()) {
            throw new BadRequestException("ImagenUrl es obligatoria");
        }
        // Validar formato de URL
        try {
            new URL(dto.getImagenUrl());
        } catch (MalformedURLException e) {
            throw new BadRequestException("La URL de la imagen no es válida");
        }

        if (dto.getTipoCarta() == TipoCarta.POKEMON) {
            if (dto.getTipoPokemon() == null) {
                throw new BadRequestException("TipoPokemon obligatorio para cartas Pokémon");
            }
            if (dto.getEvolucion() == null) {
                throw new BadRequestException("Evolucion obligatoria para cartas Pokémon");
            }
        } else {
            dto.setTipoPokemon(null);
            dto.setEvolucion(null);
        }

        // si se manda parametro: se actualiza
        // si no (llega null): no se hace nada: se queda como está ese campo anteriormente
        if (dto.getActivo() != null) {
            existing.setActivo(dto.getActivo()); // seguro, no da NullPointerException
        }

        // ===== ACTUALIZACIÓN =====

        existing.setNombre(dto.getNombre());
        existing.setNumero(dto.getNumero());
        existing.setTipoCarta(dto.getTipoCarta());
        existing.setRareza(dto.getRareza());
        existing.setImagenUrl(dto.getImagenUrl());
        existing.setTipoPokemon(dto.getTipoPokemon());
        existing.setEvolucion(dto.getEvolucion());

        CartaModelo saved = cmRepository.save(existing);

        return Mapper.cartaModeloToDTO(saved);
    }

    @Transactional // seguridad extra al haber múltiples cambios en las entidades (rollback ante fallo, nada a medias)
    public void delete(Long id) {

        if (id == null) {
            throw new BadRequestException("El id es obligatorio");
        }

        // Buscar carta modelo
        CartaModelo existing = cmRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Carta modelo con id " + id + " no encontrada"));

        // Obtener todas las cartas físicas asociadas
        List<CartaFisica> cartasFisicas = cfRepository.findByCartaModeloId(id);

        if (cartasFisicas.isEmpty()) {
            // No hay cartas físicas: borrado físico real
            cmRepository.delete(existing);
            return;
        }

        // Hay cartas físicas → borrado lógico
        existing.setActivo(false);
        cmRepository.save(existing);

        // Separar cartas físicas con intercambios pendientes de las que no
        List<CartaFisica> conPendientes = cfRepository.findConIntercambiosPendientes(id);

        for (CartaFisica cf : cartasFisicas) {

            List<Intercambio> pendientesOrigen =
                    intercambioRepository.findByCartaOrigenIdAndEstado(
                            cf.getId(), EstadoIntercambio.PENDIENTE);

            List<Intercambio> pendientesDestino =
                    intercambioRepository.findByCartaDestinoIdAndEstado(
                            cf.getId(), EstadoIntercambio.PENDIENTE);

            if (!pendientesOrigen.isEmpty() || !pendientesDestino.isEmpty()) {


                pendientesOrigen.forEach(i -> i.setEstado(EstadoIntercambio.RECHAZADO));
                pendientesDestino.forEach(i -> i.setEstado(EstadoIntercambio.RECHAZADO));

                cf.setDisponible(false);
            }
        }

        // Guardar todos los cambios de cartas físicas
        cfRepository.saveAll(cartasFisicas);
    }

}
