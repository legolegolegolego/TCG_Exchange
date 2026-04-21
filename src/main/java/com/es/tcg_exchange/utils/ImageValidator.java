package com.es.tcg_exchange.utils;

import com.es.tcg_exchange.error.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ImageValidator {

    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/jpeg", // jpg + jpeg
            "image/png",
            "image/webp"
    );

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    // para create
    public static void validarImagenObligatoria(MultipartFile imagen) {

        if (imagen == null || imagen.isEmpty()) {
            throw new BadRequestException("La imagen es obligatoria");
        }

        validarContenido(imagen);
    }

    // para update
    public static void validarImagenOpcional(MultipartFile imagen) {

        if (imagen == null || imagen.isEmpty()) {
            return; // no validar nada
        }

        validarContenido(imagen);
    }

    private static void validarContenido(MultipartFile imagen) {

        String contentType = imagen.getContentType();

        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new BadRequestException("Formato de imagen no válido (solo jpg, jpeg, png, webp)");
        }

        if (imagen.getSize() > MAX_SIZE) {
            throw new BadRequestException("La imagen es demasiado grande (máx 5MB)");
        }
    }
}