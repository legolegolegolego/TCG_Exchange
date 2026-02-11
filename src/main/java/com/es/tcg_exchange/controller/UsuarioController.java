package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.UsuarioPrivateDTO;
import com.es.tcg_exchange.service.UsuarioService;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // indica a Spring Boot que es un controller, maneja solicitudes HTTP
@RequestMapping("/usuarios") // mapear solicitudes http (todas las que lleguen a /usuarios/... seran manejadas aqui)
public class UsuarioController {

    @Autowired // inyecta auto instancias de repo
    private UsuarioService usuarioService;

    // obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<List<UsuarioPrivateDTO>> getAll(){
        List<UsuarioPrivateDTO> usuariosDTO = usuarioService.getAll();

        return new ResponseEntity<List<UsuarioPrivateDTO>>(usuariosDTO, HttpStatus.OK);
    }

    // no hace falta el authentication aqui pq solo pueden acceder los admin
    // y ya se contempla eso en el securityconfig
    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioPrivateDTO> findById(@PathVariable Long id){
        UsuarioPrivateDTO usuarioPrivateDTO = usuarioService.findById(id);

        return new ResponseEntity<UsuarioPrivateDTO>(usuarioPrivateDTO, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioPrivateDTO> findByUsername(
            @PathVariable String username, Authentication authentication) {

        // si no tiene los permisos el método se encarga de lanzar excepcion:
        SecurityUtils.checkAdminOrSelf(authentication, username);

        UsuarioPrivateDTO usuarioPrivateDTO = usuarioService.findByUsername(username);
        return new ResponseEntity<>(usuarioPrivateDTO, HttpStatus.OK);
    }


    @PutMapping("/{username}")
    public ResponseEntity<UsuarioPrivateDTO> updateUsername(
            @PathVariable String username, @RequestBody UsuarioPrivateDTO udto, Authentication authentication
    ){

        SecurityUtils.checkAdminOrSelf(authentication, username);
        Usu

    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(
//    public ResponseEntity<UsuarioPrivateDTO> deleteUser(
            @PathVariable String username, Authentication authentication
    ){
        SecurityUtils.checkAdminOrSelf(authentication, username);

        usuarioService.deleteUser(username);
        return ResponseEntity.noContent().build();

    }

}
