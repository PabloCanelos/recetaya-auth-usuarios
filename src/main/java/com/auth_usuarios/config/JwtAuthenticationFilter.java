
package com.auth_usuarios.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth_usuarios.entity.UsuarioEntity;
import com.auth_usuarios.repository.UsuarioRepository;
import com.auth_usuarios.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization =
                request.getHeader("Authorization");

        if (authorization != null
                && authorization.startsWith("Bearer ")) {

            String token = authorization.substring(7);

            if (jwtService.validarToken(token)) {

                String email = jwtService.obtenerEmail(token);

                UsuarioEntity usuario = usuarioRepository
                        .findByEmail(email)
                        .orElse(null);

                if (usuario != null
                        && Boolean.TRUE.equals(usuario.getActivo())) {

                    String rol = usuario.getRol().getNombre();

                    SimpleGrantedAuthority autoridad =
                            new SimpleGrantedAuthority(
                                    "ROLE_" + rol
                            );

                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(autoridad)
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(autenticacion);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
