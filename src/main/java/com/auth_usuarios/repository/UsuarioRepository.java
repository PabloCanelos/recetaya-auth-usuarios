package com.auth_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth_usuarios.entity.UsuarioEntity;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}