package com.es.TCG_Commerce.controller;

import com.es.TCG_Commerce.dto.TransaccionDTO;
import com.es.TCG_Commerce.dto.UsuarioDTO;
import com.es.TCG_Commerce.dto.UsuarioLoginDTO;
import com.es.TCG_Commerce.dto.UsuarioRegisterDTO;
import com.es.TCG_Commerce.error.exception.ForbiddenException;
import com.es.TCG_Commerce.error.exception.InternalServerErrorException;
import com.es.TCG_Commerce.error.exception.NotFoundException;
import com.es.TCG_Commerce.error.exception.UnauthorizedException;
import com.es.TCG_Commerce.service.TokenService;
import com.es.TCG_Commerce.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController // indica a Spring Boot que es un controller, maneja solicitudes HTTP
@RequestMapping("/usuarios") // mapear solicitudes http (todas las que lleguen a /usuarios/... seran manejadas aqui)
public class UsuarioController {

    @Autowired // inyecta auto instancias de repo
    private AuthenticationManager authenticationManager; // manejar autenticacion (SpringBoot)

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    /*
    * ¿Por qué no usar ResponseEntity en login?
    En este caso, el método devuelve solo el token de autenticación como String.
    Como Spring Boot lo envuelve automáticamente en una respuesta HTTP, no es estrictamente necesario usar ResponseEntity.
    * */
    @PostMapping("/login")
    public String login(
            @RequestBody UsuarioLoginDTO usuarioLoginDTO
    ) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getUsername(), usuarioLoginDTO.getPassword())// modo de autenticación
            );
        } catch (Exception e) {
            System.out.println("Excepción en authentication");
            throw new UnauthorizedException("Credenciales del usuario incorrectas");
        }

        String token = "";
        try {
            token = tokenService.generateToken(authentication);
        } catch (Exception e) {
            System.out.println("Excepción en generar token");
            throw new InternalServerErrorException("Error al generar el token de autenticación");
        }

        return token;
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
            @RequestBody UsuarioRegisterDTO usuarioRegisterDTO) {

        usuarioService.registerUser(usuarioRegisterDTO);

        return new ResponseEntity<UsuarioRegisterDTO>(usuarioRegisterDTO, HttpStatus.OK);

    }

    // obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<List<UsuarioDTO>> getAll(){
        List<UsuarioDTO>usuarioDTOS = usuarioService.getAll();

        return new ResponseEntity<List<UsuarioDTO>>(usuarioDTOS, HttpStatus.OK);
    }

    // no hace falta el authentication aqui pq solo pueden acceder los admin
    // y ya se contempla eso en el securityconfig
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id){
        UsuarioDTO udto = usuarioService.findById(id);

        return new ResponseEntity<UsuarioDTO>(udto, HttpStatus.OK);
    }

    @GetMapping("/byNombre/{nombre}")
    public ResponseEntity<UsuarioDTO> findByNombre(@PathVariable String nombre, Authentication authentication) {

        if(authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.equals(new SimpleGrantedAuthority("ROLE_ADMIN"))) || authentication.getName().equals(nombre)) {
            UsuarioDTO usuarioDTO = usuarioService.findByNombre(nombre);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        } else {
            throw new ForbiddenException("No tienes los permisos para acceder al recurso");
        }
    }


    @PutMapping("/{nombre}")
    public ResponseEntity<UsuarioDTO> updateUser(
            @PathVariable String nombre, @RequestBody UsuarioDTO udto, Authentication authentication
    ){

        // Comprobar si el usuario autenticado es el mismo que se quiere actualizar
//        String usuarioAutenticado = authentication.getName();
//        boolean esElMismoUsuario = usuarioAutenticado.equals(nombre);
//
//        if (!esElMismoUsuario){
//            throw new ForbiddenException("No tienes permiso para modificar este usuario");
//        }
        if(authentication.getAuthorities()
                .stream()
                .anyMatch(authority
                        -> authority.equals(new SimpleGrantedAuthority("ROLE_ADMIN"))) ||
                authentication.getName().equals(nombre)) {
            UsuarioDTO usuarioDTO = usuarioService.updateUser(nombre, udto);
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        } else {
            throw new ForbiddenException("No tienes los permisos para acceder al recurso");
        }

    }

    @DeleteMapping("/{nombre}")
    public ResponseEntity<UsuarioDTO> deleteUser(
            @PathVariable String nombre, Authentication authentication
    ){
//        if (!authentication.getName().equals(nombre)){
//            throw new ForbiddenException("No tienes permiso para eliminar este usuario");
//        }
        if(authentication.getAuthorities()
                .stream()
                .anyMatch(authority
                        -> authority.equals(new SimpleGrantedAuthority("ROLE_ADMIN"))) ||
                authentication.getName().equals(nombre)) {
            // copio antes de borrar para retornarlo despues
            UsuarioDTO udto = usuarioService.deleteUser(nombre);

            return new ResponseEntity<UsuarioDTO>(udto, HttpStatus.OK);
        } else {
            throw new ForbiddenException("No tienes los permisos para acceder al recurso");
        }

    }

}
