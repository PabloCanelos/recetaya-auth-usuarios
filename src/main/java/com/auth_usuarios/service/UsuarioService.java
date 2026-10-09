
package com.auth_usuarios.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth_usuarios.dto.UsuarioRequest;
import com.auth_usuarios.dto.UsuarioResponse;
import com.auth_usuarios.entity.RolEntity;
import com.auth_usuarios.entity.UsuarioEntity;
import com.auth_usuarios.repository.RolRepository;
import com.auth_usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // LISTAR TODOS
    public List<UsuarioResponse> listarUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // BUSCAR POR ID
    public UsuarioResponse buscarPorId(Integer id) {

        UsuarioEntity usuario = obtenerEntidadPorId(id);

        return convertirAResponse(usuario);
    }

    // BUSCAR POR EMAIL
    public UsuarioResponse buscarPorEmail(String email) {

        validarEmail(email);

        String emailNormalizado = email.trim().toLowerCase();

        UsuarioEntity usuario = usuarioRepository
                .findByEmail(emailNormalizado)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe un usuario con email: " + email
                ));

        return convertirAResponse(usuario);
    }

    // CREAR USUARIO
    public UsuarioResponse crearUsuario(UsuarioRequest datos) {

        if (datos == null) {
            throw new IllegalArgumentException(
                    "Los datos del usuario son obligatorios"
            );
        }

        validarNombre(datos.getNombre());
        validarEmail(datos.getEmail());
        validarPassword(datos.getPassword());

        String emailNormalizado =
                datos.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new IllegalStateException(
                    "Ya existe un usuario registrado con ese email"
            );
        }

        RolEntity rol = obtenerRol(datos.getIdRol());

        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setNombre(datos.getNombre().trim());
        usuario.setEmail(emailNormalizado);

        // Nunca almacenar la contraseña en texto plano.
        usuario.setPasswordHash(
                passwordEncoder.encode(datos.getPassword())
        );

        usuario.setRol(rol);
        usuario.setActivo(true);

        UsuarioEntity guardado =
                usuarioRepository.save(usuario);

        return convertirAResponse(guardado);
    }

    // ACTUALIZAR USUARIO
    // La contraseña no se modifica mediante esta operación.
    public UsuarioResponse actualizarUsuario(
            Integer id,
            UsuarioRequest datos) {

        UsuarioEntity usuario = obtenerEntidadPorId(id);

        if (datos == null) {
            throw new IllegalArgumentException(
                    "Los datos del usuario son obligatorios"
            );
        }

        validarNombre(datos.getNombre());
        validarEmail(datos.getEmail());

        String emailNormalizado =
                datos.getEmail().trim().toLowerCase();

        usuarioRepository.findByEmail(emailNormalizado)
                .filter(otro ->
                        !otro.getIdUsuario().equals(id))
                .ifPresent(otro -> {
                    throw new IllegalStateException(
                            "El email ya está registrado por otro usuario"
                    );
                });

        RolEntity rol = obtenerRol(datos.getIdRol());

        usuario.setNombre(datos.getNombre().trim());
        usuario.setEmail(emailNormalizado);
        usuario.setRol(rol);

        // No utilizamos datos.getPassword() en la actualización.

        UsuarioEntity actualizado =
                usuarioRepository.save(usuario);

        return convertirAResponse(actualizado);
    }

    // ELIMINAR USUARIO
    public void eliminarUsuario(Integer id) {

        UsuarioEntity usuario = obtenerEntidadPorId(id);

        usuarioRepository.delete(usuario);
    }

    // ACTIVAR USUARIO
    public UsuarioResponse activarUsuario(Integer id) {

        UsuarioEntity usuario = obtenerEntidadPorId(id);

        if (Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalStateException(
                    "El usuario ya se encuentra activo"
            );
        }

        usuario.setActivo(true);

        return convertirAResponse(
                usuarioRepository.save(usuario)
        );
    }

    // DESACTIVAR USUARIO
    public UsuarioResponse desactivarUsuario(Integer id) {

        UsuarioEntity usuario = obtenerEntidadPorId(id);

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new IllegalStateException(
                    "El usuario ya se encuentra inactivo"
            );
        }

        usuario.setActivo(false);

        return convertirAResponse(
                usuarioRepository.save(usuario)
        );
    }

    // OBTENER ENTIDAD INTERNAMENTE
    private UsuarioEntity obtenerEntidadPorId(Integer id) {

        validarId(id);

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe un usuario con ID: " + id
                ));
    }

    // BUSCAR Y VALIDAR ROL
    private RolEntity obtenerRol(Integer idRol) {

        if (idRol == null || idRol <= 0) {
            throw new IllegalArgumentException(
                    "El ID del rol debe ser mayor que 0"
            );
        }

        return rolRepository.findById(idRol)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe un rol con ID: " + idRol
                ));
    }

    // CONVERTIR ENTITY A DTO DE RESPUESTA
    private UsuarioResponse convertirAResponse(
            UsuarioEntity usuario) {

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().getNombre(),
                usuario.getActivo()
        );
    }

    // VALIDACIONES
    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El ID del usuario debe ser mayor que 0"
            );
        }
    }

    private void validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario es obligatorio"
            );
        }

        if (nombre.trim().length() > 150) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar los 150 caracteres"
            );
        }
    }

    private void validarEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "El email es obligatorio"
            );
        }

        String normalizado = email.trim();

        if (normalizado.length() > 255) {
            throw new IllegalArgumentException(
                    "El email no puede superar los 255 caracteres"
            );
        }

        if (!normalizado.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "El formato del email no es válido"
            );
        }
    }

    private void validarPassword(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria"
            );
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }
    }
}
