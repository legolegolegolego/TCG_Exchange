package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.service.CartaModeloService;
import com.es.tcg_exchange.dto.CartaModeloDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartas-modelo")
public class CartaModeloController {

    @Autowired
    private CartaModeloService cmService;

    // Obtener todas las cartas modelo (con filtros opcionales)
    @GetMapping
    public ResponseEntity<List<CartaModeloDTO>> getCartasModelo(
            @RequestParam(required = false) Long idMin,
            @RequestParam(required = false) Long idMax,
            @RequestParam(required = false) String tipoCarta,
            @RequestParam(required = false) String rareza,
            @RequestParam(required = false) String tipoPokemon,
            @RequestParam(required = false) String evolucion
    ) {
        List<CartaModeloDTO> cmsDTO = cmService.findAll(
                idMin, idMax, tipoCarta, rareza, tipoPokemon, evolucion
        );

        return new ResponseEntity<List<CartaModeloDTO>>(cmsDTO, HttpStatus.OK);
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
        cmService.insert(cmDTO);

        return new ResponseEntity<CartaModeloDTO>(cmDTO, HttpStatus.CREATED);
    }

    // Actualizar una carta modelo
    @PutMapping("/{id}")
    public ResponseEntity<CartaModeloDTO> updateCartaModelo(
            @PathVariable Long id,
            @RequestBody CartaModeloDTO cmDTO
    ) {
        cmService.update(id, cmDTO);

        return new ResponseEntity<CartaModeloDTO>(cmDTO, HttpStatus.OK);
    }

    // Eliminar una carta modelo
    @DeleteMapping("/{id}")
    public ResponseEntity<CartaModeloDTO> deleteCartaModelo(@PathVariable Long id) {

        return new ResponseEntity<CartaModeloDTO>(cmService.delete(id), HttpStatus.OK);
    }
}