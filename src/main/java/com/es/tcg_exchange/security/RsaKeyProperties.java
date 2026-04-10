package com.es.tcg_exchange.security;

import com.es.tcg_exchange.utils.PemUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
// version local con claves locales:
//public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
//
//}

// version despliegue claves variables .env Render
public class RsaKeyProperties {

    private String publicKey;
    private String privateKey;

    public RSAPublicKey publicKey() {
        return PemUtils.parsePublicKey(publicKey);
    }

    public RSAPrivateKey privateKey() {
        return PemUtils.parsePrivateKey(privateKey);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}