-- Inicializacion de Auth/Usuarios para la EP2
-- Ejecutar despues de iniciar Spring Boot por primera vez,
-- para que JPA haya creado las tablas.

USE db_usuarios;

-- Crear roles si no existen
INSERT INTO rol (nombre)
SELECT 'MEDICO'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre = 'MEDICO'
);

INSERT INTO rol (nombre)
SELECT 'FARMACEUTICO'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre = 'FARMACEUTICO'
);

-- Crear usuario de demostracion
INSERT INTO usuario (
    nombre,
    email,
    password_hash,
    id_rol,
    activo
)
SELECT
    'Usuario Demo EP2',
    'demo@recetaya.cl',
    '$2a$10$/mbiwgyvixgYEBXPIYrHl.3U3HKrQqEtA8eVjzitcyqenKC5bEb/e',
    r.id_rol,
    1
FROM rol r
WHERE r.nombre = 'MEDICO'
AND NOT EXISTS (
    SELECT 1
    FROM usuario
    WHERE email = 'demo@recetaya.cl'
);