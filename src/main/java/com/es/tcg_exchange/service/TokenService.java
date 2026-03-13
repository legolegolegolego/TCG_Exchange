package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.UsuarioDTO;
import com.es.tcg_exchange.dto.UsuarioFullDTO;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UsuarioService usuarioService;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String username = authentication.getName();

        Usuario usuario = usuarioService.findByUsernameOrEmail(username);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(usuario.getUsername())
                .claim("roles", roles)
                .claim("id", usuario.getId())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
