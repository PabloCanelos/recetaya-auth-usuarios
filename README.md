# recetaya-auth-usuarios
# RecetaYa — Autenticación y Usuarios

Microservicio responsable de identificar a los usuarios, gestionar sus roles y emitir el JWT que se utilizará para acceder a RecetaYa.

## Contexto del proyecto

RecetaYa tiene **cuatro microservicios Spring Boot**, cada uno con su propia base de datos: Autenticación y Usuarios, Recetas, Inventario y Dispensación. Este repositorio contiene únicamente **Autenticación y Usuarios**.

El caso oficial exige dos roles:

- `MEDICO`: emite recetas.
- `FARMACEUTICO`: consulta recetas con stock reservado y realiza la dispensación.

En la arquitectura entregada, el acceso externo pasa por **Amazon API Gateway**. Este microservicio atiende la autenticación y emite el JWT. Amazon API Gateway no forma parte de este repositorio ni es un quinto microservicio.

## Configuración acordada

| Concepto | Valor |
|---|---|
| Motor de base de datos | MySQL Server 8.4 |
| Base de datos propia | `db_usuarios` |
| Puerto local del microservicio | `8081` |
| Puerto reservado para la entrada local | `8080` |

El puerto `8081` corresponde a la aplicación Spring Boot. El puerto de conexión a MySQL se configura por separado según el entorno de cada integrante.

## Responsabilidades

Este microservicio debe:

1. Mantener usuarios y roles en `db_usuarios`.
2. Permitir el inicio de sesión de un usuario activo.
3. Comprobar su contraseña contra el hash almacenado.
4. Emitir un JWT que permita identificar al usuario y su rol.
5. Entregar la información necesaria para aplicar las reglas de acceso según el rol.

La arquitectura entregada muestra la ruta **`POST /auth/login`**. Los formatos de solicitud y respuesta se acordarán antes de integrar este servicio con los demás.

## Clases de entidad y modelo relacional

Crear **dos clases de entidad**: `Rol` y `Usuario`. Cada una corresponde a una tabla de `db_usuarios`.

### Clase `Rol` — tabla `ROL`

| Atributo Java | Tipo Java | Columna MySQL | Tipo MySQL | Restricción |
|---|---|---|---|---|
| `id` | `Integer` | `id_rol` | `INT` | Clave primaria, autoincremental |
| `nombre` | `String` | `nombre` | `VARCHAR(40)` | Obligatorio y único |

Registrar como valores iniciales de `nombre`:

- `MEDICO`
- `FARMACEUTICO`

### Clase `Usuario` — tabla `USUARIO`

| Atributo Java | Tipo Java | Columna MySQL | Tipo MySQL | Restricción |
|---|---|---|---|---|
| `id` | `Integer` | `id_usuario` | `INT` | Clave primaria, autoincremental |
| `nombre` | `String` | `nombre` | `VARCHAR(150)` | Obligatorio |
| `email` | `String` | `email` | `VARCHAR(255)` | Obligatorio y único |
| `passwordHash` | `String` | `password_hash` | `VARCHAR(255)` | Obligatorio |
| `rol` | `Rol` | `id_rol` | `INT` | Clave foránea obligatoria a `ROL.id_rol` |
| `activo` | `Boolean` | `activo` | `BOOLEAN` | Obligatorio |

**Relación:** un `Rol` puede estar asociado a muchos `Usuario`; cada `Usuario` tiene un `Rol`. La clave foránea se encuentra en `USUARIO.id_rol`. En Java, `Usuario.rol` representa esa relación.

`password_hash` guarda únicamente el **hash** de la contraseña. Nunca guardar contraseñas legibles ni devolver el hash en las respuestas de la API.

## Reglas de implementación compartidas

- Usar `INT` en MySQL e `Integer` en Java para los identificadores. No usar `BIGINT` ni `Long`.
- Mantener las tablas `ROL` y `USUARIO` exclusivamente en `db_usuarios`. Los demás microservicios conservarán el identificador del usuario cuando lo necesiten, sin crear claves foráneas hacia esta base de datos.
- No agregar precios ni campos `FLOAT`: el caso oficial no contempla cálculos monetarios.
- Configurar `server.port=8081`.
- Mantener las credenciales de MySQL y la clave usada para firmar los JWT fuera del repositorio. El README puede indicar los nombres de las variables de configuración, pero no publicar sus valores secretos.

## Alcance de lo acordado

**Exigido por el caso y la arquitectura entregada:** autenticación y autorización por rol, roles Médico y Farmacéutico, acceso externo mediante Amazon API Gateway y uso de JWT según el diagrama.

**Definido por la propuesta del equipo para implementarlo:** tablas `ROL` y `USUARIO`, sus columnas y relación, inicio de sesión mediante `email`, campo `activo`, tipos de datos indicados arriba, MySQL Server 8.4 y puerto local `8081`.

Este servicio no registra recetas, stock ni dispensaciones. Esos datos pertenecen a los otros tres microservicios.
