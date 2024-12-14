package com.es.TCG_Commerce.repository;

import com.es.TCG_Commerce.model.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Long> {
    Optional<Carta> findByNombre(String nombre);

}
