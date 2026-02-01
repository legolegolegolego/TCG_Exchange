# TCG Exchange

## Descripción
TCG Exchange es una aplicación web orientada al intercambio de cartas del juego **Pokémon: Trading Card Game (TCG)** entre usuarios registrados.

La aplicación está compuesta por dos partes principales:

- **Backend** implementado como una API REST, responsable de la lógica de negocio, gestión de usuarios, cartas, intercambios y seguridad.
- **Frontend web**, que actúa como interfaz visual y permite a los usuarios interactuar de forma intuitiva con la plataforma.

La aplicación permite a los usuarios:
- Registrarse e iniciar sesión de forma segura.
- Explorar el catálogo de cartas disponibles.
- Publicar cartas propias para intercambio.
- Consultar perfiles de otros usuarios y las cartas que ofrecen.
- Proponer, aceptar o rechazar intercambios.
- Gestionar sus intercambios activos desde una sección personal.

El sistema está diseñado para fomentar el intercambio directo entre jugadores, sin transacciones económicas, priorizando la colaboración y el coleccionismo responsable.

---

## Justificación
El intercambio de cartas Pokémon TCG presenta varias limitaciones:
- Dependencia del azar para obtener cartas específicas.
- Falta de plataformas especializadas para intercambios entre jugadores.
- Dificultad para organizar y gestionar colecciones personales.
- Necesidad de encuentros presenciales para realizar intercambios.

### ¿Por qué es necesaria esta aplicación web?
- **Centralización**: reúne a jugadores interesados en intercambiar cartas.
- **Organización**: permite gestionar cartas, perfiles e intercambios de forma estructurada.
- **Seguridad**: garantiza que los intercambios se realicen entre usuarios autenticados.
- **Accesibilidad**: disponible desde cualquier navegador y en cualquier momento.

TCG Exchange ofrece una solución moderna y digital a un proceso tradicionalmente informal.

---

## Modelo de Datos

### Usuarios
Representa a los usuarios registrados en la plataforma.
- `id (Long)`: identificador único.
- `username (String)`: nombre de usuario único.
- `password (String)`: contraseña cifrada mediante hash.
- `rol (Enum/String)`: USER o ADMIN.
- `cartasFisicas (List<CartaFisica>)`: cartas físicas asociadas.

### CartaModelo
Modelo conceptual de una carta del catálogo.
- `id (Long)`
- `nombre (String)`
- `tipoCarta (Enum)`: POKEMON, ENTRENADOR.
- `rareza (Enum)`
- `imagenUrl (String)`
- `tipoPokemon (Enum, opcional)`
- `evolucion (Enum, opcional)`

### CartaFisica
Carta real ofrecida para intercambio.
- `id (Long)`
- `estadoCarta (Enum)`
- `disponible (boolean)`
- `imagenUrl (String, opcional)`
- `usuario (Usuario)`
- `cartaModelo (CartaModelo)`

### Intercambio
Propuesta de intercambio entre usuarios.
- `id (Long)`
- `usuarioOrigen (Usuario)`
- `usuarioDestino (Usuario)`
- `cartaOrigen (CartaFisica)`
- `cartaDestino (CartaFisica)`
- `estado (Enum)`: PENDIENTE, ACEPTADO, RECHAZADO.

---

## Diagrama Entidad-Relación
![Imagen Diagrama Entidad-Relación](./images/ER-TCG(proyecto).png)

---

## Lógica de Negocio

### Usuarios
- El `username` es único y obligatorio.
- La contraseña se almacena cifrada.
- Solo el propio usuario o un ADMIN pueden modificar el perfil.
- No es posible registrarse como ADMIN.

### CartaModelo
- Solo un ADMIN puede crear, modificar o eliminar.
- El `id` no es autoincremental (corresponde al número oficial de carta).
- Puede existir sin cartas físicas asociadas.

