package com.auth_usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth_usuarios.entity.RolEntity;
import com.auth_usuarios.repository.RolRepository;

import java.util.List;

@Service
public class RolService {
    @Autowired
    private  RolRepository rolRepository;



    // LISTAR TODOS
    public List<RolEntity> listarRoles() {
        return rolRepository.findAll();
    }

    // BUSCAR POR ID
    public RolEntity buscarPorId(Integer id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del rol debe ser válido");
        }

        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El rol no existe"));
    }

    // BUSCAR POR NOMBRE
    public RolEntity buscarPorNombre(String nombre) {

        validarNombre(nombre);

        return rolRepository.findByNombre(nombre.trim().toUpperCase())
                .orElseThrow(() ->
                        new IllegalArgumentException("El rol no existe"));
    }

    // CREAR
    public RolEntity crearRol(RolEntity rol) {

        if (rol == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        validarNombre(rol.getNombre());

        String nombreNormalizado = rol.getNombre().trim().toUpperCase();

        if (rolRepository.findByNombre(nombreNormalizado).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un rol con el nombre " + nombreNormalizado
            );
        }

        rol.setIdRol(null);
        rol.setNombre(nombreNormalizado);

        return rolRepository.save(rol);
    }

    // ACTUALIZAR
    public RolEntity actualizarRol(Integer id, RolEntity datosRol) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del rol debe ser válido");
        }

        if (datosRol == null) {
            throw new IllegalArgumentException("Los datos del rol son obligatorios");
        }

        validarNombre(datosRol.getNombre());

        RolEntity rolExistente = rolRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El rol no existe"));

        String nombreNormalizado =
                datosRol.getNombre().trim().toUpperCase();

        rolRepository.findByNombre(nombreNormalizado)
                .filter(rol -> !rol.getIdRol().equals(id))
                .ifPresent(rol -> {
                    throw new IllegalArgumentException(
                            "Ya existe otro rol con ese nombre"
                    );
                });

        rolExistente.setNombre(nombreNormalizado);

        return rolRepository.save(rolExistente);
    }

    // ELIMINAR
    public void eliminarRol(Integer id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del rol debe ser válido");
        }

        RolEntity rol = rolRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El rol no existe"));

        if (rol.getUsuarios() != null && !rol.getUsuarios().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar el rol porque tiene usuarios asociados"
            );
        }

        rolRepository.delete(rol);
    }

    // VALIDACIÓN INTERNA
    private void validarNombre(String nombre) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del rol es obligatorio"
            );
        }

        if (nombre.trim().length() > 40) {
            throw new IllegalArgumentException(
                    "El nombre del rol no puede superar los 40 caracteres"
            );
        }
    }
}