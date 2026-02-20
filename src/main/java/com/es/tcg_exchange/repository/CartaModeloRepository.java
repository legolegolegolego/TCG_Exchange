package com.es.tcg_exchange.repository;

import com.es.tcg_exchange.model.CartaModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * extends (, JpaSpecificationExecutor<CartaModelo>) para findAll en service (uso de Specification); version profesional
 * JpaSpecificationExecutor permite hacer consultas dinámicas usando Criteria API (filtros directamente en BD)
 */
@Repository
public interface CartaModeloRepository extends JpaRepository <CartaModelo, Long>, JpaSpecificationExecutor<CartaModelo> {
    boolean existsByNumero(Long numero);
    boolean existsByNumeroAndIdNot(Long numero, Long id);
}
