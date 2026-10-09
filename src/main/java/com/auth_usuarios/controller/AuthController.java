
package com.auth_usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth_usuarios.dto.LoginRequest;
import com.auth_usuarios.dto.LoginResponse;
import com.auth_usuarios.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // INICIAR SESION
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest datos) {

        LoginResponse respuesta = authService.login(datos);

        return ResponseEntity.ok(respuesta);
    }
}
