
package com.auth_usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth_usuarios.dto.UsuarioRequest;
import com.auth_usuarios.dto.UsuarioResponse;
import com.auth_usuarios.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // LISTAR TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {

        List<UsuarioResponse> usuarios =
                usuarioService.listarUsuarios();

        return ResponseEntity.ok(usuarios);
    }

    // BUSCAR USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            @PathVariable Integer id) {

        UsuarioResponse usuario =
                usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuario);
    }

    // BUSCAR USUARIO POR EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(
            @PathVariable String email) {

        UsuarioResponse usuario =
                usuarioService.buscarPorEmail(email);

        return ResponseEntity.ok(usuario);
    }

    // CREAR USUARIO
    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @RequestBody UsuarioRequest datos) {

        UsuarioResponse nuevoUsuario =
                usuarioService.crearUsuario(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoUsuario);
    }

    // ACTUALIZAR USUARIO
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Integer id,
            @RequestBody UsuarioRequest datos) {

        UsuarioResponse usuarioActualizado =
                usuarioService.actualizarUsuario(id, datos);

        return ResponseEntity.ok(usuarioActualizado);
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Integer id) {

        usuarioService.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    // ACTIVAR USUARIO
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UsuarioResponse> activarUsuario(
            @PathVariable Integer id) {

        UsuarioResponse usuario =
                usuarioService.activarUsuario(id);

        return ResponseEntity.ok(usuario);
    }

    // DESACTIVAR USUARIO
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponse> desactivarUsuario(
            @PathVariable Integer id) {

        UsuarioResponse usuario =
                usuarioService.desactivarUsuario(id);

        return ResponseEntity.ok(usuario);
    }
}
