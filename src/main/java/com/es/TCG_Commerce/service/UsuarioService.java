package com.es.TCG_Commerce.service;

import com.es.TCG_Commerce.dto.UsuarioDTO;
import com.es.TCG_Commerce.dto.UsuarioRegisterDTO;
import com.es.TCG_Commerce.error.exception.BadRequestException;
import com.es.TCG_Commerce.error.exception.DuplicateException;
import com.es.TCG_Commerce.error.exception.NotFoundException;
import com.es.TCG_Commerce.model.Usuario;
import com.es.TCG_Commerce.repository.UsuarioRepository;
import com.es.TCG_Commerce.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Mapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario No encontrado"));

        List<GrantedAuthority> authorities = Arrays.stream(usuario.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());

        UserDetails userDetails = User // User pertenece a SpringSecurity
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRoles())
                .build();

        return userDetails;
    }

    public UsuarioRegisterDTO registerUser(UsuarioRegisterDTO usuarioRegisterDTO) {
        // Compruebo que el usuario no existe en la base de datos
        if (usuarioRepository.findByUsername(usuarioRegisterDTO.getUsername()).isPresent()) {
            throw new DuplicateException("El nombre de usuario ya existe");
        }

        // Compruebo que ambas contrasenias coinciden
        if (!usuarioRegisterDTO.getPassword().equals(usuarioRegisterDTO.getPassword2())) {
            throw new BadRequestException("Ambas contraseñas deben ser iguales");
        }

        Usuario newUsuario = new Usuario();

        newUsuario.setUsername(usuarioRegisterDTO.getUsername());
        newUsuario.setPassword(passwordEncoder.encode(usuarioRegisterDTO.getPassword()));

        usuarioRepository.save(newUsuario);

        return usuarioRegisterDTO;
    }

    public UsuarioDTO findByNombre(String nombre) {

        Usuario u = usuarioRepository
                .findByUsername(nombre)
                .orElseThrow(() -> new NotFoundException("Usuario con nombre "+nombre+" no encontrado"));

        return Mapper.entityToDTO(u);

    }
}
