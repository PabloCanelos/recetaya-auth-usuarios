# RecetaYa – Microservicio Auth/Usuarios

Microservicio REST desarrollado con **Java 23, Spring Boot 4.1.1 y MySQL Server 8.4**, encargado de la autenticación JWT y la gestión de usuarios del sistema RecetaYa.

## Tecnologías

- Java 23 y Spring Boot 4.1.1
- Spring Security y JWT
- Spring Data JPA / Hibernate
- MySQL Server 8.4
- Maven
- Postman
- Git y GitHub

## Funcionalidades

- Inicio de sesión mediante correo electrónico y contraseña.
- Generación de tokens JWT.
- CRUD de usuarios.
- Activación y desactivación de cuentas.
- Roles `MEDICO` y `FARMACEUTICO`.
- Contraseñas almacenadas mediante BCrypt.
- Persistencia de datos en MySQL.

## Requisitos previos

- JDK 21 o superior compatible.
- MySQL Server 8.4.
- Git.
- Postman para probar los endpoints.

El proyecto incluye Maven Wrapper, por lo que no requiere instalar Maven por separado.

## Instalación y ejecución

### 1. Clonar el repositorio

```powershell
git clone https://github.com/PabloCanelos/recetaya-auth-usuarios.git
cd recetaya-auth-usuarios
```

### 2. Crear la base de datos

Ejecutar en MySQL Workbench:

```sql
CREATE DATABASE IF NOT EXISTS db_usuarios;
```

La conexión predeterminada utiliza `localhost:3306` y la base de datos `db_usuarios`.

### 3. Configurar las variables de entorno

En PowerShell:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "CONTRASENA_DE_SU_MYSQL"
$env:JWT_SECRET = "ClaveDeEjemploRecetaYaJWT2026Segura123456789"
```

Reemplazar la contraseña y la clave JWT por valores propios. El usuario de MySQL debe tener permisos sobre `db_usuarios`.

Estas variables deben configurarse en la misma terminal donde se ejecutará el microservicio. No se deben publicar credenciales reales en GitHub.

### 4. Iniciar Spring Boot

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación se ejecutará en:

`http://localhost:8081`

En el primer inicio, Hibernate creará o actualizará las tablas mediante `spring.jpa.hibernate.ddl-auto=update`.

### 5. Inicializar los datos de demostración

Después del primer inicio de Spring Boot, abrir y ejecutar en MySQL Workbench el archivo incluido en el repositorio:

`inicializacion-demo.sql`

Este script incorpora los roles `MEDICO` y `FARMACEUTICO` y un usuario de demostración con contraseña almacenada mediante BCrypt.

## Prueba de autenticación

En Postman:

**POST** `http://localhost:8081/api/auth/login`

Body → raw → JSON:

```json
{
  "email": "demo@recetaya.cl",
  "password": "DemoRecetaYa2026!"
}
```

**Resultado esperado:** HTTP `200 OK` y un token JWT.

Para acceder a los endpoints protegidos, seleccionar **Authorization → Bearer Token** en Postman y pegar el token obtenido.

El token tiene una vigencia de 60 minutos.

## Endpoints disponibles

| Método | Endpoint | Función |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Buscar usuario por ID |
| GET | `/api/usuarios/email/{email}` | Buscar por correo |
| POST | `/api/usuarios` | Crear usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |
| PATCH | `/api/usuarios/{id}/activar` | Activar cuenta |
| PATCH | `/api/usuarios/{id}/desactivar` | Desactivar cuenta |

Todos los endpoints de usuarios requieren un JWT válido.

### Ejemplo: crear usuario

**POST** `http://localhost:8081/api/usuarios`

Authorization → Bearer Token

```json
{
  "nombre": "Usuario de Prueba",
  "email": "prueba@recetaya.cl",
  "password": "PruebaRecetaYa2026!",
  "idRol": 2
}
```

El `idRol` debe corresponder a un rol existente en la base de datos.

**Resultado esperado:** HTTP `201 Created`.

## Generación del JAR

Compilar el proyecto:

```powershell
.\mvnw.cmd clean package
```

Resultado esperado: `BUILD SUCCESS`.

Ejecutar el artefacto generado:

```powershell
java -jar target\ms-auth-usuarios-0.0.1-SNAPSHOT.jar
```

MySQL debe estar iniciado y las variables de entorno configuradas.

## Respuestas HTTP principales

| Código | Significado |
|---|---|
| 200 | Operación exitosa |
| 201 | Usuario creado |
| 204 | Usuario eliminado |
| 400 | Datos inválidos |
| 401 | Credenciales de inicio de sesión incorrectas |
| 403 | Acceso denegado |
| 404 | Recurso no encontrado |
| 409 | Conflicto de negocio |

## Arquitectura

El microservicio está organizado por capas: **Controller, Service, Repository, Entity, DTO y Security**.

Utiliza las entidades `Usuario` y `Rol`, relacionadas mediante una asociación de uno a muchos (1:N), con persistencia en la base de datos `db_usuarios`.

Las operaciones CRUD, la autenticación JWT, la conexión MySQL y la generación del JAR fueron probadas durante el desarrollo.

**Nota:** las credenciales de demostración y la configuración de permisos actual están destinadas a pruebas académicas, no a producción.

---

**Proyecto académico RecetaYa – Duoc UC.**