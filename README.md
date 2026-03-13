# TCG Exchange

## Descripción
TCG Exchange es una aplicación web orientada al intercambio de cartas del juego **Pokémon: Trading Card Game (TCG)** entre usuarios registrados.

La aplicación está compuesta por dos partes principales:

- **Backend** implementado como una API REST, responsable de la lógica de negocio, gestión de usuarios, cartas, intercambios y seguridad.
- **Frontend web**, que actúa como interfaz visual y permite a los usuarios interactuar de forma intuitiva con la plataforma.

La aplicación permite a los usuarios:
- Registrarse e iniciar sesión de forma segura.
- Verificar el correo electrónico tras el registro.
- Recuperar la contraseña mediante enlace enviado al email.
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
- `email (String)`: correo electrónico del usuario (único).
- `password (String)`: contraseña cifrada mediante hash.
- `roles (String)`: USER o ADMIN.
- `desactivado (boolean)`:
  - `false` (por defecto): cuenta activa y operativa.
  - `true`: cuenta desactivada; el usuario no puede autenticarse ni operar en el sistema, pero sus datos e intercambios se conservan.
- `emailVerificado (boolean)`:
  - `false` (por defecto): el usuario aún no ha confirmado su correo electrónico.
  - `true`: el usuario ha verificado su correo electrónico mediante el enlace enviado al registrarse.
- `cartasFisicas (List<CartaFisica>)`: cartas físicas asociadas.
- `direccion (Direccion)`: dirección física de un usuario.

### CartaModelo
Modelo conceptual de una carta del catálogo.
- `id (Long)`: identificador único autogenerado, corresponde al número oficial del modelo de carta.
- `numero (Long)`: número oficial de la carta dentro del catálogo (único).
- `nombre (String)`: nombre de la carta
- `tipoCarta (Enum)`: POKEMON, ENTRENADOR.
- `rareza (Enum)`: COMUN, INFRECUENTE, RARA, RARA_HOLO.
- `imagenUrl (String)`: url de la imagen oficial de la carta.
- `tipoPokemon (Enum, opcional)`: PLANTA, FUEGO, AGUA, ELECTRICO, PSIQUICO, LUCHA, INCOLORO.
- `evolucion (Enum, opcional)`: BASICO, FASE_1, FASE_2.
- `activo (boolean)`:
  - `true` (por defecto): carta visible y utilizable en el sistema.
  - `false`: carta desactivada; no puede asociarse a nuevas cartas físicas.

### CartaFisica
Carta real ofrecida para intercambio.
- `id (Long)`: identificador único autogenerado.
- `version (Long)`: control de concurrencia optimista de JPA; se incrementa automáticamente en cada actualización.
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
- `direccionOrigen (String)`: dirección de usuarioOrigen.
- `direccionDestino (String)`: dirección de usuarioDestino.
- `estado (Enum)`: PENDIENTE, ACEPTADO, RECHAZADO.

### VerificationToken
Token temporal utilizado para operaciones sensibles relacionadas con la cuenta.
- `id (Long)`: identificador único autogenerado.
- `token (String)`: cadena única generada mediante UUID.
- `tipo (Enum)`: EMAIL_VERIFICATION, PASSWORD_RESET.
- `usuario (Usuario)`: usuario al que pertenece el token.
- `expiracion (LocalDateTime)`: fecha y hora límite de validez.
- `usado (boolean)`:
  - `false` (por defecto): el token no ha sido utilizado.
  - `true`: el token ya ha sido utilizado y no puede reutilizarse.
- `fechaCreacion (LocalDateTime)`: momento en el que se generó el token.


### Direccion
Representa la dirección física asociada a un usuario.
- `id (Long)`: identificador único autogenerado.
- `usuario (Usuario)`: usuario propietario de la dirección.
- `calleYNumero (String)`: calle y número de la dirección.
- `pisoYPuerta (String)`: piso, puerta o apartamento.
- `codigoPostal (String)`: código postal.
- `ciudad (String)`: ciudad de la dirección.
- `pais (String)`: país de la dirección.

