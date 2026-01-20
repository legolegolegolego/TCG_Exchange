package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// el jpa, por ejemplo, implementa solo la busqueda por id
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data JPA se encarga automáticamente de implementar los métodos.
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
