package com.es.TCG_Commerce.controller;

import com.es.TCG_Commerce.dto.TransaccionDTO;
import com.es.TCG_Commerce.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService tservice;

    // no tiene sentido obtener todas las transacciones, no lo quiero para mi API
//    @GetMapping("/")
//    public ResponseEntity<List<TransaccionDTO>> getAll(){
//        List<TransaccionDTO> tdto = tservice.getAll();
//
//        return new ResponseEntity<List<TransaccionDTO>>(tdto, HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionDTO> findById(@PathVariable Long id){
        TransaccionDTO tdto = tservice.findById(id);

        return new ResponseEntity<TransaccionDTO>(tdto, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<TransaccionDTO> insert(@PathVariable Long idTransaccion, @RequestBody TransaccionDTO tdto){
        tservice.insert(idTransaccion, tdto);

        return new ResponseEntity<TransaccionDTO>(tdto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionDTO> update(@PathVariable Long idTransaccion, @RequestBody TransaccionDTO tdto){
        tservice.update(idTransaccion, tdto);

        return new ResponseEntity<TransaccionDTO>(tdto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransaccionDTO> delete(@PathVariable Long idTransaccion){

        return new ResponseEntity<TransaccionDTO>(tservice.delete(idTransaccion), HttpStatus.OK);
    }
}
