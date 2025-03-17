package com.es.TCG_Commerce.controller;

import com.es.TCG_Commerce.dto.CartaDTO;
import com.es.TCG_Commerce.error.exception.ForbiddenException;
import com.es.TCG_Commerce.service.CartaService;
import com.es.TCG_Commerce.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartas")
public class CartaController {

    @Autowired
    private CartaService cartaService;

    @GetMapping("/")
    public ResponseEntity<List<CartaDTO>> getAll(){
        List<CartaDTO> cartasdto = cartaService.getAll();

        return new ResponseEntity<List<CartaDTO>>(cartasdto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaDTO> findById(@PathVariable Long id){
        CartaDTO cdto = cartaService.findById(id);

        return new ResponseEntity<CartaDTO>(cdto, HttpStatus.OK);
    }

    @GetMapping("/byNombre/{nombre}")
    public ResponseEntity<CartaDTO> findByNombre(@PathVariable String nombre, Authentication authentication){
        if (!authentication.getName().equals(nombre)){
            throw new ForbiddenException("No tienes permiso para acceder a este recurso");
        } else {
            CartaDTO cdto = cartaService.findByNombre(nombre);
            return new ResponseEntity<CartaDTO>(cdto, HttpStatus.OK);
        }
    }

    @PostMapping("/")
    public ResponseEntity<CartaDTO> insertCarta(@PathVariable Long id, @RequestBody CartaDTO cdto){
        cartaService.insert(cdto);

        return new ResponseEntity<CartaDTO>(cdto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaDTO> updateCarta(@PathVariable Long id, @RequestBody CartaDTO cdto){
        cartaService.update(id, cdto);

        return new ResponseEntity<CartaDTO>(cdto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartaDTO> deleteCarta(@PathVariable Long id){

        return new ResponseEntity<CartaDTO>(cartaService.delete(id), HttpStatus.OK);
    }

}
