
package com.auth_usuarios.dto;

public class LoginResponse {

    private String token;
    private String tipo;
    private Integer idUsuario;
    private String nombre;
    private String rol;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tipo,
                         Integer idUsuario, String nombre,
                         String rol) {
        this.token = token;
        this.tipo = tipo;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
