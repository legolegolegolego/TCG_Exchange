package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.error.exception.ForbiddenException;
import com.es.tcg_exchange.service.CartaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{nombreCarta}")
    public ResponseEntity<CartaDTO> findByNombre(@PathVariable String nombre, Authentication authentication){
        if (!authentication.getName().equals(nombre)){
            throw new ForbiddenException("No tienes permiso para acceder a este recurso");
        } else {
            CartaDTO cdto = cartaService.findByNombre(nombre);
            return new ResponseEntity<CartaDTO>(cdto, HttpStatus.OK);
        }
    }

    // para hacer un endpoint dentro de otro, lo meto en el metodo anterior? u otro nuevo
    // y en el metodo del service igual?
    // y que hay si quiero hacer lo mismo en cartas/id/comprar y cartas/nombre/comprar, repito codigo?
    // @GetMapping("/{nombreCarta}/comprar")
    // usuario al que se la compra
    // @GetMapping("/{nombreCarta}/comprar/{nombreUsuario}")

    @PostMapping("/")
    public ResponseEntity<CartaDTO> insertCarta(@RequestBody CartaDTO cdto){
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
