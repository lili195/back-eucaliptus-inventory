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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

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
