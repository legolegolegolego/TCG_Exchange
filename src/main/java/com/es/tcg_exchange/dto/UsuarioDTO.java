package com.es.tcg_exchange.dto;

/*
En Java un record es una clase inmutable y compacta, introducida en Java 16, diseñada para representar “datos puros”.
Es útil para DTOs (Data Transfer Objects) porque automáticamente te genera:

- Constructor con todos los campos
- Getters (id() y username())
- equals(), hashCode() y toString()
 */
public record UsuarioDTO(Long id, String username) {
}
