package entity;

import jakarta.persistence.Entity;

@Entity 
public class UsuarioEntity {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String passwordHash;
    private Long idRol;
    private Boolean activo;
}
