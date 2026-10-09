package com.auth_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth_usuarios.entity.RolEntity;

import java.util.Optional;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {

    Optional<RolEntity> findByNombre(String nombre);
}