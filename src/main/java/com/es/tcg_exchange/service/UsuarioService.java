package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.UsuarioDetailDTO;
import com.es.tcg_exchange.dto.UsuarioFullDTO;
import com.es.tcg_exchange.dto.UsuarioRegisterDTO;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.DuplicateException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.repository.UsuarioRepository;
import com.es.tcg_exchange.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    // buscar x id
    public UsuarioFullDTO findById(Long id){
        if (id == null){
            throw new BadRequestException("El id no puede ser null");
        }
        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con id " + id + " no encontrado"));
        return Mapper.usuarioToFullDTO(usuario);
    }

    // buscar x nombre
    public UsuarioFullDTO findByUsername(String nombre) {

        if (nombre.isEmpty() || nombre.isBlank()){
            throw new BadRequestException("El nombre no puede estar vacío");
        }

        Usuario usuario = usuarioRepository
                .findByUsername(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario con nombre " + nombre + " no encontrado"));

        return Mapper.usuarioToFullDTO(usuario);

    }


    public UsuarioFullDTO updateUser(String nombre, UsuarioFullDTO usuarioActualizado){
        // a partir de ahora solo pongo isBlank, ya que según la info que he encontrado
        // es redundante poner los dos, basicamente blank hace lo que empty pero mejor, pq tmb contempla espacios
        if (usuarioActualizado.getUsername().isBlank() || usuarioActualizado.getPassword().isBlank() || usuarioActualizado.getRoles().isBlank()){
            throw new BadRequestException("Los campos a actualizar no deben estar vacíos");
        }

        // Misma logica que registro:
        // Compruebo que el usuario no existe en la base de datos
        if (usuarioRepository.findByUsername(usuarioActualizado.getUsername()).isPresent()) {
            throw new DuplicateException("El nombre de usuario ya existe");
        }

        // Logica de pass
        if (usuarioActualizado.getPassword().length() < 6){
            throw new BadRequestException("La longitud de la contraseña debe ser superior o igual a 6 caracteres");
        }

        if (!usuarioActualizado.getRoles().equals("USER") && !usuarioActualizado.getRoles().equals("ADMIN")){
            throw new BadRequestException("Roles inválidos");
        }

//        // Compruebo que es alfanumérica sin símbolos
//        if (usuarioActualizado.getPassword().matches("[A-Za-z0-9]+")) {
//            throw new BadRequestException("La contraseña debe ser alfanumérica (solo letras y números, sin símbolos)");
//        }

        Usuario usuario = usuarioRepository.findByUsername(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        usuario.setRoles(usuarioActualizado.getRoles());
        usuario.setCartasFisicas(Mapper.DTOsToEntities(usuarioActualizado.getCartasFisicas()));

        usuarioRepository.save(usuario);
        return usuarioActualizado;
    }

    public UsuarioFullDTO deleteUser(String nombre){

        Usuario usuario = usuarioRepository.findByUsername(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // lo copio antes de borrarlo para retornarlo luego los datos
        UsuarioFullDTO usuarioFullDTO = Mapper.usuarioToFullDTO(usuario);

        usuarioRepository.delete(usuario);

        return usuarioFullDTO;
    }
}