---

## Diagrama Entidad-Relación
![Imagen Diagrama Entidad-Relación](./images/ER-TCG(proyecto).png)

---

## Lógica de Negocio

### Usuarios
- El `username` es único y obligatorio.
- El `email` es obligatorio, único y debe cumplir un formato válido (usuario@dominio.ext).
- La contraseña es obligatoria.
- La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial
- Al actualizar la contraseña, la nueva no puede ser la misma que la actual.
- Solo el propio usuario o un ADMIN pueden modificar el perfil.
- No es posible registrarse como ADMIN.
- El atributo `emailVerificado` gestiona la confirmación del correo electrónico:
  - `false` (por defecto): el usuario no ha confirmado su correo.
  - `true`: el usuario ha verificado su dirección de email.
- Un usuario no puede iniciar sesión si `emailVerificado = false`.
- El atributo `desactivado` gestiona el estado de la cuenta:
  - `false` (por defecto): cuenta activa.
  - `true`: cuenta desactivada.
- Cuando un usuario solicita eliminar su cuenta:
  - Si no ha participado en ningún intercambio (PENDIENTE, ACEPTADO o RECHAZADO), se elimina físicamente de la base de datos.
  - Si ha participado en algún intercambio, la cuenta no se elimina físicamente:
    - Se marca como `desactivado = true`.
    - Las cartas físicas disponibles:
      - Se eliminan si no han participado en intercambios.
      - Si han participado en intercambios:
        - Pasan a `disponible = false`. 
        - Sus intercambios en estado `PENDIENTE` se rechazan automáticamente.
    - Las cartas físicas no disponibles se mantienen sin cambios y no se eliminan físicamente.
- Un usuario desactivado no puede iniciar sesión ni acceder a endpoints protegidos, pero sus datos e intercambios se conservan para mantener la integridad histórica.
- Un usuario solo puede modificar o eliminar (desactivar) su propio perfil.
- Un ADMIN puede acceder y gestionar cualquier usuario.

### CartaModelo
- Solo un ADMIN puede crear, modificar o eliminar cartas modelo.
- Una carta modelo puede existir sin cartas físicas asociadas.
- No se puede actualizar una carta modelo inactiva (`activo = true`).
- El atributo `numero` es único y representa el número oficial de la carta.
- Una carta modelo solo puede eliminarse físicamente si no tiene cartas físicas asociadas. Si tiene cartas físicas asociadas:
  - Se marca como `activo = false`.
  - Los intercambios en estado `PENDIENTE` relacionados con cartas físicas asociadas con la carta modelo pasan a `RECHAZADO`.
  - Todas las cartas físicas asociadas pasan a `disponible = false`.

### CartaFisica
- Solo el usuario propietario puede crear, modificar o eliminar sus cartas físicas.
- Un ADMIN puede eliminar cartas físicas de cualquier usuario.
- No se pueden crear ni actualizar cartas físicas asociándolas a una carta modelo con `activo = false`.
- Un usuario no puede crear una carta física si no tiene una dirección asociada.
- Las cartas físicas que hayan participado o estén participando en intercambios no se eliminan de la base de datos.
- Si una carta ha participado en algún intercambio (cualquier estado) no puede eliminarse físicamente, por motivos históricos, 
en su lugar: se marca como disponible = false, y si hubieran intercambios `PENDIENTE`s, se rechazarían automáticamente.
- No se pueden eliminar cartas no disponibles.
- Al aceptar un intercambio, la carta pasa automáticamente a `disponible = false`.
- No se transfiere la propiedad de la carta al aceptar un intercambio.
- Una carta aceptada no puede reutilizarse en otros intercambios.
- Al actualizar una carta física, el usuario no puede modificar manualmente su disponibilidad.
- Una carta física puede participar simultáneamente en varios intercambios en estado `PENDIENTE`.
- Una carta física solo puede formar parte de un único intercambio `ACEPTADO`.

