
package com.auth_usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth_usuarios.dto.LoginRequest;
import com.auth_usuarios.dto.LoginResponse;
import com.auth_usuarios.entity.UsuarioEntity;
import com.auth_usuarios.exception.CredencialesInvalidasException;
import com.auth_usuarios.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // INICIAR SESION
    public LoginResponse login(LoginRequest datos) {

        // VALIDAR CAMPOS OBLIGATORIOS
        if (datos == null ||
                datos.getEmail() == null ||
                datos.getEmail().isBlank() ||
                datos.getPassword() == null ||
                datos.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "El email y la contraseña son obligatorios"
            );
        }

        String email = datos.getEmail().trim().toLowerCase();

        // BUSCAR USUARIO POR EMAIL
        UsuarioEntity usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new CredencialesInvalidasException(
                                "Credenciales incorrectas"
                        )
                );

        // COMPROBAR CONTRASEÑA CON BCRYPT
        if (!passwordEncoder.matches(
                datos.getPassword(),
                usuario.getPasswordHash())) {

            throw new CredencialesInvalidasException(
                    "Credenciales incorrectas"
            );
        }

        // COMPROBAR ESTADO DEL USUARIO
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalStateException(
                    "El usuario se encuentra inactivo"
            );
        }

        // GENERAR TOKEN JWT
        String token = jwtService.generarToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getRol().getNombre()
        );
    }
}
