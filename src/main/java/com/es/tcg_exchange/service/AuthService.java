package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.UsuarioLoginDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.error.exception.ForbiddenException;
import com.es.tcg_exchange.error.exception.InternalServerErrorException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

            return tokenService.generateToken(authentication);

        } catch (DisabledException e) {
            throw new ForbiddenException("Usuario desactivado");
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Credenciales incorrectas");
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Error en autenticación");
        } catch (Exception e) {
            throw new InternalServerErrorException("Error interno");
        }
    }

    public void register(UsuarioRegisterDTO dto) {
        usuarioService.registerUser(dto);
    }
}
