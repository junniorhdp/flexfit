package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.model.Usuario;
import com.flexfit.flexfit.repository.EjercicioRepository;
import com.flexfit.flexfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.flexfit.flexfit.repository.RutinaRepository;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {
            return redirectToAdminDashboard(authentication, model);
        } else {
            return redirectToUserDashboard(authentication, model);
        }
    }

    // Agregar este metodo a tu HomeController.java existente

    @GetMapping("/perfil")
    public String perfil(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            model.addAttribute("nombreUsuario", usuarioOpt.get().getNombre());
        }

        return "perfil";
    }

    // Ruta adicional para /index
    @GetMapping("/index")
    public String index(Authentication authentication, Model model) {
        return home(authentication, model);
    }

    private String redirectToAdminDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        String nombreUsuario = usuarioOpt.map(Usuario::getNombre).orElse(email);
        model.addAttribute("nombreUsuario", nombreUsuario);

        long totalUsuarios = usuarioRepository.count();
        long totalRutinas = rutinaRepository.count();
        long totalEjercicios = ejercicioRepository.count();

        LocalDateTime inicioSemana = LocalDate.now().minusDays(7).atStartOfDay();
        long rutinasNuevasSemana = rutinaRepository.countByFechaCreacionAfter(inicioSemana);

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalRutinas", totalRutinas);
        model.addAttribute("totalEjercicios", totalEjercicios);
        model.addAttribute("usuariosNuevosMes", 0);
        model.addAttribute("rutinasNuevasSemana", rutinasNuevasSemana);

        return "index-admin";
    }

    private String redirectToUserDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioOpt.get();
        model.addAttribute("nombreUsuario", usuario.getNombre());

        // Estadisticas del usuario
        long misRutinas = rutinaRepository.countByUsuarioId(usuario.getId());
        long totalEjerciciosEnRutinas = rutinaRepository.countEjerciciosByUsuarioId(usuario.getId());
        long totalEjerciciosDisponibles = ejercicioRepository.count();

        model.addAttribute("misRutinas", misRutinas);
        model.addAttribute("totalEjerciciosEnRutinas", totalEjerciciosEnRutinas);
        model.addAttribute("totalEjerciciosDisponibles", totalEjerciciosDisponibles);
        model.addAttribute("diasEntrenando", 0); // Por ahora estatico, se puede implementar despues
        model.addAttribute("rutinas", rutinaRepository.findByUsuarioId(usuario.getId()));

        return "index";
    }
}