### Intercambio
- El estado inicial de un intercambio es `PENDIENTE`.
- Solo el usuario destino puede aceptar o rechazar un intercambio.
- Antes de aceptar un intercambio se valida nuevamente que ambas cartas siguen disponibles.
- Antes de aceptar un intercambio, se verifica que usuarioOrigen y usuarioDestino tengan dirección registrada.
- Al aceptar un intercambio:
  - Se valida que ambos usuarios tienen una dirección registrada.
  - El estado pasa a `ACEPTADO`.
  - Las cartas físicas involucradas pasan a no disponibles (`disponible = false`).
  - Se setean `direccionOrigen` y `direccionDestino` con la información formateada de cada usuario.
- Al rechazar un intercambio:
  - El estado pasa a `RECHAZADO`.
  - Las cartas físicas continúan disponibles.
- No se pueden eliminar intercambios, por cuestiones de: integridad, trazabilidad, consistencia y seguridad.
- No se permite crear intercambios con cartas no disponibles.
- Un usuario solo puede consultar intercambios en los que participa.
- Un ADMIN puede consultar cualquier intercambio.
- No se permite crear un intercambio si ya existe otro en estado PENDIENTE con la misma cartaOrigen y cartaDestino,
ni tampoco si existe el mismo intercambio en sentido inverso (cartaOrigen ↔ cartaDestino).
- Un intercambio solo puede pasar de `PENDIENTE` a `ACEPTADO` o `RECHAZADO`; una vez aceptado o rechazado, su estado no se puede modificar.

### VerificationToken
- Se utiliza para operaciones de verificación de email y recuperación de contraseña.
- El token se genera automáticamente mediante UUID.
- Tiene una expiración por defecto de 24 horas desde su creación.
- Un token solo puede utilizarse una vez.
- Si se utiliza correctamente, se marca como `usado = true`.
- Antes de validar un token se comprueba que:
  - Existe en la base de datos.
  - No está usado.
  - No ha expirado.
- Para un mismo usuario y tipo de token solo puede existir uno en el sistema.
- Al generar uno nuevo se eliminan los tokens anteriores del mismo tipo.

**Verificación de email**
- Al registrarse un usuario se genera un token `EMAIL_VERIFICATION`.
- Se envía un enlace de verificación al email del usuario.
- Si el token es válido:
  - `emailVerificado` pasa a `true`.
  - el token se marca como usado.

**Recuperación de contraseña**
- El usuario solicita restablecer su contraseña.
- Se genera un token `PASSWORD_RESET`.
- Se envía un enlace al email del usuario.
- Si el token es válido:
  - se permite establecer una nueva contraseña
  - el token se marca como usado.

### Direccion
- Cada usuario solo puede tener una dirección asociada.
- La dirección está vinculada directamente al usuario y no puede asociarse a otro usuario.
- El codigoPostal no puede superar 10 caracteres/dígitos.
- Solo el propietario de la dirección puede modificarla.
- No se permite eliminar direcciones; solo se pueden actualizar.
- Al crear o actualizar una dirección, los valores se validan para no permitir campos nulos o vacíos en los obligatorios.

---

## Seguridad

### Autenticación mediante JWT
- Autenticación mediante `POST /auth/login`.
- El servidor devuelve un token JWT.
- El token debe enviarse en la cabecera `Authorization: Bearer <token>`.
- Los tokens tienen expiración.
- El rol del usuario autenticado se obtiene exclusivamente del token validado por el servidor y no puede ser modificado desde el cliente.

### Acceso a endpoints
- Control basado en roles (USER, ADMIN) y propiedad del recurso.
- Un ADMIN puede consultar cualquier recurso.
- Un ADMIN no puede modificar recursos reservados al propietario.
- En los endpoints públicos, los recursos inactivos o restringidos no son accesibles aunque el cliente conozca su identificador interno.
- Cualquier endpoint no marcado como público requiere autenticación mediante JWT.

