package com.es.tcg_exchange.controller;

import com.es.tcg_exchange.dto.PasswordUpdateDTO;
import com.es.tcg_exchange.dto.UsernameUpdateDTO;
import com.es.tcg_exchange.dto.UsuarioDetailDTO;
import com.es.tcg_exchange.dto.UsuarioFullDTO;
import com.es.tcg_exchange.model.Usuario;
import com.es.tcg_exchange.service.UsuarioService;
import com.es.tcg_exchange.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // indica a Spring Boot que es un controller, maneja solicitudes HTTP
@RequestMapping("/usuarios") // mapear solicitudes http (todas las que lleguen a /usuarios/... seran manejadas aqui)
public class UsuarioController {

    @Autowired // inyecta auto instancias de repo
    private UsuarioService usuarioService;

    // obtener datos de todos los usuarios
    @GetMapping //("/") si pongo / la uri tiene que acabar con barra, si no pongo nada no hace falta (es más común asi)
    public ResponseEntity<List<UsuarioDetailDTO>> getAll(){
        List<UsuarioDetailDTO> usuariosDTO = usuarioService.getAll();

//        return new ResponseEntity<List<UsuarioDetailDTO>>(usuariosDTO, HttpStatus.OK);
        return ResponseEntity.ok(usuariosDTO);
    }
    // no hace falta el authentication aqui pq solo pueden acceder los admin
    // y ya se contempla eso en el securityconfig

    // obtener datos de usuario por su id
    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioFullDTO> findById(
            @PathVariable Long id){
        UsuarioFullDTO usuarioFullDTO = usuarioService.findByIdToDTO(id);

        return new ResponseEntity<UsuarioFullDTO>(usuarioFullDTO, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioFullDTO> findByUsername(
            @PathVariable String username) {

        UsuarioFullDTO usuarioFullDTO = usuarioService.findByUsername(username);
        return new ResponseEntity<UsuarioFullDTO>(usuarioFullDTO, HttpStatus.OK);
    }


    @PutMapping("/{id}/username")
    public ResponseEntity<UsernameUpdateDTO> updateUsername(
            @PathVariable Long id,
            @RequestBody UsernameUpdateDTO dto,
            Authentication authentication) {

        UsernameUpdateDTO updated =
                usuarioService.updateUsername(id, dto.getNuevoUsername(), authentication);

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateDTO dto,
            Authentication authentication) {

        usuarioService.updatePassword(id, dto, authentication);

        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            Authentication authentication
    ) {
        // Validar permisos: admin o el propio usuario
        Usuario usuario = usuarioService.findByIdToModel(id);
        SecurityUtils.checkAdminOrSelf(authentication, usuario.getUsername());

        // Ejecutar delete/desactivar según la lógica
        usuarioService.deleteUser(id);

        // Respuesta HTTP estándar para delete
        return ResponseEntity.noContent().build();
    }

}
