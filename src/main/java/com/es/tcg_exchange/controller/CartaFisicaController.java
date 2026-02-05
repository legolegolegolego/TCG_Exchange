package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.service.CartaFisicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartas-fisicas")
public class CartaFisicaController {

    @Autowired
    private CartaFisicaService cfService;

    // Obtener las cartas físicas de un usuario por su username
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<CartaFisicaDTO>> getCartasDeUsuario(
            @PathVariable String username){
        List<CartaFisicaDTO> cfsDTO = cfService.getByUsername(username);

        return new ResponseEntity<List<CartaFisicaDTO>>(cfsDTO, HttpStatus.OK);
    }

    // Obtener una carta física por id
    @GetMapping("/{id}")
    public ResponseEntity<CartaFisicaDTO> getCartaFisicaById(
            @PathVariable Long id){
        CartaFisicaDTO cfDTO = cfService.findById(id);

        return new ResponseEntity<CartaFisicaDTO>(cfDTO, HttpStatus.OK);
    }

    // Crear carta física
    @PostMapping
    public ResponseEntity<CartaFisicaDTO> createCartaFisica(
            @RequestBody CartaFisicaDTO cfDTO){
        cfService.insert(cfDTO);

        return new ResponseEntity<CartaFisicaDTO>(cfDTO, HttpStatus.CREATED);
    }

    // Actualizar carta física
    @PutMapping("/{id}")
    public ResponseEntity<CartaFisicaDTO> updateCartaFisica(
            @PathVariable Long id,
            @RequestBody CartaFisicaDTO cfDTO){
        cfService.update(id, cfDTO);

        return new ResponseEntity<CartaFisicaDTO>(cfDTO, HttpStatus.OK);
    }

    // Eliminar carta física
    @DeleteMapping("/{id}")
    public ResponseEntity<CartaFisicaDTO> deleteCartaFisica(@PathVariable Long id){

        return new ResponseEntity<CartaFisicaDTO>(cfService.delete(id), HttpStatus.OK);
    }
}
