package com.flexfit.flexfit.service.security;

import com.flexfit.flexfit.model.Usuario;
import com.flexfit.flexfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service // Marca esta clase como un Bean de Spring
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carga los datos de un usuario para Spring Security.
     * El 'username' de Spring Security es nuestro 'email'.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscar el usuario en la base de datos por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // 2. Definir los roles/permisos
        // Como solo tenemos un rol (ENUM Rol), lo convertimos a un SimpleGrantedAuthority.
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()) // Ej: "ROLE_ESTANDAR"
        );

        // 3. Devolver un objeto UserDetails de Spring Security
        return new User(
                usuario.getEmail(),           // Nombre de usuario (email)
                usuario.getPassword(),        // Contraseña (ya encriptada)
                authorities                   // Colección de roles
        );
    }
}