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
- `id (Long)`: identificador único autogenerado.
- `username (String)`: nombre de usuario único.
- `password (String)`: contraseña cifrada mediante hash.
- `roles (String)`: USER o ADMIN.
- `desactivado (boolean)`:
  - `false` (por defecto): cuenta activa y operativa.
  - `true`: cuenta desactivada; el usuario no puede autenticarse ni operar en el sistema, pero sus datos e intercambios se conservan.
- `cartasFisicas (List<CartaFisica>)`: cartas físicas asociadas.

### CartaModelo
Modelo conceptual de una carta del catálogo.
- `id (Long)`: identificador único no autogenerado, corresponde al número oficial del modelo de carta.
- `nombre (String)`: nombre de la carta
- `tipoCarta (Enum)`: POKEMON, ENTRENADOR.
- `rareza (Enum)`: COMUN, INFRECUENTE, RARA, RARA_HOLO.
- `imagenUrl (String)`: url de la imagen oficial de la carta.
- `tipoPokemon (Enum, opcional)`: PLANTA, FUEGO, AGUA, ELECTRICO, PSIQUICO, LUCHA, INCOLORO.
- `evolucion (Enum, opcional)`: BASICO, FASE_1, FASE_2.

### CartaFisica
Carta real ofrecida para intercambio.
- `id (Long)`: identificador único autogenerado.
- `estadoCarta (Enum)`: EXCELENTE, ACEPTABLE.
- `disponible (boolean)`:
  - `true` (por defecto): la carta está publicada y puede participar en intercambios.
  - `false`: la carta no está disponible para intercambiar.
- `imagenUrl (String)`: url de la imagen real de la carta.
- `usuario (Usuario)`: usuario autenticado que crea la carta física.
- `cartaModelo (CartaModelo)`: modelo conceptual al que pertenece la carta física.

### Intercambio
Propuesta de intercambio entre usuarios.
- `id (Long)`: identificador único autogenerado.
- `usuarioOrigen (Usuario)`: usuario que inicia el intercambio.
- `usuarioDestino (Usuario)`: usuario que recibe la propuesta de intercambio.
- `cartaOrigen (CartaFisica)`: carta ofrecida por usuarioOrigen.
- `cartaDestino (CartaFisica)`: carta solicitada que pertenece a usuarioDestino.
- `estado (Enum)`: PENDIENTE, ACEPTADO, RECHAZADO.

---

## Diagrama Entidad-Relación
![Imagen Diagrama Entidad-Relación](./images/ER-TCG(proyecto).png)

---

## Lógica de Negocio

### Usuarios
- El `username` es único y obligatorio.
- La contraseña es obligatoria y debe tener una longitud mínima de 6 caracteres.
- Solo el propio usuario o un ADMIN pueden modificar el perfil.
- No es posible registrarse como ADMIN.
- El atributo `desactivado` gestiona el estado de la cuenta:
  - `false` (por defecto): cuenta activa.
  - `true`: cuenta desactivada.
- Cuando un usuario solicita eliminar su cuenta:
  - Si no ha participado en ningún intercambio (PENDIENTE, ACEPTADO o RECHAZADO), se elimina físicamente de la base de datos.
  - Si ha participado en algún intercambio, la cuenta no se elimina físicamente y se marca como `desactivado = true`.
- Un usuario desactivado no puede iniciar sesión ni acceder a endpoints protegidos, pero sus datos e intercambios se conservan para mantener la integridad histórica.
- Un usuario solo puede modificar o eliminar (desactivar) su propio perfil.
- Un ADMIN puede acceder y gestionar cualquier usuario.

### CartaModelo
- Solo un ADMIN puede crear, modificar o eliminar cartas modelo.
- El `id` no es autogenerado y corresponde al número oficial de la carta.
- Una carta modelo puede existir sin cartas físicas asociadas.

### CartaFisica
- Solo el usuario propietario puede crear, modificar o eliminar sus cartas físicas.
- Un ADMIN puede eliminar cartas físicas de cualquier usuario.
- Las cartas físicas que hayan participado o estén participando en intercambios no se eliminan de la base de datos.
- Si una carta ha estado involucrada en un intercambio `RECHAZADO`, al solicitar el usuario su eliminación,
la carta pasa a estado `disponible = false`, quedando cerrada para futuros intercambios y manteniéndose por motivos históricos.
- Al aceptar un intercambio, la carta pasa automáticamente a `disponible = false`.
- No se transfiere la propiedad de la carta al aceptar un intercambio.
- Una carta aceptada no puede reutilizarse en otros intercambios.
- Al actualizar una carta física, el usuario no puede modificar manualmente su disponibilidad.
- Una carta física puede participar simultáneamente en varios intercambios en estado `PENDIENTE`.
- Una carta física solo puede formar parte de un único intercambio `ACEPTADO`.

### Intercambio
- El estado inicial de un intercambio es `PENDIENTE`.
- Solo el usuario destino puede aceptar o rechazar un intercambio.
- Al aceptar un intercambio:
  - El estado pasa a `ACEPTADO`.
  - Las cartas físicas involucradas pasan a no disponibles (`disponible = false`).
- Al rechazar un intercambio:
  - El estado pasa a `RECHAZADO`.
  - Las cartas físicas continúan disponibles.
- Al eliminar un intercambio:
  - Las cartas (y su disponibilidad) y usuarios involucrados no se ven alterados.
- No se permite crear intercambios con cartas no disponibles.
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
- `GET /cartas-fisicas/usuario/{username}`
  - Si `disponible = true`: Público.
  - Si `disponible = false`: Propietario o ADMIN
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
- `DELETE /intercambios/{id}` – Solo ADMIN.

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
