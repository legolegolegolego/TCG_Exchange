package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.CartaModeloDTO;
import com.es.tcg_exchange.dto.UsuarioDTO;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.DuplicateException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.model.CartaModelo;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.EtapaEvolucion;
import com.es.tcg_exchange.model.enums.Rareza;
import com.es.tcg_exchange.model.enums.TipoCarta;
import com.es.tcg_exchange.model.enums.TipoPokemon;
import com.es.tcg_exchange.repository.CartaFisicaRepository;
import com.es.tcg_exchange.repository.CartaModeloRepository;
import com.es.tcg_exchange.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaModeloService {

    @Autowired
    private CartaModeloRepository cmRepository;

    @Autowired
    private CartaFisicaRepository cfRepository;

    /**
     * Buscar cartas modelo por filtros (opcionales)
     * @param idMin
     * @param idMax
     * @param nombre
     * @param tipoCarta
     * @param rareza
     * @param tipoPokemon
     * @param evolucion
     * @return dto de cartaModelo
     */
    public Page<CartaModeloDTO> findAll(
            Long idMin,
            Long idMax,
            String nombre,
            TipoCarta tipoCarta,
            Rareza rareza,
            TipoPokemon tipoPokemon,
            EtapaEvolucion evolucion,
            Pageable pageable
    ) {

        /* versión 1

        List<CartaModelo> cartas = cmRepository.findAll();

        return cartas.stream()

                .filter(c -> idMin == null || c.getId() >= idMin)
                .filter(c -> idMax == null || c.getId() <= idMax)
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

        if (pageable.getPageSize() > 50) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    50,
                    pageable.getSort()
            );
        }

        Specification<CartaModelo> spec = Specification.where(null);

        if (idMin != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("id"), idMin));
        }

        if (idMax != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("id"), idMax));
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

        return cmRepository.findAll(spec, pageable)
                .map(Mapper::cartaModeloToDTO);
    }

    /**
     * Obtener cartaModelo por id
     * @param id
     * @return dto de cartaModelo
     */
    public CartaModeloDTO findById(Long id) {

        /*
            Si el id viene por path (/{id}), Spring nunca enviará null.

            Si no se envía id → no matchea la ruta.

            Si no es numérico → lanza MethodArgumentTypeMismatchException.

            Asi que, innecesario el if:
         */

//        if (id == null) {
//            throw new BadRequestException("El id no puede ser null");
//        }

        CartaModelo cartaModelo = cmRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Carta modelo con id " + id + " no encontrada"));

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

        // Validar ID
        if (dto.getId() == null) {
            throw new BadRequestException("El id de la carta modelo es obligatorio");
        }
        if (cmRepository.existsById(dto.getId())) {
            throw new DuplicateException("Ya existe una carta modelo con id " + dto.getId());
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

        // Coherencia id path vs body
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

        if (dto.getTipoCarta() == null) {
            throw new BadRequestException("TipoCarta es obligatorio");
        }

        if (dto.getRareza() == null) {
            throw new BadRequestException("Rareza es obligatoria");
        }

        if (dto.getImagenUrl() == null || dto.getImagenUrl().isBlank()) {
            throw new BadRequestException("ImagenUrl es obligatoria");
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

        // ===== ACTUALIZACIÓN =====

        existing.setNombre(dto.getNombre());
        existing.setTipoCarta(dto.getTipoCarta());
        existing.setRareza(dto.getRareza());
        existing.setImagenUrl(dto.getImagenUrl());
        existing.setTipoPokemon(dto.getTipoPokemon());
        existing.setEvolucion(dto.getEvolucion());

        CartaModelo saved = cmRepository.save(existing);

        return Mapper.cartaModeloToDTO(saved);
    }

    public void delete(Long id) {

        if (id == null) {
            throw new BadRequestException("El id es obligatorio");
        }

        CartaModelo existing = cmRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Carta modelo con id " + id + " no encontrada"));

        cmRepository.delete(existing);
    }

}
