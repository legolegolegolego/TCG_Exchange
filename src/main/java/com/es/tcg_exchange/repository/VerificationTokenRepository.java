package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.VerificationToken;
import com.es.tcg_exchange.model.enums.TipoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByTokenAndTipo(String token, TipoToken tipo);
    void deleteByUsuarioAndTipo(Usuario usuario, TipoToken tipo);
    void deleteByUsuario(Usuario usuario);
}