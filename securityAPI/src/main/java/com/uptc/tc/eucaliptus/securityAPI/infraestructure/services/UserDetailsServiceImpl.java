package com.uptc.tc.eucaliptus.securityAPI.infraestructure.services;

import com.uptc.tc.eucaliptus.securityAPI.infraestructure.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación del servicio {@link UserDetailsService} utilizado por Spring Security.
 * <p>
 * Este servicio se encarga de cargar los detalles de un usuario a partir de su nombre de usuario
 * y convertirlos en una instancia de {@link UserDetails}, que es utilizada para la autenticación y autorización.
 * </p>
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    /**
     * Constructor que inyecta la dependencia del servicio de usuarios.
     *
     * @param userService El servicio que gestiona las operaciones relacionadas con los usuarios.
     */

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Carga un usuario desde la base de datos a partir de su nombre de usuario.
     * <p>
     * Convierte la entidad {@link User} en una instancia de {@link UserDetails},
     * que incluye la información necesaria para la autenticación y autorización,
     * como los roles del usuario.
     * </p>
     *
     * @param username El nombre de usuario del usuario que se desea cargar.
     * @return Una instancia de {@link UserDetails} con los detalles del usuario.
     * @throws UsernameNotFoundException Si el usuario con el nombre especificado no existe.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUserName(username).orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getRoleName().name())
        );
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
