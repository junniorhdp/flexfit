package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.model.Rol;
import com.flexfit.flexfit.model.Usuario;
import com.flexfit.flexfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Mostrar el formulario de registro (GET)
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Añadimos un objeto vacío al modelo para que Thymeleaf pueda enlazar los campos
        model.addAttribute("usuario", new Usuario());
        return "register"; // Retorna la plantilla register.html
    }

    // 2. Procesar el registro (POST)
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("usuario") Usuario usuario, Model model) {

        // --- DEBUG: Verificación de Binding ---
        // Esto es lo que fallaba antes. Si Spring Boot está bien, DEBE funcionar.
        System.out.println("✅ DEBUG: Intentando registrar al usuario:");
        System.out.println("Email recibido: " + usuario.getEmail());
        System.out.println("Nombre recibido: " + usuario.getNombre());
        // ----------------------------------------

        // Asignar el rol por defecto
        usuario.setRol(Rol.ESTANDAR);

        try {
            // 1. Encriptar la contraseña antes de guardar
            String rawPassword = usuario.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            usuario.setPassword(encodedPassword);

            // 2. Guardar en la base de datos
            usuarioRepository.save(usuario);

            // Éxito: Redirigir a la página de login
            return "redirect:/login";

        } catch (DataIntegrityViolationException e) {
            // Manejar error si el correo ya existe o faltan campos NOT NULL
            model.addAttribute("error", "Error al registrar: El correo ya existe o faltan campos obligatorios.");
            // Devolver el objeto usuario al modelo para que el formulario retenga los datos
            return "register";
        } catch (Exception e) {
            // Manejar otros errores
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error inesperado durante el registro.");
            return "register";
        }
    }

    // 3. Método para mostrar la página de login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Retorna la plantilla login.html
    }
}