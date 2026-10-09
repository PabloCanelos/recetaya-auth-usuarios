
package com.auth_usuarios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // API REST sin sesiones HTTP
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session -> session
                .sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // LOGIN PUBLICO
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/login"
                ).permitAll()

                // CONSULTAR USUARIOS
                // Solo MEDICO y FARMACEUTICO autenticados
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/usuarios",
                    "/api/usuarios/**"
                ).hasAnyRole("MEDICO", "FARMACEUTICO")

                // BLOQUEAR OPERACIONES ADMINISTRATIVAS
                // Registro, actualización y eliminación
                // hasta definir un mecanismo autorizado
                .requestMatchers(
                    "/api/usuarios",
                    "/api/usuarios/**"
                ).hasAnyRole("MEDICO", "FARMACEUTICO")

                // BLOQUEAR ADMINISTRACION DE ROLES
                .requestMatchers(
                    "/api/roles",
                    "/api/roles/**"
                ).denyAll()

                // DENEGAR CUALQUIER OTRA RUTA
                .anyRequest().denyAll()
            )

            // VALIDAR JWT ANTES DE LA AUTENTICACION ESTANDAR
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
