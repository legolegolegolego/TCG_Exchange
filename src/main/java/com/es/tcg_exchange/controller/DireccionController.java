package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.DireccionCreateDTO;
import com.es.tcg_exchange.dto.DireccionDTO;
import com.es.tcg_exchange.service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direccion")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping("/{username}")
    public ResponseEntity<DireccionDTO> getDireccion(
            @PathVariable String username,
            Authentication authentication
    ){
        DireccionDTO dto = direccionService.findByUsername(username, authentication);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> createDireccion(
            @RequestBody DireccionCreateDTO createDTO,
            Authentication authentication
            ){

        DireccionDTO direccionCreada = direccionService.create(createDTO, authentication);

        return new ResponseEntity<DireccionDTO>(direccionCreada, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<DireccionDTO> updateDireccion(
            @RequestBody DireccionCreateDTO createDTO,
            Authentication authentication
    ){

        DireccionDTO direccionNueva = direccionService.update(createDTO, authentication);

        return ResponseEntity.ok(direccionNueva);
    }

    // no se puede borrar una direccion

}
