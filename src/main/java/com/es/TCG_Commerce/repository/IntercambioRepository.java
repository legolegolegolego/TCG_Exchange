package com.es.TCG_Commerce.repository;

import com.es.TCG_Commerce.model.Intercambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntercambioRepository extends JpaRepository<Intercambio, Long> {
    // nada adicional
}
