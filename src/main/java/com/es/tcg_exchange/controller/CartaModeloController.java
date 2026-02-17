package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.model.enums.EtapaEvolucion;
import com.es.tcg_exchange.model.enums.Rareza;
import com.es.tcg_exchange.model.enums.TipoCarta;
import com.es.tcg_exchange.model.enums.TipoPokemon;
import com.es.tcg_exchange.service.CartaModeloService;
import com.es.tcg_exchange.dto.CartaModeloDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartas-modelo")
public class CartaModeloController {

    @Autowired
    private CartaModeloService cmService;

    // Obtener todas las cartas modelo (con filtros opcionales)
    @GetMapping
    public ResponseEntity<Page<CartaModeloDTO>> getCartasModelo(
            @RequestParam(required = false) Long idMin,
            @RequestParam(required = false) Long idMax,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) TipoCarta tipoCarta,
            @RequestParam(required = false) Rareza rareza,
            @RequestParam(required = false) TipoPokemon tipoPokemon,
            @RequestParam(required = false) EtapaEvolucion evolucion,
            Pageable pageable
    ) {

        Page<CartaModeloDTO> resultado = cmService.findAll(
                idMin,
                idMax,
                nombre,
                tipoCarta,
                rareza,
                tipoPokemon,
                evolucion,
                pageable
        );

//        List<CartaModeloDTO> cmsDTO = cmService.findAll(
//                idMin, idMax, tipoCarta, rareza, tipoPokemon, evolucion
//        );

//        return new ResponseEntity<List<CartaModeloDTO>>(cmsDTO, HttpStatus.OK);

        return ResponseEntity.ok(resultado);
    }

    // Obtener una carta modelo por id
    @GetMapping("/{id}")
    public ResponseEntity<CartaModeloDTO> getCartaModeloById(
            @PathVariable Long id) {
        CartaModeloDTO cmDTO = cmService.findById(id);

        return new ResponseEntity<CartaModeloDTO>(cmDTO, HttpStatus.OK);
    }

    // Crear una nueva carta modelo
    @PostMapping
    public ResponseEntity<CartaModeloDTO> createCartaModelo(
            @RequestBody CartaModeloDTO cmDTO
    ) {
        CartaModeloDTO created = cmService.create(cmDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar una carta modelo
    @PutMapping("/{id}")
    public ResponseEntity<CartaModeloDTO> updateCartaModelo(
            @PathVariable Long id,
            @RequestBody CartaModeloDTO cmDTO
    ) {
        CartaModeloDTO updated = cmService.update(id, cmDTO);

        return ResponseEntity.ok(updated);
    }

    // Eliminar una carta modelo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartaModelo(@PathVariable Long id) {

        cmService.delete(id);

        return ResponseEntity.noContent().build();
    }
}