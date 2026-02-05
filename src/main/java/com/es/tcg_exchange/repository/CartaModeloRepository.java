package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.CartaModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaModeloRepository extends JpaRepository <CartaModelo, Long> {
}
