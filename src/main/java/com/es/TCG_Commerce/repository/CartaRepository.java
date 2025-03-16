package com.es.TCG_Commerce.repository;

import com.es.TCG_Commerce.model.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Long> {
    // El método findByNombre(String nombre) funciona porque Spring Data JPA
    // genera automáticamente la consulta SQL correspondiente basándose en el nombre del método.
    // Es decir, busca una carta por su nombre en la base de datos.
    // SELECT * FROM carta WHERE nombre = ?;
    Optional<Carta> findByNombre(String nombre);

}
