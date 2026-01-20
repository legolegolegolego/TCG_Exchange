package com.es.tcg_exchange;

import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    /**
     * Inicializa un usuario ADMIN solo para entorno de desarrollo / académico.
     * En producción este mecanismo no debería existir.
     */
    @Bean
    CommandLineRunner initAdmin(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (!usuarioRepository.existsByUsername("admin")) {
                Usuario admin = new Usuario(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        Rol.ROLE_ADMIN
                );
                usuarioRepository.save(admin);
            }
        };
    }
}
