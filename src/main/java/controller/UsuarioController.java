package controller;

import entity.UsuarioEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Inyección de dependencias por constructor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // LISTAR TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarUsuarios() {

        List<UsuarioEntity> usuarios =
                usuarioService.listarUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    // BUSCAR USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> buscarPorId(
            @PathVariable Long id) {

        UsuarioEntity usuario =
                usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuario);
    }

    // BUSCAR USUARIO POR EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioEntity> buscarPorEmail(
            @PathVariable String email) {

        UsuarioEntity usuario =
                usuarioService.buscarPorEmail(email);

        return ResponseEntity.ok(usuario);
    }

    // CREAR USUARIO
    @PostMapping
    public ResponseEntity<UsuarioEntity> crearUsuario(
            @RequestBody UsuarioEntity usuario) {

        UsuarioEntity nuevoUsuario =
                usuarioService.crearUsuario(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoUsuario);
    }

    // ACTUALIZAR USUARIO
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioEntity usuario) {

        UsuarioEntity usuarioActualizado =
                usuarioService.actualizarUsuario(id, usuario);

        return ResponseEntity.ok(usuarioActualizado);
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminarUsuario(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // ACTIVAR USUARIO
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UsuarioEntity> activarUsuario(
            @PathVariable Long id) {

        UsuarioEntity usuario =
                usuarioService.activarUsuario(id);

        return ResponseEntity.ok(usuario);
    }

    // DESACTIVAR USUARIO
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioEntity> desactivarUsuario(
            @PathVariable Long id) {

        UsuarioEntity usuario =
                usuarioService.desactivarUsuario(id);

        return ResponseEntity.ok(usuario);
    }
}