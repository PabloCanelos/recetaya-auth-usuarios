package com.auth_usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth_usuarios.entity.RolEntity;
import com.auth_usuarios.service.RolService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private  RolService rolService;



    // LISTAR TODOS LOS ROLES
    @GetMapping
    public ResponseEntity<List<RolEntity>> listarRoles() {

        List<RolEntity> roles = rolService.listarRoles();

        return ResponseEntity.ok(roles);
    }

    // BUSCAR ROL POR ID
    @GetMapping("/{id}")
    public ResponseEntity<RolEntity> buscarPorId(
            @PathVariable Integer id) {

        RolEntity rol = rolService.buscarPorId(id);

        return ResponseEntity.ok(rol);
    }

    // BUSCAR ROL POR NOMBRE
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolEntity> buscarPorNombre(
            @PathVariable String nombre) {

        RolEntity rol = rolService.buscarPorNombre(nombre);

        return ResponseEntity.ok(rol);
    }

    // CREAR ROL
    @PostMapping
    public ResponseEntity<RolEntity> crearRol(
            @RequestBody RolEntity rol) {

        RolEntity nuevoRol = rolService.crearRol(rol);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoRol);
    }

    // ACTUALIZAR ROL
    @PutMapping("/{id}")
    public ResponseEntity<RolEntity> actualizarRol(
            @PathVariable Integer id,
            @RequestBody RolEntity rol) {

        RolEntity rolActualizado =
                rolService.actualizarRol(id, rol);

        return ResponseEntity.ok(rolActualizado);
    }

    // ELIMINAR ROL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(
            @PathVariable Integer id) {

        rolService.eliminarRol(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}