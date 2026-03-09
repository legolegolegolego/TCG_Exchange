package com.es.tcg_exchange.service;

import com.es.tcg_exchange.dto.*;
import com.es.tcg_exchange.error.exception.BadRequestException;
import com.es.tcg_exchange.error.exception.DuplicateException;
import com.es.tcg_exchange.error.exception.NotFoundException;
import com.es.tcg_exchange.error.exception.UnauthorizedException;
import com.es.tcg_exchange.model.CartaFisica;
import com.es.tcg_exchange.model.Intercambio;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.model.enums.EstadoIntercambio;
import com.es.tcg_exchange.repository.CartaFisicaRepository;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CartaFisicaRepository cfRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;


    // no hace falta inyectarlo si no es componente
//    @Autowired
//    private Mapper mapper;

//    para el login
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByUsernameOrEmail(identifier, identifier)
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
    public Usuario registerUser(UsuarioRegisterDTO usuarioRegisterDTO) {

        if (usuarioRegisterDTO.getUsername() == null ||
                usuarioRegisterDTO.getUsername().isBlank()) {
            throw new BadRequestException("El username es obligatorio");
        }

        // Validación del email
        if (usuarioRegisterDTO.getEmail() == null || usuarioRegisterDTO.getEmail().isBlank() ||
                !usuarioRegisterDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new BadRequestException("El email es obligatorio y debe tener un formato válido");
        }

        // Logica de pass
        if (usuarioRegisterDTO.getPassword() == null
                || !usuarioRegisterDTO.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new BadRequestException(
                    "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial"
            );
        }

        // Compruebo que ambas contraseñas coinciden
        if (!usuarioRegisterDTO.getPassword().equals(usuarioRegisterDTO.getPassword2())) {
            throw new BadRequestException("Ambas contraseñas deben coincidir");
        }

        try {
            Usuario newUsuario = Mapper.usuarioRegisterDTOToModel(usuarioRegisterDTO);

            newUsuario.setPassword(passwordEncoder.encode(usuarioRegisterDTO.getPassword()));

            return usuarioRepository.save(newUsuario);

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

    /**
     * Busca un usuario por username o email.
     * Lanza NotFoundException si no existe.
     */
    public Usuario findByUsernameOrEmail(String identifier) {
        return usuarioRepository
                .findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new NotFoundException(
                        "Usuario con username o email '" + identifier + "' no encontrado"
                ));
    }

    /**
     * Busca un usuario por email.
     * Lanza NotFoundException si no existe.
     */
    public Usuario findByEmail(String email) {
        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        "Usuario con email '" + email + "' no encontrado"
                ));
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

        if (dto.getPasswordNueva() == null
                || !dto.getPasswordNueva().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new BadRequestException(
                    "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial"
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
    @Transactional
    public void deleteUser(Long id, Authentication authentication){

        Usuario usuario = findByIdToModel(id);

        SecurityUtils.checkAdminOrSelf(authentication, usuario.getUsername());

        verificationTokenService.deleteByUsuario(usuario);

        List<CartaFisica> cartas = cfRepository.findByUsuarioId(usuario.getId());

        boolean haParticipado = intercambioRepository
                .existsByUsuarioOrigenOrUsuarioDestino(usuario, usuario);

        if (haParticipado) {

            // Desactivar usuario
            usuario.setDesactivado(true);

            for (CartaFisica cf : cartas) {

                if(cf.isDisponible()){ // si no está disponible se queda como está
                    // Obtener intercambios de la carta
                    List<Intercambio> intercambios =
                            intercambioRepository.findByCartaOrigenOrCartaDestino(cf, cf);

                    if (intercambios.isEmpty()) {
                        // No ha participado → se puede borrar
                        cfRepository.delete(cf);

                    } else {
                        // Ha participado → NO se borra

                        // Rechazar pendientes
                        intercambios.stream()
                                .filter(i -> i.getEstado() == EstadoIntercambio.PENDIENTE)
                                .forEach(i -> i.setEstado(EstadoIntercambio.RECHAZADO));

                        // Marcar como no disponible
                        cf.setDisponible(false);

                        cfRepository.save(cf);
                    }
                }

            }

            usuarioRepository.save(usuario);

        } else {

            // No ha participado → borrado físico completo

            if (!cartas.isEmpty()) {
                cfRepository.deleteAll(cartas);
            }

            usuarioRepository.delete(usuario);
        }

    }
}
