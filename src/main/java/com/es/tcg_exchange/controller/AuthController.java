package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.PasswordResetDTO;
import com.es.tcg_exchange.dto.UsuarioLoginDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> register(
            @RequestBody UsuarioRegisterDTO dto) {

        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado. Revisa tu email para verificar la cuenta.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verificado");
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Se ha enviado un email con el enlace para restablecer la contraseña.");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }
}
