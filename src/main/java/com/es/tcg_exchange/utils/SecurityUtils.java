package com.es.tcg_exchange.utils;

import com.es.tcg_exchange.error.exception.ForbiddenException;
import org.springframework.security.core.Authentication;

/**
 * Clase para manejar validaciones de seguridad
 */
public class SecurityUtils {

    // validacion de acceso de ADMIN o propio usuario
    public static void checkAdminOrSelf(Authentication auth, String username) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isSelf = auth.getName().equals(username);

        if (!isAdmin && !isSelf) {
            throw new ForbiddenException("No tienes los permisos para acceder al recurso");
        }
    }

    // esto eliminar este procedimiento en cada metodo de controller o service:
    //        if (!authentication.getName().equals(username)){
//            throw new ForbiddenException("No tienes permiso para eliminar este usuario");
//        }

//        if(authentication.getAuthorities()
//                .stream()
//                .anyMatch(authority
//                        -> authority.equals(new SimpleGrantedAuthority("ROLE_ADMIN"))) ||
//                authentication.getName().equals(username)) {
//            // copio antes de borrar para retornarlo despues
//            UsuarioPrivateDTO usuarioPrivateDTO = usuarioService.deleteUser(username);
//
//            return new ResponseEntity<UsuarioPrivateDTO>(usuarioPrivateDTO, HttpStatus.OK);
//        } else {
//            throw new ForbiddenException("No tienes los permisos para acceder al recurso");
//        }

}
