package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.Direccion;
import com.es.tcg_exchange.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    Optional<Direccion> findByUsuarioId(Long usuarioId);

    boolean existsByUsuario(Usuario usuario);

}