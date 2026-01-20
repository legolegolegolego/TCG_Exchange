package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.Intercambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntercambioRepository extends JpaRepository<Intercambio, Long> {
    // nada adicional
}
