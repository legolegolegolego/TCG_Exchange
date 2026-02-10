package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.UsuarioPrivateDTO;
import com.es.tcg_exchange.dto.UsuarioLoginDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.error.exception.ForbiddenException;
import com.es.tcg_exchange.error.exception.InternalServerErrorException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import com.es.tcg_exchange.service.TokenService;
import com.es.tcg_exchange.service.UsuarioService;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

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
