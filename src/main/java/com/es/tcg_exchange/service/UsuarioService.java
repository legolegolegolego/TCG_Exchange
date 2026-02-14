package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.*;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.DuplicateException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.repository.IntercambioRepository;
import com.es.tcg_exchange.repository.UsuarioRepository;
import com.es.tcg_exchange.utils.Mapper;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IntercambioRepository intercambioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // no hace falta inyectarlo si no es componente
//    @Autowired
//    private Mapper mapper;

//    para el login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario No encontrado"));

        /* RETORNAMOS UN USERDETAILS
        El método loadUserByUsername nos fuerza a devolver un objeto de tipo UserDetails.
        Tenemos que convertir nuestro objeto de tipo Usuario a un objeto de tipo UserDetails
        ¡No os preocupéis, esto es siempre igual!
         */
        List<GrantedAuthority> authorities = Arrays.stream(usuario.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());

        UserDetails userDetails = User // User pertenece a SpringSecurity
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRoles())
                .disabled(usuario.isDesactivado()) // comprueba si el usuario esta desactivado
                .build();

        return userDetails;
    }

    /**
     * Registra un usuario en la BD
     * @param usuarioRegisterDTO
     * @return UsuarioDetailDTO
     */
    public UsuarioDetailDTO registerUser(UsuarioRegisterDTO usuarioRegisterDTO) {

        // Esto no hace falta ni es buena práctica (puede fallar si dos registros llegan al mismo tiempo),
        // basta con tener unique en username model y capturar DataIntegrityViolationException
        // Compruebo que el usuario no existe en la base de datos
//        if (usuarioRepository.findByUsername(usuarioRegisterDTO.getUsername()).isPresent()) {
//            throw new DuplicateException("El nombre de usuario ya existe");
//        }

        if (usuarioRegisterDTO.getUsername() == null ||
                usuarioRegisterDTO.getUsername().isBlank()) {
            throw new BadRequestException("El username es obligatorio");
        }

        // Logica de pass
        if (usuarioRegisterDTO.getPassword().length() < 6){
            throw new BadRequestException("La longitud de la contraseña debe ser superior o igual da 6 caracteres");
        }

        //lo quito porque me da problemas el regex
//        // Compruebo que es alfanumérica sin símbolos
//        if (usuarioRegisterDTO.getPassword().matches("^[a-zA-Z0-9]+$")) {
//            throw new BadRequestException("La contraseña debe ser alfanumérica (solo letras y números, sin símbolos)");
//        }

        // Compruebo que ambas contraseñas coinciden
        if (!usuarioRegisterDTO.getPassword().equals(usuarioRegisterDTO.getPassword2())) {
            throw new BadRequestException("Ambas contraseñas deben coincidir");
        }

        // Ya me aseguro del rol inicializandolo por defecto en USER desde el model
        // y el usuario no tiene opción de elegir
//        if (!usuarioRegisterDTO.getRoles().equals("USER") && !usuarioRegisterDTO.getRoles().equals("ADMIN")){
//            throw new BadRequestException("Roles inválidos");
//        }

        try {
            Usuario newUsuario = Mapper.usuarioRegisterDTOToModel(usuarioRegisterDTO);

            newUsuario.setPassword(passwordEncoder.encode(usuarioRegisterDTO.getPassword()));

            usuarioRepository.save(newUsuario);

            return Mapper.usuarioToDetailDTO(newUsuario);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("El nombre de usuario ya existe");
        }
    }

    // obtener todos los usuarios de la bd
    public List<UsuarioDetailDTO> getAll(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return Mapper.usuariosToDetailDTO(usuarios);
    }

    // metodo interno
    private Usuario findByIdOrThrow(Long id) {
        if (id == null) {
            throw new BadRequestException("El id no puede ser null");
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con id " + id + " no encontrado"));
    }

    // encuentra usuario por id y retorna mapeado a fulldto
    public UsuarioFullDTO findByIdToDTO(Long id) {
        Usuario usuario = findByIdOrThrow(id);
        return Mapper.usuarioToFullDTO(usuario);
    }

    // encuentra usuario por id y retorna usuario modelo
    public Usuario findByIdToModel(Long id) {
        return findByIdOrThrow(id);
    }

    // busca por username y devuelve usuariofulldto
    public UsuarioFullDTO findByUsername(String nombre) {

        if (nombre.isEmpty() || nombre.isBlank()){
            throw new BadRequestException("El nombre no puede estar vacío");
        }

        Usuario usuario = usuarioRepository
                .findByUsername(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario con nombre " + nombre + " no encontrado"));

        return Mapper.usuarioToFullDTO(usuario);

    }

    // actualizar username
    public UsernameUpdateDTO updateUsername(
            Long id,
            String nuevoUsername,
            Authentication authentication) {

        if (id == null) {
            throw new BadRequestException("El id no puede ser null");
        }

        if (nuevoUsername == null || nuevoUsername.isBlank()) {
            throw new BadRequestException("El nuevo username no puede estar vacío");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuario con id " + id + " no encontrado")
                );

        SecurityUtils.checkAdminOrSelf(authentication, usuario.getUsername());

        if (usuario.getUsername().equals(nuevoUsername)) {
            throw new BadRequestException("El nuevo username no puede ser igual al actual");
        }


        usuario.setUsername(nuevoUsername);

        try {
            Usuario saved = usuarioRepository.save(usuario);
            return new UsernameUpdateDTO(saved.getUsername());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("El nombre de usuario ya existe");
        }
    }

    // actualizar password
    public void updatePassword(
            Long id,
            PasswordUpdateDTO dto,
            Authentication authentication) {

        if (id == null) {
            throw new BadRequestException("El id no puede ser null");
        }

        if (dto.getPasswordNueva() == null || dto.getPasswordNueva().length() < 6) {
            throw new BadRequestException(
                    "La nueva contraseña debe tener al menos 6 caracteres"
            );
        }

        if (!dto.getPasswordNueva().equals(dto.getPasswordNueva2())) {
            throw new BadRequestException(
                    "Las nuevas contraseñas deben coincidir"
            );
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuario con id " + id + " no encontrado")
                );

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Si NO es admin, debe validar password actual
        if (!isAdmin) {
            if (!passwordEncoder.matches(
                    dto.getPasswordActual(),
                    usuario.getPassword())) {

                throw new UnauthorizedException("La contraseña actual es incorrecta");
            }
        }

        // Permisos
        SecurityUtils.checkAdminOrSelf(authentication, usuario.getUsername());

        // Importante hacer esto después de comprobar permisos para no exponer contraseña actual
        if (passwordEncoder.matches(dto.getPasswordNueva(), usuario.getPassword())) {
            throw new BadRequestException("La nueva contraseña no puede ser igual a la actual");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));

        usuarioRepository.save(usuario);
    }

    // borrar usuario
    public void deleteUser(Long id){

        // Buscamos la entidad directamente, validando null y not found
        Usuario usuario = findByIdToModel(id);

        // Comprobar si ha participado en algún intercambio
        boolean haParticipado = intercambioRepository.existsByUsuarioOrigenOrUsuarioDestino(usuario, usuario);
        if (haParticipado) {
            // Desactivar usuario en lugar de borrarlo
            usuario.setDesactivado(true);
            usuarioRepository.save(usuario);
        } else {
            // Borrado físico
            usuarioRepository.delete(usuario);
        }

    }
}
