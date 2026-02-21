package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.CartaFisicaCreateDTO;
import com.es.tcg_exchange.dto.CartaFisicaDTO;
import com.es.tcg_exchange.service.CartaFisicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartas-fisicas")
public class CartaFisicaController {

    @Autowired
    private CartaFisicaService cfService;

    // todas las cartas disponibles de tal usuario
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<CartaFisicaDTO>> getDisponiblesByUsername(
            @PathVariable String username) {
        List<CartaFisicaDTO> cartas = cfService.findDisponiblesByUsername(username);
        return ResponseEntity.ok(cartas);
    }

    // cartas no disponibles de tal usuario
    @GetMapping("/usuario/{username}/no-disponibles")
    public ResponseEntity<List<CartaFisicaDTO>> getNoDisponiblesByUsername(
            @PathVariable String username,
            Authentication authentication
    ) {
        List<CartaFisicaDTO> cartas = cfService.findNoDisponiblesByUsername(username, authentication);
        return ResponseEntity.ok(cartas);
    }

    // Obtener una carta física por id
    @GetMapping("/{id}")
    public ResponseEntity<CartaFisicaDTO> getCartaFisicaById(
            @PathVariable Long id,
            Authentication authentication){
        CartaFisicaDTO cfDTO = cfService.findById(id, authentication);

        return ResponseEntity.ok(cfDTO);
    }

    // Crear carta física
    @PostMapping
    public ResponseEntity<CartaFisicaDTO> createCartaFisica(
            @RequestBody CartaFisicaCreateDTO cfDTO,
            Authentication authentication){
        CartaFisicaDTO cartaCreada =  cfService.create(cfDTO, authentication);

        return new ResponseEntity<CartaFisicaDTO>(cartaCreada, HttpStatus.CREATED);
    }

    // Actualizar carta física
    @PutMapping("/{id}")
    public ResponseEntity<CartaFisicaDTO> updateCartaFisica(
            @PathVariable Long id,
            @RequestBody CartaFisicaCreateDTO cartaCreateDTO,
            Authentication authentication) {

        CartaFisicaDTO updated = cfService.update(id, cartaCreateDTO, authentication);

        return ResponseEntity.ok(updated);
    }

    // Borrar carta física
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartaFisica(
            @PathVariable Long id,
            Authentication authentication) {

        cfService.delete(id, authentication);

        return ResponseEntity.noContent().build();
    }
}
