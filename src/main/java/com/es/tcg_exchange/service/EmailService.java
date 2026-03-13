package com.es.tcg_exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Auth:

    public void sendVerificationEmail(String to, String token) {

        String verifyUrl = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verifica tu cuenta");
        message.setText(
                "Para verificar tu cuenta haz click en el siguiente enlace:\n\n"
                        + verifyUrl
        );

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        // url del front
        String resetUrl = "http://localhost:8080/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Restablece tu contraseña");
        message.setText(
                "Para restablecer tu contraseña haz click en el siguiente enlace:\n\n"
                        + resetUrl
        );

        mailSender.send(message);
    }

    // Intercambios:

    public void sendIntercambioPropuestaRecibida(String to, String usuarioOrigen, String cartaOfrecida,
                                                 String cartaSolicitada, Long intercambioId) {
        String url = "http://localhost:5173/intercambio/" + intercambioId; // Frontend
        String body = "Has recibido una nueva propuesta de intercambio de " + usuarioOrigen + ":\n\n" +
                "Carta que te ofrecen: " + cartaOfrecida + "\n" +
                "Carta que solicitan: " + cartaSolicitada + "\n\n" +
                "Ver detalle y aceptar/rechazar: " + url;
        sendEmail(to, "Propuesta de intercambio recibida", body);
    }

    public void sendIntercambioPropuestaEnviada(String to, String usuarioDestino, String cartaOfrecida,
                                                String cartaSolicitada, Long intercambioId) {
        String url = "http://localhost:5173/intercambio/" + intercambioId; // Frontend
        String body = "Has enviado una propuesta de intercambio a " + usuarioDestino + ":\n\n" +
                "Carta que ofreces: " + cartaOfrecida + "\n" +
                "Carta que solicitas: " + cartaSolicitada + "\n\n" +
                "Ver detalle: " + url;
        sendEmail(to, "Propuesta de intercambio enviada", body);
    }

    public void sendIntercambioAceptado(String to, String otroUsuario, boolean esDestino, String cartaOrigen,
                                        String cartaDestino, String direccionOtroUsuario, Long intercambioId) {
        String url = "http://localhost:5173/intercambio/" + intercambioId;
        String body;
        if (esDestino) {
            body = "¡Has aceptado el intercambio de " + otroUsuario + "!\n\n" +
                    "Carta que recibes: " + cartaDestino + "\n" +
                    "Carta que envías: " + cartaOrigen + "\n\n" +
                    "Ya puedes enviar tu carta a la dirección de " + otroUsuario + ": " + "\n\n" +
                    direccionOtroUsuario + "\n\n" +
                    "Ver detalle: " + url;
        } else {
            body = otroUsuario + " ha aceptado tu intercambio!\n\n" +
                    "Carta que recibes: " + cartaOrigen + "\n" +
                    "Carta que envías: " + cartaDestino + "\n\n" +
                    "Ya puedes enviar tu carta a la dirección de " + otroUsuario + ": " + "\n\n" +
                    direccionOtroUsuario + "\n\n" +
                    "Ver detalle: " + url;
        }
        sendEmail(to, "Intercambio aceptado", body);
    }

    public void sendIntercambioRechazado(String to, String otroUsuario, boolean esDestino, String cartaOrigen,
                                         String cartaDestino, Long intercambioId) {
        String url = "http://localhost:5173/intercambio/" + intercambioId;
        String body;
        if (esDestino) {
            body = "Has rechazado el intercambio de " + otroUsuario + "\n\n" +
                    "Carta que recibías: " + cartaDestino + "\n" +
                    "Carta que ofrecías: " + cartaOrigen + "\n\n" +
                    "Ver detalle: " + url;
        } else {
            body = otroUsuario + " ha rechazado tu intercambio\n\n" +
                    "Carta que ofrecías: " + cartaOrigen + "\n" +
                    "Carta que solicitabas: " + cartaDestino + "\n\n" +
                    "Ver detalle: " + url;
        }
        sendEmail(to, "Intercambio rechazado", body);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}