### CartaFisica
- Solo el propietario puede crear, modificar o eliminar.
- Un ADMIN puede eliminar cartas de cualquier usuario.
- Las cartas físicas que hayan participado o estén participando en intercambios no se eliminan de la BD.
- Si una carta ha estado involucrada en un intercambio `RECHAZADO`, al solicitar el usuario su eliminación, pasa a 
estado `disponible = false`, quedando cerrada para futuros intercambios.
- Al aceptar un intercambio, la carta pasa a `disponible = false`.
- No se transfiere la propiedad de la carta.

### Intercambio
- El estado inicial es `PENDIENTE`.
- Solo el usuario destino puede aceptar o rechazar.
- Al aceptar, las cartas pasan a no disponibles.
- Un usuario solo puede consultar intercambios en los que participa.
- Un ADMIN puede consultar cualquier intercambio.

---

## Seguridad

### Autenticación mediante JWT
- Autenticación mediante `POST /auth/login`.
- El servidor devuelve un token JWT.
- El token debe enviarse en la cabecera `Authorization: Bearer <token>`.
- Los tokens tienen expiración.

### Acceso a endpoints
- Control basado en roles (USER, ADMIN) y propiedad del recurso.
- Un ADMIN puede consultar cualquier recurso.
- Un ADMIN no puede modificar recursos reservados al propietario.

### Cifrado de contraseñas
- Contraseñas almacenadas mediante hashing seguro.
- Nunca se devuelven contraseñas en texto plano.

### Validaciones de seguridad
- Username único.
- Contraseña mínima de 6 caracteres.
- No se permiten intercambios consigo mismo.
- Validación de existencia y disponibilidad de cartas y usuarios.

---

## Endpoints

### Autenticación
- `POST /auth/login` – Login de usuario (ruta pública).
- `POST /auth/register` – Registro de usuario (ruta pública).

### Gestión de Usuarios
- `GET /usuarios` – Solo ADMIN.
- `GET /usuarios/id/{id}` – Solo ADMIN.
- `GET /usuarios/{username}` – ADMIN o propio usuario.
- `PUT /usuarios/{username}` – ADMIN o propio usuario.
- `DELETE /usuarios/{username}` – ADMIN o propio usuario.

### Gestión de Cartas Modelo
- `GET /cartas-modelo` – Público.
- `GET /cartas-modelo/{id}` – Público.
- `POST /cartas-modelo` – Solo ADMIN.
- `PUT /cartas-modelo/{id}` – Solo ADMIN.
- `DELETE /cartas-modelo/{id}` – Solo ADMIN.

### Gestión de Cartas Físicas
- `GET /cartas-fisicas/usuario/{username}` – Público o protegido según disponibilidad.
- `GET /cartas-fisicas/{id}` – Público.
- `POST /cartas-fisicas` – Usuario autenticado.
- `PUT /cartas-fisicas/{id}` – Propietario.
- `DELETE /cartas-fisicas/{id}` – Propietario o ADMIN.

### Gestión de Intercambios
- `GET /intercambios/usuario/{username}` – ADMIN o propio usuario.
- `GET /intercambios/{id}` – ADMIN o participantes.
- `POST /intercambios` – Usuario autenticado.
- `PUT /intercambios/{id}/aceptar` – Usuario destino.
- `PUT /intercambios/{id}/rechazar` – Usuario destino.

---

## Gestión de Errores
- **400 Bad Request**: datos inválidos.
- **401 Unauthorized**: no autenticado.
- **403 Forbidden**: sin permisos.
- **404 Not Found**: recurso inexistente.
- **409 Duplicate**: duplicados o conflictos de estado.
- **500 Internal Server Error**: error inesperado del servidor.

---

## Tecnologías Utilizadas

### Dependencias incluidas
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **Spring Security**
- **MySQL Connector/J**

### Software utilizado
- IntelliJ IDEA
- Insomnia
- XAMPP (MySQL + phpMyAdmin)
- Git y GitHub
- Navegador web (Brave)

### Descripción de tecnologías y su propósito
- **Spring Boot**: base del backend REST.
- **Spring Data JPA**: persistencia y acceso a datos.
- **Spring Security**: autenticación JWT y autorización.
- **MySQL Connector/J**: conexión con MySQL.
- **Insomnia**: pruebas de la API REST.
- **XAMPP**: entorno local de base de datos.
- **GitHub**: control de versiones.