### Cifrado de contraseñas
- Contraseñas almacenadas mediante hashing seguro.
- Nunca se devuelven contraseñas en texto plano.

### Validaciones de seguridad
- Username único.
- Solo pueden acceder a la app usuarios con rol `USER` o `ADMIN`.
- Un usuario no puede registrarse como `ADMIN` desde la app.
- La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial
- Se valida que el email tenga un formato correcto.
- Solo los usuarios con email verificado pueden iniciar sesión.
- No se permiten intercambios consigo mismo.
- Validación de existencia y disponibilidad de cartas y usuarios.
- No se permite asociar una carta física a una carta modelo inactiva.
- No se permiten eliminar intercambios, por cuestiones de: integridad, trazabilidad, consistencia y seguridad.
- Las respuestas de la API no exponen información sensible como contraseñas, hashes o datos internos del sistema.
- Antes de realizar operaciones críticas, el sistema vuelve a verificar el estado actual de los recursos implicados para evitar inconsistencias.
- Los tokens utilizados para verificar email o recuperar contraseña:
  - deben existir en el sistema,
  - no pueden estar usados,
  - no pueden haber expirado,
  - solo pueden utilizarse una vez.

---

## Endpoints

### Autenticación
- `POST /auth/login` – Público.
- `POST /auth/register` – Público.
- `GET /auth/verify` – Público.
- `POST /auth/verify/resend` – Público.
- `POST /auth/password/forgot` – Público.
- `POST /auth/password/reset` – Público.

### Gestión de Usuarios
- `GET /usuarios` – Solo ADMIN.
- `GET /usuarios/id/{id}` – Solo ADMIN.
- `GET /usuarios/username/{username}` – Solo ADMIN.
- `PUT /usuarios/{id}/username` – ADMIN o propio usuario.
- `PUT /usuarios/{id}/password` – ADMIN o propio usuario.
- `DELETE /usuarios/{id}` – ADMIN o propio usuario.

### Gestión de Cartas Modelo
- `GET /cartas-modelo`
  - Público: solo cartas activas.
  - ADMIN: puede ver cartas activas e inactivas.
- `GET /cartas-modelo/{id}` Público.
- `GET /cartas-modelo/{id}/usuarios` Público.
- `POST /cartas-modelo` – Solo ADMIN.
- `PUT /cartas-modelo/{id}` – Solo ADMIN.
- `DELETE /cartas-modelo/{id}` – Solo ADMIN.

### Gestión de Cartas Físicas
- `GET /cartas-fisicas/usuario/{username}` Público.
- `GET /cartas-fisicas/usuario/{username}/no-disponibles` ADMIN o propio usuario.
- `GET /cartas-fisicas/{id}` Público.
- `POST /cartas-fisicas` – Usuario autenticado.
- `PUT /cartas-fisicas/{id}` – Propietario.
- `DELETE /cartas-fisicas/{id}` – Propietario o ADMIN.

### Gestión de Intercambios
- `GET /intercambios/usuario/{username}` – ADMIN o propio usuario.
- `GET /intercambios/{id}` – ADMIN o participantes.
- `POST /intercambios` – Usuario autenticado.
- `PUT /intercambios/{id}/aceptar` – Usuario destino.
- `PUT /intercambios/{id}/rechazar` – Usuario destino.

### Gestión de Direcciones
- `GET /direccion/{username}` - ADMIN o propio usuario
- `POST /direccion` - Usuario autenticado
- `PUT /direccion` - Usuario autenticado

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

---

Proyecto académico desarrollado con fines educativos

Esta aplicación no tiene fines comerciales ni está destinada a uso público.

Las imágenes, nombres y referencias a Pokémon TCG se utilizan únicamente con fines de aprendizaje y demostración, sin intención de infringir derechos de autor.

Pokémon y Pokémon TCG son marcas registradas de Nintendo, Game Freak y Creatures Inc.

© 2026 TCG Exchange. 