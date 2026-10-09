# RecetaYa – Microservicio de Autenticación y Usuarios

## 1. Descripción del proyecto

Este repositorio contiene el microservicio de **Autenticación y Usuarios** del sistema RecetaYa, desarrollado como parte de un proyecto académico de arquitectura de microservicios.

Su responsabilidad es gestionar las cuentas de usuario, autenticar credenciales y proporcionar mecanismos de autorización mediante JSON Web Tokens (JWT).

**Funcionalidades principales:**

- Autenticación mediante correo electrónico y contraseña.
- Generación y validación de tokens JWT.
- Registro, consulta, actualización y eliminación de usuarios (CRUD).
- Activación y desactivación de cuentas.
- Gestión de usuarios asociados a los roles `MEDICO` y `FARMACEUTICO`.
- Persistencia de información en MySQL Server 8.4.
- Protección de endpoints mediante Spring Security.

## 2. Tecnologías utilizadas

| Tecnología | Versión o finalidad |
|---|---|
| Java | JDK 21 o superior compatible |
| Spring Boot | 4.1.1 |
| Spring Web | API REST |
| Spring Security | Autenticación y autorización |
| JWT | Tokens de autenticación |
| Spring Data JPA | Acceso a datos |
| Hibernate | Persistencia y mapeo objeto-relacional |
| MySQL Server | **8.4** |
| Maven | Compilación y gestión de dependencias |
| Postman | Pruebas de endpoints |
| Git y GitHub | Control de versiones |

El desarrollo y las pruebas locales se realizaron con MySQL Server 8.4 y Java 23.

## 3. Arquitectura del microservicio

El proyecto utiliza una arquitectura por capas para separar las responsabilidades.

- **Controller:** recibe las solicitudes HTTP y entrega las respuestas REST.
- **Service:** implementa la lógica de negocio.
- **Repository:** gestiona las operaciones de persistencia mediante Spring Data JPA.
- **Entity:** representa las entidades de la base de datos.
- **DTO:** permite transferir información entre el cliente y la API.
- **Security:** contiene la configuración de seguridad, validación JWT y control de acceso.

### Modelo de datos

La base de datos se denomina `db_usuarios` y contiene dos entidades principales.

**Entidad Rol**

| Campo | Descripción |
|---|---|
| `id_rol` | Identificador del rol |
| `nombre` | Nombre del rol |

**Entidad Usuario**

| Campo | Descripción |
|---|---|
| `id_usuario` | Identificador del usuario |
| `nombre` | Nombre del usuario |
| `email` | Correo electrónico único |
| `password_hash` | Contraseña cifrada mediante hash BCrypt |
| `id_rol` | Clave foránea hacia Rol |
| `activo` | Estado de la cuenta |

La relación entre Rol y Usuario es **uno a muchos (1:N)**: un rol puede estar asociado a varios usuarios, mientras que cada usuario tiene un rol asignado.

## 4. Requisitos previos

Para ejecutar el microservicio se necesita:

- Java JDK 21 o superior compatible con Spring Boot 4.1.1.
- **MySQL Server 8.4**, versión utilizada durante el desarrollo.
- MySQL Workbench o un cliente SQL equivalente.
- Git para clonar el repositorio.
- Postman para probar la API.
- Windows PowerShell para seguir los comandos de este documento.

El proyecto incluye Maven Wrapper, por lo que no es obligatorio instalar Maven globalmente.

## 5. Clonar el repositorio

Abrir PowerShell y ejecutar:

```bash
git clone https://github.com/PabloCanelos/recetaya-auth-usuarios.git
```

Ingresar a la carpeta:

```bash
cd recetaya-auth-usuarios
```

## 6. Configuración de MySQL Server 8.4

### 6.1. Crear la base de datos

Abrir MySQL Workbench, conectarse al servidor MySQL 8.4 y ejecutar:

```sql
CREATE DATABASE IF NOT EXISTS db_usuarios;
```

La conexión configurada en el proyecto es:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_usuarios
```

Por defecto se utiliza el puerto `3306`.

Si MySQL utiliza otro puerto, debe ajustarse la URL en `src/main/resources/application.properties`.

### 6.2. Configurar las credenciales de conexión

El microservicio utiliza variables de entorno para evitar almacenar contraseñas reales dentro del código fuente.

| Variable | Descripción |
|---|---|
| `DB_USERNAME` | Usuario de MySQL con acceso a `db_usuarios` |
| `DB_PASSWORD` | Contraseña del usuario MySQL |
| `JWT_SECRET` | Clave secreta utilizada para firmar y validar los tokens JWT |

En PowerShell, configurar las variables antes de iniciar el microservicio:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "CONTRASENA_LOCAL_MYSQL"
$env:JWT_SECRET = "ClaveDeEjemploRecetaYaJWT2026Segura123456789"
```

**Instrucciones importantes:**

1. Reemplazar `root` si se utiliza otro usuario MySQL.
2. Reemplazar `CONTRASENA_LOCAL_MYSQL` por la contraseña real del usuario MySQL configurado en ese computador.
3. Configurar una clave JWT propia, larga y aleatoria.
4. Ejecutar estos comandos en la misma terminal desde la que se iniciará Spring Boot.

