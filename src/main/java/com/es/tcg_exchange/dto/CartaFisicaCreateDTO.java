package com.es.tcg_exchange.dto;

import com.es.tcg_exchange.model.enums.EstadoCarta;
import org.springframework.web.multipart.MultipartFile;

public record CartaFisicaCreateDTO(Long idCartaModelo, EstadoCarta estadoCarta, MultipartFile imagen) {
}
