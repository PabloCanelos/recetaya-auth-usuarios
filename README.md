# RecetaYa — Microservicio de Autenticación y Usuarios

Microservicio REST encargado del inicio de sesión, la autenticación mediante JWT y la gestión de usuarios del sistema RecetaYa.

## 1. Requisitos

Necesitas tener instalado:

- Java JDK compatible con el proyecto.
- MySQL Server 8.4.
- Git.
- Postman para probar la API.

No necesitas instalar Maven por separado, porque el proyecto incluye Maven Wrapper.

## 2. Descargar el proyecto

Abre PowerShell y ejecuta:

```powershell
git clone https://github.com/PabloCanelos/recetaya-auth-usuarios.git
cd recetaya-auth-usuarios
```

## 3. Crear la base de datos

Abre MySQL Workbench, conéctate a tu servidor MySQL y ejecuta:

```sql
CREATE DATABASE IF NOT EXISTS db_usuarios;
```

## 4. Configurar la conexión

Desde PowerShell, dentro de la carpeta del proyecto, ejecuta:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = Read-Host "Contraseña de MySQL"
$env:JWT_SECRET = Read-Host "Clave secreta para JWT"
```

Introduce tu contraseña real de MySQL y una clave secreta propia para JWT.

El usuario de MySQL debe tener permisos sobre `db_usuarios`. Estas variables deben configurarse en la misma terminal donde ejecutarás el microservicio.

No publiques contraseñas ni claves secretas reales en GitHub.

## 5. Ejecutar el microservicio

En la misma terminal, ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación estará disponible en:

http://localhost:8081

En el primer inicio, Hibernate creará o actualizará las tablas automáticamente, según la configuración del proyecto.

Mantén abierta la terminal mientras utilizas la API.

## 6. Cargar los datos de demostración

Después de iniciar Spring Boot por primera vez:

1. Abre MySQL Workbench.
2. Abre el archivo `inicializacion-demo.sql` incluido en el repositorio.
3. Selecciona la base de datos `db_usuarios` y ejecuta el script.

El script crea los roles `MEDICO` y `FARMACEUTICO` y registra un usuario de demostración si todavía no existe.

## 7. Probar el inicio de sesión

Abre Postman y crea una solicitud con estos datos:

**Método:** `POST`

**URL:** `http://localhost:8081/api/auth/login`

Selecciona **Body → raw → JSON** e introduce:

```json
{
  "email": "demo@recetaya.cl",
  "password": "DemoRecetaYa2026!"
}
```

Si el usuario de demostración y su contraseña están correctamente configurados, recibirás una respuesta `200 OK` con un token JWT.

Para utilizar los endpoints protegidos, selecciona **Authorization → Bearer Token** en Postman y pega el token obtenido.

El token tiene una vigencia de 60 minutos.

## 8. Operaciones disponibles

| Método | Endpoint | Función |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Buscar usuario por ID |
| GET | `/api/usuarios/email/{email}` | Buscar usuario por correo |
| POST | `/api/usuarios` | Crear usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |
| PATCH | `/api/usuarios/{id}/activar` | Activar cuenta |
| PATCH | `/api/usuarios/{id}/desactivar` | Desactivar cuenta |

Las operaciones de usuarios requieren un token JWT válido.

## 9. Generar el archivo JAR

Para compilar y empaquetar el proyecto, ejecuta:

```powershell
.\mvnw.cmd clean package
```

Si la compilación termina correctamente, Maven mostrará `BUILD SUCCESS`.

El archivo ejecutable se generará en la carpeta `target`.

Para ejecutarlo:

```powershell
java -jar target\ms-auth-usuarios-0.0.1-SNAPSHOT.jar
```

MySQL debe estar iniciado y las variables de entorno configuradas en esa terminal.

## 10. Arquitectura

El microservicio utiliza una arquitectura por capas:

- **Controller:** recibe las solicitudes HTTP.
- **Service:** ejecuta la lógica de negocio.
- **Repository:** accede a la base de datos.
- **Entity:** representa los datos almacenados.
- **DTO:** organiza los datos de entrada y salida.
- **Security:** gestiona la autenticación y protege los endpoints.

La persistencia se realiza en MySQL. Las contraseñas se almacenan mediante BCrypt y la autenticación utiliza tokens JWT.

## 11. Respuestas HTTP

| Código | Significado |
|---|---|
| 200 | Operación exitosa |
| 201 | Usuario creado |
| 204 | Recurso eliminado |
| 400 | Datos inválidos |
| 401 | Credenciales incorrectas o autenticación no válida |
| 403 | Acceso denegado |
| 404 | Recurso no encontrado |
| 409 | Conflicto de negocio |

**Nota:** las credenciales de demostración son exclusivamente para pruebas académicas. No deben utilizarse en producción.

Proyecto académico RecetaYa — Duoc UC.