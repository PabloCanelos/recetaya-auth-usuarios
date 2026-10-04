package service;

import entity.RolEntity;
import entity.UsuarioEntity;
import org.springframework.stereotype.Service;
import repository.RolRepository;
import repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    // Inyección de dependencias por constructor
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    // LISTAR TODOS
    public List<UsuarioEntity> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // BUSCAR POR ID
    public UsuarioEntity buscarPorId(Integer id) {

        validarId(id);

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un usuario con ID: " + id
                        )
                );
    }

    // BUSCAR POR EMAIL
    public UsuarioEntity buscarPorEmail(String email) {

        validarEmail(email);

        return usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un usuario con email: " + email
                        )
                );
    }

    // CREAR USUARIO
    public UsuarioEntity crearUsuario(UsuarioEntity usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Los datos del usuario son obligatorios"
            );
        }

        validarNombre(usuario.getNombre());
        validarEmail(usuario.getEmail());
        validarPasswordHash(usuario.getPasswordHash());

        String emailNormalizado =
                usuario.getEmail().trim().toLowerCase();

        // El email debe ser único
        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario registrado con ese email"
            );
        }

        // El usuario obligatoriamente debe tener un rol
        if (usuario.getRol() == null ||
                usuario.getRol().getIdRol() == null) {

            throw new IllegalArgumentException(
                    "El usuario debe tener un rol"
            );
        }

        Integer idRol = usuario.getRol().getIdRol();

        if (idRol <= 0) {
            throw new IllegalArgumentException(
                    "El ID del rol debe ser mayor que 0"
            );
        }

        // Verificamos que el rol realmente exista en BD
        RolEntity rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un rol con ID: " + idRol
                        )
                );

        // En creación, el ID lo genera MySQL
        usuario.setIdUsuario(null);

        usuario.setNombre(usuario.getNombre().trim());
        usuario.setEmail(emailNormalizado);
        usuario.setRol(rol);

        // Si no se especifica estado, queda activo
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        return usuarioRepository.save(usuario);
    }

    // ACTUALIZAR USUARIO
    public UsuarioEntity actualizarUsuario(
            Integer id,
            UsuarioEntity datosUsuario) {

        validarId(id);

        if (datosUsuario == null) {
            throw new IllegalArgumentException(
                    "Los datos del usuario son obligatorios"
            );
        }

        validarNombre(datosUsuario.getNombre());
        validarEmail(datosUsuario.getEmail());

        UsuarioEntity usuarioExistente =
                usuarioRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe un usuario con ID: " + id
                                )
                        );

        String emailNormalizado =
                datosUsuario.getEmail().trim().toLowerCase();

        // Verificar que el nuevo email no pertenezca
        // a otro usuario
        usuarioRepository.findByEmail(emailNormalizado)
                .filter(usuario ->
                        !usuario.getIdUsuario().equals(id))
                .ifPresent(usuario -> {
                    throw new IllegalArgumentException(
                            "El email ya está registrado por otro usuario"
                    );
                });

        // Validar nuevo rol
        if (datosUsuario.getRol() == null ||
                datosUsuario.getRol().getIdRol() == null) {

            throw new IllegalArgumentException(
                    "El usuario debe tener un rol"
            );
        }

        Integer idRol = datosUsuario.getRol().getIdRol();

        if (idRol <= 0) {
            throw new IllegalArgumentException(
                    "El ID del rol debe ser mayor que 0"
            );
        }

        RolEntity rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un rol con ID: " + idRol
                        )
                );

        usuarioExistente.setNombre(
                datosUsuario.getNombre().trim()
        );

        usuarioExistente.setEmail(emailNormalizado);

        usuarioExistente.setRol(rol);

        if (datosUsuario.getActivo() != null) {
            usuarioExistente.setActivo(
                    datosUsuario.getActivo()
            );
        }

        /*
         * Por ahora NO modificamos passwordHash aquí.
         * La contraseña tendrá posteriormente su propio
         * tratamiento mediante BCrypt.
         */

        return usuarioRepository.save(usuarioExistente);
    }

    // ELIMINAR USUARIO
    public void eliminarUsuario(Integer id) {

        validarId(id);

        UsuarioEntity usuario =
                usuarioRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe un usuario con ID: " + id
                                )
                        );

        usuarioRepository.delete(usuario);
    }

    // ACTIVAR USUARIO
    public UsuarioEntity activarUsuario(Integer id) {

        UsuarioEntity usuario = buscarPorId(id);

        if (Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalStateException(
                    "El usuario ya se encuentra activo"
            );
        }

        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    // DESACTIVAR USUARIO
    public UsuarioEntity desactivarUsuario(Integer id) {

        UsuarioEntity usuario = buscarPorId(id);

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new IllegalStateException(
                    "El usuario ya se encuentra inactivo"
            );
        }

        usuario.setActivo(false);

        return usuarioRepository.save(usuario);
    }

    // ==============================
    // VALIDACIONES INTERNAS
    // ==============================

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

        if (nombre.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "El nombre no puede superar los 100 caracteres"
            );
        }
    }

    private void validarEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "El email es obligatorio"
            );
        }

        String emailNormalizado = email.trim();

        if (emailNormalizado.length() > 150) {
            throw new IllegalArgumentException(
                    "El email no puede superar los 150 caracteres"
            );
        }

        if (!emailNormalizado.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "El formato del email no es válido"
            );
        }
    }

    private void validarPasswordHash(String passwordHash) {

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria"
            );
        }
    }
}