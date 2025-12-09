package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.model.Rol;
import com.flexfit.flexfit.model.Usuario;
import com.flexfit.flexfit.enums.TipoEntrenamiento;
import com.flexfit.flexfit.repository.EjercicioRepository;
import com.flexfit.flexfit.repository.RutinaRepository;
import com.flexfit.flexfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model, Authentication authentication) {
        // Datos del admin actual
        String email = authentication.getName();
        Optional<Usuario> adminOpt = usuarioRepository.findByEmail(email);
        adminOpt.ifPresent(admin -> model.addAttribute("adminNombre", admin.getNombre()));

        // Estadísticas generales
        long totalUsuarios = usuarioRepository.count();
        long totalRutinas = rutinaRepository.count();
        long totalEjercicios = ejercicioRepository.count();

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalRutinas", totalRutinas);
        model.addAttribute("totalEjercicios", totalEjercicios);

        // <CHANGE> Ejercicios por tipo de entrenamiento (enums corregidos)
        long ejerciciosPesoLibre = ejercicioRepository.countByTipoEntrenamiento(TipoEntrenamiento.PESO_LIBRE);
        long ejerciciosCalistenia = ejercicioRepository.countByTipoEntrenamiento(TipoEntrenamiento.CALISTENIA);
        long ejerciciosCrossfit = ejercicioRepository.countByTipoEntrenamiento(TipoEntrenamiento.CROSSFIT);
        long ejerciciosYoga = ejercicioRepository.countByTipoEntrenamiento(TipoEntrenamiento.YOGA);
        long ejerciciosCardio = ejercicioRepository.countByTipoEntrenamiento(TipoEntrenamiento.CARDIO);

        model.addAttribute("ejerciciosPesoLibre", ejerciciosPesoLibre);
        model.addAttribute("ejerciciosCalistenia", ejerciciosCalistenia);
        model.addAttribute("ejerciciosCrossfit", ejerciciosCrossfit);
        model.addAttribute("ejerciciosYoga", ejerciciosYoga);
        model.addAttribute("ejerciciosCardio", ejerciciosCardio);

        // Usuarios nuevos esta semana
        LocalDateTime inicioSemana = LocalDate.now().minusDays(7).atStartOfDay();
        long usuariosNuevos = rutinaRepository.countByFechaCreacionAfter(inicioSemana);
        model.addAttribute("usuariosNuevos", usuariosNuevos);

        return "index-admin";
    }

    // ==================== GESTION DE USUARIOS ====================

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());

        long totalAdmins = usuarios.stream()
                .filter(u -> u.getRol() == Rol.ADMIN)
                .count();
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("totalUsers", usuarios.size() - totalAdmins);

        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/cambiar-rol")
    public String cambiarRol(@PathVariable Long id, @RequestParam String nuevoRol,
                             RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Rol rol = Rol.valueOf(nuevoRol);
            usuario.setRol(rol);
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Rol actualizado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuario.getRol() == Rol.ADMIN) {
                redirectAttributes.addFlashAttribute("mensaje", "No se puede eliminar un administrador");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            } else {
                usuarioRepository.delete(usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/usuarios";
    }

    // ==================== REPORTES ====================

    @GetMapping("/reportes")
    public String reportes(Model model) {
        long totalUsuarios = usuarioRepository.count();
        long totalRutinas = rutinaRepository.count();
        long totalEjercicios = ejercicioRepository.count();

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalRutinas", totalRutinas);
        model.addAttribute("totalEjercicios", totalEjercicios);

        LocalDateTime hace4Semanas = LocalDate.now().minusWeeks(4).atStartOfDay();
        LocalDateTime hace3Semanas = LocalDate.now().minusWeeks(3).atStartOfDay();
        LocalDateTime hace2Semanas = LocalDate.now().minusWeeks(2).atStartOfDay();
        LocalDateTime hace1Semana = LocalDate.now().minusWeeks(1).atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();

        long rutinasSemana4 = rutinaRepository.countByFechaCreacionBetween(hace4Semanas, hace3Semanas);
        long rutinasSemana3 = rutinaRepository.countByFechaCreacionBetween(hace3Semanas, hace2Semanas);
        long rutinasSemana2 = rutinaRepository.countByFechaCreacionBetween(hace2Semanas, hace1Semana);
        long rutinasSemana1 = rutinaRepository.countByFechaCreacionBetween(hace1Semana, ahora);

        model.addAttribute("rutinasSemana4", rutinasSemana4);
        model.addAttribute("rutinasSemana3", rutinasSemana3);
        model.addAttribute("rutinasSemana2", rutinasSemana2);
        model.addAttribute("rutinasSemana1", rutinasSemana1);

        double promedioRutinasPorUsuario = totalUsuarios > 0 ? (double) totalRutinas / totalUsuarios : 0;
        model.addAttribute("promedioRutinasPorUsuario", String.format("%.1f", promedioRutinasPorUsuario));

        List<Object[]> topUsuarios = rutinaRepository.findTopUsuariosByRutinas();
        model.addAttribute("topUsuarios", topUsuarios);

        return "admin/reportes";
    }
}