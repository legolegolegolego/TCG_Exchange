package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.IntercambioDTO;
import com.es.tcg_exchange.service.IntercambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/intercambios")
public class IntercambioController {

    @Autowired
    private IntercambioService intercambioService;

    // Obtener intercambios de un usuario
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<IntercambioDTO>> getIntercambiosDeUsuario(@PathVariable String username,
                                                                         @RequestParam(required = false) String estado) {
        List<IntercambioDTO> intercambiosDTO = intercambioService.findByUsuario(username, estado);
        return new ResponseEntity<>(intercambiosDTO, HttpStatus.OK);
    }

    // Obtener intercambio por id
    @GetMapping("/{id}")
    public ResponseEntity<IntercambioDTO> getIntercambioById(@PathVariable Long id) {
        IntercambioDTO intercambioDTO = intercambioService.findById(id);
        return new ResponseEntity<>(intercambioDTO, HttpStatus.OK);
    }

    // Crear nuevo intercambio
    @PostMapping
    public ResponseEntity<IntercambioDTO> createIntercambio(@RequestBody IntercambioDTO intercambioDTO) {
        intercambioService.insert(intercambioDTO);
        return new ResponseEntity<>(intercambioDTO, HttpStatus.CREATED);
    }

    // Aceptar intercambio
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<IntercambioDTO> aceptarIntercambio(@PathVariable Long id) {
        IntercambioDTO intercambioDTO = intercambioService.aceptar(id);
        return new ResponseEntity<>(intercambioDTO, HttpStatus.OK);
    }

    // Rechazar intercambio
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<IntercambioDTO> rechazarIntercambio(@PathVariable Long id) {
        IntercambioDTO intercambioDTO = intercambioService.rechazar(id);
        return new ResponseEntity<>(intercambioDTO, HttpStatus.OK);
    }

    // Eliminar intercambio
    @DeleteMapping("/{id}")
    public ResponseEntity<IntercambioDTO> deleteIntercambio(@PathVariable Long id) {
        IntercambioDTO intercambioDTO = intercambioService.delete(id);
        return new ResponseEntity<>(intercambioDTO, HttpStatus.OK);
    }
}
