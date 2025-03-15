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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

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
            throw new NotFoundException("Credenciales del usuario incorrectas");
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

    @PostMapping("/register")
    public ResponseEntity<UsuarioRegisterDTO> register(
            @RequestBody UsuarioRegisterDTO usuarioRegisterDTO) {

        usuarioService.registerUser(usuarioRegisterDTO);

        return new ResponseEntity<UsuarioRegisterDTO>(usuarioRegisterDTO, HttpStatus.OK);

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

    // apartir de aqui ejemplos

    /*@GetMapping("/transaccion/{id}")
    public String login(
            @PathVariable String id
    ) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getUsername(), usuarioLoginDTO.getPassword())// modo de autenticación
            );
        } catch (Exception e) {
            System.out.println("Excepcion en authentication");
            throw new NotFoundException("Credenciales del usuario incorrectas");
        }
    }

    @PostMapping("/transaccion/")
    public String login(
            @RequestBody TransaccionDTO tDTO
    ) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getUsername(), usuarioLoginDTO.getPassword())// modo de autenticación
            );
        } catch (Exception e) {
            System.out.println("Excepcion en authentication");
            throw new NotFoundException("Credenciales del usuario incorrectas");
        }
    }*/
}
