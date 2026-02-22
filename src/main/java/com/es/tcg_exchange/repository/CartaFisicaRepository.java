package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaFisicaRepository extends JpaRepository <CartaFisica, Long> {

    @Query("SELECT DISTINCT c.usuario FROM CartaFisica c WHERE c.cartaModelo.id = :cartaModeloId")
    List<Usuario> findUsuariosConCartaModelo(@Param("cartaModeloId") Long cartaModeloId);

    List<CartaFisica> findByCartaModeloId(Long cartaModeloId);

    @Query("SELECT DISTINCT c FROM Intercambio i " +
            "JOIN CartaFisica c ON (c = i.cartaOrigen OR c = i.cartaDestino) " +
            "WHERE c.cartaModelo.id = :cartaModeloId " +
            "AND i.estado = :estado")
    List<CartaFisica> findConIntercambiosPendientes(
            @Param("cartaModeloId") Long cartaModeloId,
            @Param("estado") EstadoIntercambio estado
    );

    List<CartaFisica> findByUsuarioIdAndDisponible(Long usuarioId, boolean disponible);

}