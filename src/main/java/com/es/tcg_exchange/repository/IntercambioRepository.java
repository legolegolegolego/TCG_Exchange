package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntercambioRepository extends JpaRepository<Intercambio, Long> {
    /**
     * Comprueba si existen intercambios asociados a un usuario.
     * @param usuarioOrigen usuarioDestino (mandar el mismo dos veces para ver si está en algún intercambio)
     * @return true si existen intercambios relacionados con el usuario, false si no
     */
    boolean existsByUsuarioOrigenOrUsuarioDestino(Usuario usuarioOrigen, Usuario usuarioDestino);

    @Query("SELECT i FROM Intercambio i " +
            "WHERE i.usuarioOrigen.id = :usuarioId OR i.usuarioDestino.id = :usuarioId")
    List<Intercambio> findByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT i FROM Intercambio i " +
            "WHERE (i.usuarioOrigen.id = :usuarioId OR i.usuarioDestino.id = :usuarioId) " +
            "AND i.estado = :estado")
    List<Intercambio> findByUsuarioAndEstado(@Param("usuarioId") Long usuarioId,
                                             @Param("estado") EstadoIntercambio estado);

    List<Intercambio> findByCartaOrigenIdAndEstado(Long cartaId, EstadoIntercambio estado);

    List<Intercambio> findByCartaDestinoIdAndEstado(Long cartaId, EstadoIntercambio estado);

    /**
     * Devuelve todos los intercambios en los que participa una carta física,
     * ya sea como carta origen o carta destino, sin importar el estado.
     */
    List<Intercambio> findByCartaOrigenOrCartaDestino(CartaFisica cartaOrigen, CartaFisica cartaDestino);
}
