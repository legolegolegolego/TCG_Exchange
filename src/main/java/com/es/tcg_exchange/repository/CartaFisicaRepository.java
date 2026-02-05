package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.CartaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaFisicaRepository extends JpaRepository <CartaFisica, Long> {
}