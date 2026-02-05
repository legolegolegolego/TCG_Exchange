package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.IntercambioDTO;
import com.es.tcg_exchange.service.IntercambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacciones")
public class IntercambioController {

    @Autowired
    private IntercambioService tservice;

    // no tiene sentido obtener todas las transacciones, no lo quiero para mi API
//    @GetMapping("/")
//    public ResponseEntity<List<TransaccionDTO>> getAll(){
//        List<TransaccionDTO> tdto = tservice.getAll();
//
//        return new ResponseEntity<List<TransaccionDTO>>(tdto, HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<IntercambioDTO> findById(@PathVariable Long id){
        IntercambioDTO tdto = tservice.findById(id);

        return new ResponseEntity<IntercambioDTO>(tdto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IntercambioDTO> insert(@RequestBody IntercambioDTO tdto){
        tservice.insert(tdto);

        return new ResponseEntity<IntercambioDTO>(tdto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntercambioDTO> update(@PathVariable Long id, @RequestBody IntercambioDTO tdto){
        tservice.update(id, tdto);

        return new ResponseEntity<IntercambioDTO>(tdto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IntercambioDTO> delete(@PathVariable Long id){

        return new ResponseEntity<IntercambioDTO>(tservice.delete(id), HttpStatus.OK);
    }
}