Las variables creadas mediante `$env:` se mantienen en la sesión actual de PowerShell. Si se abre una terminal nueva, deben configurarse nuevamente.

El usuario MySQL debe tener permisos para crear y modificar las tablas y ejecutar operaciones de lectura y escritura sobre `db_usuarios`.

**No es necesario utilizar las credenciales del desarrollador original.** Cada persona puede ejecutar el proyecto utilizando su propia instalación y sus propios usuarios de MySQL.

## 7. Iniciar el microservicio

Desde la carpeta raíz del proyecto, ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación se iniciará en:

```text
http://localhost:8081
```

Durante el primer inicio, Spring Data JPA y Hibernate crearán o actualizarán las tablas correspondientes a las entidades del proyecto.

Esto se encuentra configurado mediante:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Un inicio correcto mostrará mensajes similares a:

```text
Tomcat started on port 8081 (http)
Started MsAuthUsuariosApplication
```

Mantener la aplicación ejecutándose mientras se realizan las pruebas.

## 8. Inicialización de datos de demostración

El repositorio contiene el archivo:

```text
inicializacion-demo.sql
```

Este script permite incorporar los datos mínimos necesarios para realizar pruebas de autenticación en una base de datos recién creada.

### 8.1. Procedimiento

1. Crear la base de datos `db_usuarios`.
2. Configurar las variables de entorno.
3. Iniciar Spring Boot al menos una vez para que JPA genere las tablas.
4. Abrir MySQL Workbench.
5. Abrir `inicializacion-demo.sql`.
6. Ejecutar el script completo.

El script inserta los roles `MEDICO` y `FARMACEUTICO`, si todavía no existen, y un usuario de demostración.

### 8.2. Usuario de demostración

**Correo:**

```text
demo@recetaya.cl
```

**Contraseña:**

```text
DemoRecetaYa2026!
```

La contraseña se almacena en la base de datos mediante un hash BCrypt incluido en el script SQL.

Estas credenciales son exclusivamente para pruebas académicas y no deben utilizarse en producción.

## 9. Autenticación mediante JWT

### 9.1. Iniciar sesión

Abrir Postman y crear la siguiente solicitud:

**Método:** `POST`

**URL:**

```text
http://localhost:8081/api/auth/login
```

Seleccionar **Body → raw → JSON** e ingresar:

```json
{
  "email": "demo@recetaya.cl",
  "password": "DemoRecetaYa2026!"
}
```

**Resultado esperado:**

- HTTP `200 OK`.
- Respuesta que contiene un token JWT.

### 9.2. Utilizar el token

Para consumir los endpoints protegidos:

1. Copiar el token obtenido.
2. Abrir una nueva solicitud en Postman.
3. Seleccionar **Authorization → Bearer Token**.
4. Pegar el token.

También puede utilizarse la cabecera:

```http
Authorization: Bearer TOKEN_JWT
```

El token tiene una duración configurada de **60 minutos**.

Una vez expirado, es necesario autenticarse nuevamente para obtener otro token.

## 10. Endpoints de la API

La URL base del microservicio es:

```text
http://localhost:8081
```

### 10.1. Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Autenticar usuario y generar JWT |

Este endpoint es público y no requiere un token previo.

### 10.2. Gestión de usuarios

Los siguientes endpoints requieren un JWT válido.

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Consultar usuario por ID |
| GET | `/api/usuarios/email/{email}` | Consultar usuario por correo |
| POST | `/api/usuarios` | Registrar usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |
| PATCH | `/api/usuarios/{id}/activar` | Activar cuenta |
| PATCH | `/api/usuarios/{id}/desactivar` | Desactivar cuenta |

### 10.3. Ejemplo: registrar usuario

**Método:** `POST`

**URL:**

```text
http://localhost:8081/api/usuarios
```

**Autorización:** Bearer Token

**Body JSON:**

```json
{
  "nombre": "Usuario de Prueba",
  "email": "prueba@recetaya.cl",
  "password": "PruebaRecetaYa2026!",
  "idRol": 2
}
```

El campo `idRol` debe corresponder a un identificador existente en la tabla `rol`. Se recomienda consultar la tabla para verificar los identificadores asignados.

**Resultado esperado:** HTTP `201 Created`.

### 10.4. Ejemplo: consultar usuarios

**Método:** `GET`

**URL:**

```text
http://localhost:8081/api/usuarios
```

**Autorización:** Bearer Token

**Resultado esperado:** HTTP `200 OK`, con la información de los usuarios registrados.

### 10.5. Ejemplo: actualizar usuario

**Método:** `PUT`

**URL de ejemplo:**

```text
http://localhost:8081/api/usuarios/2
```

**Autorización:** Bearer Token

**Body JSON:**

```json
{
  "nombre": "Usuario Actualizado",
  "email": "actualizado@recetaya.cl",
  "password": "NuevaClaveRecetaYa2026!",
  "idRol": 2
}
```

El ID indicado debe corresponder a un usuario existente.

