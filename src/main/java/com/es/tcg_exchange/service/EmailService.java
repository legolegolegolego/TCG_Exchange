package com.es.tcg_exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
}