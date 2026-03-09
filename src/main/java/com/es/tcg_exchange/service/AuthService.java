package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.PasswordResetDTO;
import com.es.tcg_exchange.dto.UsuarioLoginDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.error.exception.*;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.VerificationToken;
import com.es.tcg_exchange.model.enums.TipoToken;
import com.es.tcg_exchange.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager; // manejar autenticacion (SpringBoot)

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login(UsuarioLoginDTO dto) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getIdentifier(),
                            dto.getPassword()
                    )
            );

            // Obtener el usuario real desde la base de datos
            Usuario usuario = usuarioService.findByUsernameOrEmail(dto.getIdentifier());

            if (!usuario.isEmailVerificado()) {
                throw new UnauthorizedException("Email no verificado. Revisa tu correo.");
            }

            return tokenService.generateToken(authentication);

        } catch (DisabledException e) {
            throw new ForbiddenException("Usuario desactivado");
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Credenciales incorrectas");
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Error en autenticación");
        } catch (UnauthorizedException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Error interno");
        }
    }

    public void register(UsuarioRegisterDTO dto) {
        Usuario usuario = usuarioService.registerUser(dto);

        VerificationToken token =
                verificationTokenService.createToken(usuario, TipoToken.EMAIL_VERIFICATION);

        emailService.sendVerificationEmail(
                usuario.getEmail(),
                token.getToken()
        );
    }

    @Transactional
    public void verifyEmail(String token) {
        // Buscar y validar el token
        VerificationToken verificationToken = verificationTokenService.validateToken(token, TipoToken.EMAIL_VERIFICATION);

        // Marcar el usuario como verificado
        Usuario usuario = verificationToken.getUsuario();
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        // Marcar el token como usado
        verificationTokenService.markAsUsed(verificationToken);
    }

    @Transactional
    public void resendVerificationToken(String email) {
        Usuario usuario = usuarioService.findByEmail(email);

        if (usuario.isDesactivado()){
            throw new ForbiddenException("No se puede realizar la accion: usuario desactivado");
        }

        // Solo usuarios no verificados pueden solicitar reenvío
        if (usuario.isEmailVerificado()) {
            throw new BadRequestException("El email ya está verificado");
        }

        // Crea un nuevo token de verificación
        VerificationToken token = verificationTokenService.createToken(usuario, TipoToken.EMAIL_VERIFICATION);

        // Envía email con enlace
        emailService.sendVerificationEmail(usuario.getEmail(), token.getToken());
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        Usuario usuario = usuarioService.findByEmail(email);

        if (!usuario.isEmailVerificado()) {
            throw new ForbiddenException("Debes verificar tu email antes de recuperar la contraseña.");
        }

        if (usuario.isDesactivado()){
            throw new ForbiddenException("No se puede realizar la accion: usuario desactivado");
        }

        // Crear token de tipo PASSWORD_RESET
        VerificationToken token = verificationTokenService.createToken(usuario, TipoToken.PASSWORD_RESET);

        // Enviar email con enlace
        emailService.sendPasswordResetEmail(usuario.getEmail(), token.getToken());
    }

    @Transactional
    public void resetPassword(PasswordResetDTO dto) {
        VerificationToken token = verificationTokenService.validateToken(dto.getToken(), TipoToken.PASSWORD_RESET);

        Usuario usuario = token.getUsuario();

        if (usuario.isDesactivado()){
            throw new ForbiddenException("No se puede realizar la accion: usuario desactivado");
        }

        // Logica de pass
        if (dto.getNewPassword() == null
                || !dto.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new BadRequestException(
                    "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial"
            );
        }
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        usuarioRepository.save(usuario);

        // Marcar token como usado
        verificationTokenService.markAsUsed(token);
    }
}
