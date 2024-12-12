# TCG Commerce API REST

## Descripción
Este proyecto trata sobre la implementación de una __API REST segura__ donde se compran y venden cartas del juego
de cartas de Pokemon:
__Trading Card Game (TCG)__.

En ella, los usuarios podrán comprar y vender cartas, sirviendo la app como página de compra.
Los administradores podrán insertar y eliminar cartas que cumplan los requisitos.

## Justificación
Esta aplicación es necesaria para que los usuarios puedan completar sus mazos con las cartas que quieran si que adeuden
a sus padres gastando dinero en sobres, a parte, esta herramienta online sirve para la inmediatez y facilidad de obtención
de estas cartas, sin que tengan que ir a eventos presenciales una vez cada lustro.

## Tablas
Estas son las tablas que contendrá:

1. Usuarios
2. Cartas
3. Transacciones

(3) Y estas tablas contendrán los siguientes campos:

1. **Usuarios**:
    - `id` (Long): Identificador único del usuario.
    - `username`(String): Nombre del usuario (único).
    - `password` (String): Contraseña del usuario (hasheada).
    - `rol` (String): Rol del usuario, puede ser `USER` o `ADMIN`

2. **Cartas**:
    - `id` (Long): Identificador único de la carta en concreto.
    - `nombre`(String): Nombre de la carta (único).
    - `tipo`(String): Tipo de la carta.
    - `vida`(int): Puntos de vida de la carta.
    - `ataque`(int): Puntos de ataque de la carta.
    - `vendedores` (List<Usuario>): Lista de usuarios que venden la carta.

3. **Transacciones**:
    - `id` (Long): Identificador único de la transacción.
    - `precio` (double): Precio de la transacción.
    - `id_vendedor`(Long): Identificador único del usuario vendedor.
    - `id_comprador`(Long): Identificador único del usuario comprador.
    - `id_carta`(Long): Identificador único de la carta.

## Endpoints
1. **Autenticación**:
   - `POST /usuarios/login`: Permite al usuario autenticarse.
      - **RUTA PÚBLICA** Todas las peticiones a este endpoint deben permitirse.
      - **Entrada**: JSON con `nombre` y `password`.
      - **Salida**: Token de la sesión si las credenciales son válidas.

2. **Registro**:
   - `POST /usuarios/register`: Permite al usuario registrarse.
      - **RUTA PÚBLICA** Todas las peticiones a este endpoint deben permitirse.
      - **Entrada**: JSON con `nombre`, `password` y `roles`.
      - **Salida esperada**: JSON con el usuario registrado.

3. **Info de Usuario**:
   - `GET /usuarios/{nombre}`: Permite al usuario consultar su información.
      - **RUTA PROTEGIDA** Sólo los usuarios autenticados pueden acceder a este recurso.
      - Usuarios con rol ADMIN pueden acceder a este recurso.
      - Usuarios con el mismo nombre que el que se consulta pueden acceder a este recurso.
      - Usuarios con rol USER con nombre diferente a este recurso *NO* pueden acceder al mismo.
      - **Salida esperada**: JSON con el usuario consultado.
   - `PUT /usuarios/{nombre}`: Permite actualizar la información de un usuario.
   - **RUTA PROTEGIDA** Sólo los usuarios autenticados pueden acceder a este recurso.
      - Usuarios con rol ADMIN pueden acceder a este recurso.
      - Usuarios con el mismo nombre que el que se consulta pueden acceder a este recurso.
      - Usuarios con rol USER con nombre diferente a este recurso *NO* pueden acceder al mismo.

