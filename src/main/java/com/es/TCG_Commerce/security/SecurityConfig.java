package com.es.TCG_Commerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RsaKeyProperties rsaKeys;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos "Cross-Site Request Forgery" (CSRF) (No lo trataremos en este ciclo)
                .authorizeHttpRequests(auth -> auth // Filtros para securizar diferentes endpoints de la aplicación
                                .requestMatchers(HttpMethod.POST, "/usuarios/login", "/usuarios/register").permitAll() // Filtro que deja pasar todas las peticiones que vayan a los endpoints que definamos
                                // solo pueden acceder a estar rutas usuarios logueados con rol ADMIN:
                                .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/cartas/{id}").hasRole("ADMIN")

                                //ejemplos:
//                                .requestMatchers(HttpMethod.GET,"/usuarios/byNombre/{nombre}").authenticated()
//                                .requestMatchers("/productos/**").authenticated()

                                .anyRequest().authenticated() // Para el resto de peticiones, el usuario debe estar autenticado
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())) // Establecemos el que el control de autenticación se realice por JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
