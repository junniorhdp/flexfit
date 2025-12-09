package com.flexfit.flexfit.config;

import com.flexfit.flexfit.service.security.UserDetailsServiceImpl; // <-- Importado
import org.springframework.beans.factory.annotation.Autowired; // <-- Importado
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // <-- Importado
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Inyectar el servicio que crearemos en el siguiente paso
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Define el Bean para encriptar contraseñas (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define el proveedor de autenticación. Este le dice a Spring Security
     * qué servicio usar para cargar usuarios (userDetailsService)
     * y qué encriptador usar para verificar la contraseña.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Define las reglas de acceso (Filtro de Seguridad).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Deshabilitar CSRF para simplificar las pruebas del formulario POST
                .csrf(AbstractHttpConfigurer::disable)

                // Configurar Autorización de peticiones
                .authorizeHttpRequests(authorize -> authorize
                        // Permitir acceso sin autenticación a /register, /login y archivos estáticos
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Requerir autenticación para cualquier otra ruta
                        .anyRequest().authenticated()
                )

                // Configurar el formulario de Login
                .formLogin(form -> form
                        .loginPage("/login") // Especifica la URL de tu página de login
                        // Si el login es exitoso, redirige a la raíz ("/")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // Configurar el Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                )

                .authenticationProvider(daoAuthenticationProvider());


        return http.build();

    }
}