4. **Gestión de Cartas**:
   - *RUTAS PROTEGIDAS* Todas las rutas requieren que el usuario esté autenticado para acceder a las mismas.
   - `GET /cartas/{id}`: Devuelve la información de una carta.
      - **Entrada**: ID de la carta.
      - **Salida**: JSON con `nombre`, `tipo`, `vida`, `ataque` y `vendedores`.
   - `GET /cartas/byNombre/{nombre}`: Devuelve la información de una carta.
      - **Entrada**: nombre de la carta.
      - **Salida**: JSON con `nombre`, `tipo`, `vida`, `ataque` y `vendedores`.
   - `GET /cartas/asc`: Devuelve todos las cartas almacenadas en la base de datos ordenadas ascendentemente por su *nombre*.
      - **Salida**: Lista con las cartas : JSON con `nombre`, `tipo`, `vida`, `ataque` y `vendedores`.
   - `GET /cartas/desc`: Devuelve todos las cartas almacenadas en la base de datos ordenadas descendentemente por su *nombre*.
      - **Salida**: Lista con los cartas : JSON con `nombre`, `tipo`, `vida`, `ataque` y `vendedores`.
   - `POST /cartas`: Permite insertar una nueva carta.
      - *SÓLO ADMIN*: Sólo los usuarios con ROL ADMIN pueden acceder a este recurso.
      - **Entrada**: JSON con `nombre`, `tipo`, `vida` y `ataque`.
   - `DELETE /cartas/{id}`: Permite eliminar una carta.
      - *SÓLO ADMIN*: Sólo los usuarios con ROL ADMIN pueden acceder a este recurso.
   - `PUT /cartas/{id}`: Permite actualizar la información de una carta.
      - *SÓLO ADMIN*: Sólo los usuarios con ROL ADMIN pueden acceder a este recurso.

5. **Gestión de Transacciones**:
   - *RUTAS PROTEGIDAS* Todas las rutas requieren que el usuario esté autenticado para acceder a las mismas.
   - Usuarios con rol ADMIN pueden acceder a este recurso.
   - Usuarios con el mismo id (vendedor o comprado) que el que se consulta, pueden acceder a este recurso.
   - Usuarios con rol USER con nombre diferente a este recurso *NO* pueden acceder al mismo.
   - `GET /transacciones/{id}`: Devuelve la información de una transacción.
     - **Entrada**: ID de la transacción.
     - **Salida**: JSON con `id`, `precio`, `id_vendedor`, `id_comprador` y `id_carta`.

## Lógica de negocio
1. **Usuarios**
   - `id` autoincremental.
   - `username` único e imprescindible (not null).
   - `password` >=6 carácteres alfanuméricos, sin símbolos. Comprobar que coindicen password en el login con la almacenada en la bd
                y al registro: las dos password.

2. **Cartas**
   - `id` adjudicado (no autoincremental).
   - `nombre` único e imprescindible (not null).
   - `tipo` que sea uno de estos: `FUEGO`, `AGUA`, `RAYO`, `LUCHA`, `DRAGON`, `PSIQUICO`, `PLANTA`, `OSCURO`, `METAL`, `NORMAL`.
   - `vida` entre 10 y 300.
   - `ataque` entre 10 y 300.
   - `vendedores` que existan en la base de datos.

3. **Transacciones**
   - `id` autoincremental.
   - `precio` mayor que 0.
   - `id_vendedor` que esté en la base de datos. No puede ser el mismo que el id_comprador.
   - `id_comprador` que esté en la base de datos. No puede ser el mismo que el id_vendedor.
   - `id_carta` que esté en la base de datos.

## Excepciones
1. `400 Bad Request`: Cuando un usuario forme mal una petición, como un error de sintaxis.
    - La lanzaré en los endpoints que intente ir que no existan, así como en cartas, usuarios y demas que no existan (por equivocación en su escritura).
2. `401 Unathorized`: Cuando un usuario intente acceder a un endpoint que no le corresponde, por rol o usuario.
    - La lanzaré cuando intente acceder a endpoints solo donde puede acceder usuarios con rol ADMIN o usuarios propios de ese endpoints
    - Tanto en los GET como en los DELETE y UPDATE (PUT), ya que solo pueden acceder a esas funciones el ADMIN.
3. `403 Forbidden`: Cuando intente acceder a endpoints donde es necesario que se esté logueado (básicamente en todos si no está logueado primeramente).
4. `404 Not Found`: Cuando intente ir a endpoints que no existen en la base de datos porque hayan sido borradas o haya habido un cambio en la URI.
5. `500 Internal Server Error`: Como error general cuando se produzca uno que no haya contemplado en la APP.

## Seguridad
1. **Autenticación mediante JWT**
   - Los usuarios deben autenticarse mediante el endpoint POST /usuarios/login, enviando su nombre y contraseña.
     Si las credenciales son válidas:
     Se genera un token que debe enviarse al cliente para que lo almacene.

2. **Acceso a endpoints**
   - Control de acceso a distintos endpoints, según usuario y roles (ver en cada endpoint).