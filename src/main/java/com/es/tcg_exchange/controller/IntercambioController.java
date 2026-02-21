package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.IntercambioCreateDTO;
import com.es.tcg_exchange.dto.IntercambioDTO;
import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import com.es.tcg_exchange.service.IntercambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/intercambios")
public class IntercambioController {

    @Autowired
    private IntercambioService intercambioService;

    // Obtener intercambios de un usuario
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<IntercambioDTO>> getIntercambiosByUsuario(
            @PathVariable String username,
            @RequestParam(required = false) EstadoIntercambio estado,
            Authentication authentication
    ) {
        List<IntercambioDTO> intercambios = intercambioService.getIntercambiosByUsuario(username, estado, authentication);
        return ResponseEntity.ok(intercambios);
    }

    // Obtener intercambio por id
    @GetMapping("/{id}")
    public ResponseEntity<IntercambioDTO> getIntercambioById(
            @PathVariable Long id,
            Authentication authentication) {

        IntercambioDTO dto = intercambioService.findById(id, authentication);
        return ResponseEntity.ok(dto);
    }

    // Crear nuevo intercambio
    @PostMapping
    public ResponseEntity<IntercambioDTO> createIntercambio(
            @RequestBody IntercambioCreateDTO dto,
            Authentication authentication) {

        IntercambioDTO intercambioCreado = intercambioService.create(dto, authentication);
        return new ResponseEntity<>(intercambioCreado, HttpStatus.CREATED);
    }

    // Aceptar intercambio
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<IntercambioDTO> aceptarIntercambio(
            @PathVariable Long id,
            Authentication authentication) {

        IntercambioDTO intercambioActualizado = intercambioService.aceptarIntercambio(id, authentication);
        return ResponseEntity.ok(intercambioActualizado);
    }

    // Rechazar intercambio
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<IntercambioDTO> rechazarIntercambio(
            @PathVariable Long id,
            Authentication authentication) {

        IntercambioDTO intercambioActualizado = intercambioService.rechazarIntercambio(id, authentication);
        return ResponseEntity.ok(intercambioActualizado);
    }

    // no se pueden eliminar intercambios
}
