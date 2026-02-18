package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaFisicaRepository extends JpaRepository <CartaFisica, Long> {

    @Query("SELECT DISTINCT c.usuario FROM CartaFisica c WHERE c.cartaModelo.id = :cartaModeloId")
    List<Usuario> findUsuariosConCartaModelo(@Param("cartaModeloId") Long cartaModeloId);
}