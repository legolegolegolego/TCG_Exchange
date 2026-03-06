package com.es.tcg_exchange.service;

import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.VerificationToken;
import com.es.tcg_exchange.model.enums.TipoToken;
import com.es.tcg_exchange.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository VTRepository;

    public VerificationToken createToken(Usuario usuario, TipoToken tipo) {

        //antes de crear uno nuevo comprueba tokens anteriores del mismo tipo para con el usuario y los elimina
        VTRepository.deleteByUsuarioAndTipo(usuario, tipo);

        VerificationToken token = new VerificationToken();
        token.setUsuario(usuario);
        token.setTipo(tipo);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiracion(LocalDateTime.now().plusHours(24));
        return save(token);
    }

    public VerificationToken validateToken(String token, TipoToken tipo) {


        if (token == null || token.isBlank()) {
            throw new BadRequestException("Token inválido");
        }

        VerificationToken verificationToken = VTRepository.findByTokenAndTipo(token, tipo)
                .orElseThrow(() -> new NotFoundException("Token no encontrado"));

        if (verificationToken.isUsado()) {
            throw new BadRequestException("Token ya usado");
        }
        if (verificationToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expirado");
        }
        return verificationToken;
    }

    public void markAsUsed(VerificationToken token) {
        token.setUsado(true);
        save(token);
    }

    private VerificationToken save(VerificationToken token) {
        return VTRepository.save(token);
    }

    public void delete(VerificationToken token) {
        VTRepository.delete(token);
    }
}
