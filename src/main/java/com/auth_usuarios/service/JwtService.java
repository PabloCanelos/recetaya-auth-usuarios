
package com.auth_usuarios.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth_usuarios.entity.UsuarioEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey obtenerClave() {
        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // GENERAR TOKEN
    public String generarToken(UsuarioEntity usuario) {

        Date ahora = new Date();

        Date vencimiento = new Date(
                ahora.getTime() + jwtExpirationMs
        );

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("rol", usuario.getRol().getNombre())
                .issuedAt(ahora)
                .expiration(vencimiento)
                .signWith(obtenerClave())
                .compact();
    }

    // EXTRAER EMAIL DEL TOKEN
    public String obtenerEmail(String token) {
        return obtenerClaims(token).getSubject();
    }

    // VALIDAR TOKEN
    public boolean validarToken(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (io.jsonwebtoken.JwtException |
                 IllegalArgumentException ex) {
            return false;
        }
    }

    // LEER Y VERIFICAR FIRMA DEL TOKEN
    private Claims obtenerClaims(String token) {

        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