**Resultado esperado:** HTTP `200 OK`.

### 10.6. Ejemplo: eliminar usuario

**Método:** `DELETE`

**URL de ejemplo:**

```text
http://localhost:8081/api/usuarios/2
```

**Autorización:** Bearer Token

**Resultado esperado:** HTTP `204 No Content`.

Se recomienda realizar las pruebas de actualización y eliminación con usuarios creados específicamente para ese propósito.

## 11. Validaciones y códigos HTTP

La API utiliza códigos HTTP para comunicar el resultado de las operaciones.

| Código | Descripción |
|---|---|
| 200 OK | Consulta, autenticación o actualización exitosa |
| 201 Created | Usuario registrado correctamente |
| 204 No Content | Usuario eliminado correctamente |
| 400 Bad Request | Datos inválidos |
| 401 Unauthorized | Credenciales de inicio de sesión incorrectas |
| 403 Forbidden | Acceso denegado |
| 404 Not Found | Recurso no encontrado |
| 409 Conflict | Conflicto de negocio, como una cuenta desactivada |

Los códigos específicos dependen de la operación y de las validaciones implementadas.

## 12. Compilación y generación del JAR

El proyecto utiliza Maven para compilar el código y generar el artefacto ejecutable.

Desde la carpeta raíz, ejecutar:

```powershell
.\mvnw.cmd clean package
```

Si la compilación finaliza correctamente, aparecerá:

```text
BUILD SUCCESS
```

El archivo generado se encontrará en:

```text
target/ms-auth-usuarios-0.0.1-SNAPSHOT.jar
```

### 12.1. Ejecutar el archivo JAR

Con MySQL Server 8.4 iniciado y las variables de entorno configuradas, ejecutar:

```powershell
java -jar target\ms-auth-usuarios-0.0.1-SNAPSHOT.jar
```

La aplicación utilizará el puerto `8081`, siempre que se encuentre disponible.

**Importante:** las variables de entorno `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` también deben estar disponibles cuando se ejecuta directamente el JAR.

## 13. Pruebas funcionales realizadas

Durante el desarrollo del microservicio se verificaron las siguientes operaciones:

- Conexión con MySQL Server 8.4.
- Persistencia de usuarios mediante Spring Data JPA.
- Autenticación con correo y contraseña.
- Generación de tokens JWT.
- Consulta de usuarios mediante GET.
- Creación de usuarios mediante POST.
- Actualización de usuarios mediante PUT.
- Eliminación de usuarios mediante DELETE.
- Compilación mediante Maven.
- Generación y ejecución del artefacto JAR.

Las operaciones CRUD fueron comprobadas mediante Postman y la persistencia de los datos se verificó utilizando MySQL Workbench.

## 14. Solución de problemas frecuentes

### Error: `Could not resolve placeholder 'JWT_SECRET'`

**Causa:** no se ha configurado la variable de entorno `JWT_SECRET`.

**Solución:** definirla en PowerShell antes de ejecutar Spring Boot.

```powershell
$env:JWT_SECRET = "REEMPLAZAR_POR_UNA_CLAVE_LARGA_Y_ALEATORIA"
```

### Error: `Access denied for user ...@localhost`

**Causa:** MySQL rechaza las credenciales configuradas o el usuario no dispone del acceso necesario.

**Solución:**

- Comprobar que el usuario existe en MySQL Server 8.4.
- Verificar su contraseña.
- Revisar los permisos sobre `db_usuarios`.
- Confirmar que `DB_USERNAME` y `DB_PASSWORD` están correctamente configuradas en la terminal.

### Error: `Unable to determine Dialect without JDBC metadata`

Este mensaje puede aparecer cuando Hibernate no consigue conectarse a la base de datos.

**Solución:** revisar primero los errores de conexión MySQL que aparecen anteriormente en el registro. No es necesario agregar manualmente un dialecto si el problema real son las credenciales.

### Error: `ECONNREFUSED 127.0.0.1:8081`

**Causa:** Postman no consigue conectarse al servidor.

**Solución:** verificar que Spring Boot esté ejecutándose correctamente en el puerto `8081`.

## 15. Consideraciones de seguridad

- Las contraseñas de usuarios se almacenan mediante BCrypt.
- La autenticación se realiza mediante JWT.
- Los endpoints de gestión de usuarios requieren autenticación.
- Las credenciales de MySQL y la clave JWT no se almacenan directamente en el repositorio.
- Los roles implementados son `MEDICO` y `FARMACEUTICO`.
- El usuario y la contraseña de demostración están destinados únicamente a pruebas académicas.

La configuración actual permite operaciones de gestión de usuarios a cuentas autenticadas con los roles definidos. Para un entorno productivo se requerirían políticas de autorización más restrictivas, especialmente para la creación y eliminación de cuentas.

## 16. Contexto académico

Este microservicio forma parte de **RecetaYa**, una solución académica basada en arquitectura de microservicios.

El desarrollo busca demostrar competencias en Java, Spring Boot, API REST, seguridad JWT, persistencia relacional, arquitectura por capas, Maven y Git.

**Proyecto académico – Duoc UC.**
