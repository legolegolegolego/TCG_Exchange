package com.es.tcg_exchange.utils;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;

/**
 * Clase para convertir String (variable pasada) a clave RSA
 */
public class PemUtils {

    public static RSAPublicKey parsePublicKey(String key) {
        try {
            String content = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\\n", "")
                    .trim();

            byte[] decoded = Base64.getDecoder().decode(content);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPrivateKey parsePrivateKey(String key) {
        try {
            String content = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("\\n", "")
                    .trim();

            byte[] decoded = Base64.getDecoder().decode(content);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}