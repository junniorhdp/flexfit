package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.model.Ejercicio;
import com.flexfit.flexfit.enums.MusculoPrincipal;
import com.flexfit.flexfit.enums.TipoEntrenamiento;
import com.flexfit.flexfit.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ejercicios")
public class EjercicioController {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    // CATALOGO - Accesible para todos (admin y usuarios)
    @GetMapping("/catalogo")
    public String catalogo(Model model, Authentication authentication,
                           @RequestParam(required = false) String nombre,
                           @RequestParam(required = false) String musculoPrimario,
                           @RequestParam(required = false) String tipoEntrenamiento) {

        List<Ejercicio> ejercicios;

        MusculoPrincipal musculoEnum = null;
        TipoEntrenamiento tipoEnum = null;

        try {
            if (musculoPrimario != null && !musculoPrimario.isEmpty()) {
                musculoEnum = MusculoPrincipal.valueOf(musculoPrimario);
            }
            if (tipoEntrenamiento != null && !tipoEntrenamiento.isEmpty()) {
                tipoEnum = TipoEntrenamiento.valueOf(tipoEntrenamiento);
            }
        } catch (IllegalArgumentException e) {
            // Si el enum no es valido, ignorar el filtro
        }

        if ((nombre != null && !nombre.isEmpty()) || musculoEnum != null || tipoEnum != null) {
            ejercicios = ejercicioRepository.findByFiltros(
                    nombre != null && !nombre.isEmpty() ? nombre : null,
                    musculoEnum,
                    tipoEnum
            );
        } else {
            ejercicios = ejercicioRepository.findAll();
        }

        model.addAttribute("ejercicios", ejercicios);
        model.addAttribute("totalEjercicios", ejercicios.size());
        model.addAttribute("musculosPrincipales", MusculoPrincipal.values());
        model.addAttribute("tiposEntrenamiento", TipoEntrenamiento.values());
        model.addAttribute("filtroNombre", nombre);
        model.addAttribute("filtroMusculoPrimario", musculoPrimario);
        model.addAttribute("filtroTipoEntrenamiento", tipoEntrenamiento);

        // Verificar si es admin para mostrar u ocultar botones
        boolean isAdmin = authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // Usar vista diferente segun el rol
        if (isAdmin) {
            return "ejercicios/ejercicio-lista";
        } else {
            return "ejercicios/ejercicio-catalogo-usuario";
        }
    }

    // ========== RUTAS SOLO PARA ADMIN ==========

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "redirect:/ejercicios/catalogo";
        }
        model.addAttribute("ejercicio", new Ejercicio());
        model.addAttribute("esNuevo", true);
        model.addAttribute("musculosPrincipales", MusculoPrincipal.values());
        model.addAttribute("tiposEntrenamiento", TipoEntrenamiento.values());
        return "ejercicios/ejercicio-form";
    }

    @PostMapping("/crear")
    public String crearEjercicio(@ModelAttribute Ejercicio ejercicio,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            return "redirect:/ejercicios/catalogo";
        }
        ejercicioRepository.save(ejercicio);
        redirectAttributes.addFlashAttribute("mensaje", "Ejercicio creado correctamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/ejercicios/catalogo";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                          Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "redirect:/ejercicios/catalogo";
        }
        Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(id);
        if (ejercicioOpt.isPresent()) {
            model.addAttribute("ejercicio", ejercicioOpt.get());
            model.addAttribute("esNuevo", false);
            model.addAttribute("musculosPrincipales", MusculoPrincipal.values());
            model.addAttribute("tiposEntrenamiento", TipoEntrenamiento.values());
            return "ejercicios/ejercicio-form";
        }
        return "redirect:/ejercicios/catalogo";
    }

    @PostMapping("/editar/{id}")
    public String editarEjercicio(@PathVariable Long id, @ModelAttribute Ejercicio ejercicio,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            return "redirect:/ejercicios/catalogo";
        }
        ejercicio.setId(id);
        ejercicioRepository.save(ejercicio);
        redirectAttributes.addFlashAttribute("mensaje", "Ejercicio actualizado correctamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/ejercicios/catalogo";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEjercicio(@PathVariable Long id,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            return "redirect:/ejercicios/catalogo";
        }
        ejercicioRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Ejercicio eliminado correctamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/ejercicios/catalogo";
    }

    // Metodo auxiliar para verificar si es admin
    private boolean isAdmin(Authentication authentication) {
        return authentication != null &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}