package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.UsuarioLoginDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /*
    * ¿Por qué no usar ResponseEntity en login?
    En este caso, el método devuelve solo el token de autenticación como String.
    Como Spring Boot lo envuelve automáticamente en una respuesta HTTP, no es estrictamente necesario usar ResponseEntity.
    * */
    @PostMapping("/login")
    public String login(@RequestBody UsuarioLoginDTO dto) {
        return authService.login(dto);
    }

    /*
    * ResponseEntity<T> es una clase de Spring que representa una respuesta HTTP completa, incluyendo:

    El cuerpo de la respuesta (en este caso, UsuarioRegisterDTO).
    El código de estado HTTP (ejemplo: 200 OK, 404 Not Found, etc.).
    Encabezados HTTP opcionales.
    Es útil cuando quieres controlar más detalles de la respuesta HTTP en lugar de solo devolver un objeto.
    * */

    @PostMapping("/register")
    public ResponseEntity<UsuarioRegisterDTO> register(
            @RequestBody UsuarioRegisterDTO dto) {

        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
