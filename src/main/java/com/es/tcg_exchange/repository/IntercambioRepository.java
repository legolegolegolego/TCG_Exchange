package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntercambioRepository extends JpaRepository<Intercambio, Long> {
    /**
     * Comprueba si existen intercambios asociados a un usuario.
     * @param usuarioOrigen usuarioDestino (mandar el mismo dos veces para ver si está en algún intercambio)
     * @return true si existen intercambios relacionados con el usuario, false si no
     */
    boolean existsByUsuarioOrigenOrUsuarioDestino(Usuario usuarioOrigen, Usuario usuarioDestino);
}
