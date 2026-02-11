package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.UsuarioLoginDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.error.exception.InternalServerErrorException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager; // manejar autenticacion (SpringBoot)

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    public String login(UsuarioLoginDTO dto) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Credenciales incorrectas");
        }

        try {
            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al generar el token de autenticación");
        }
    }

    public void register(UsuarioRegisterDTO dto) {
        usuarioService.registerUser(dto);
    }
}
