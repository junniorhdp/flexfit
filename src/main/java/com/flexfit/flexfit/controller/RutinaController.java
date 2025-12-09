package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.model.Rutina;
import com.flexfit.flexfit.model.Usuario;
import com.flexfit.flexfit.model.RutinaEjercicio;
import com.flexfit.flexfit.model.Ejercicio;
import com.flexfit.flexfit.repository.RutinaRepository;
import com.flexfit.flexfit.repository.UsuarioRepository;
import com.flexfit.flexfit.repository.RutinaEjercicioRepository;
import com.flexfit.flexfit.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rutinas")
public class RutinaController {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RutinaEjercicioRepository rutinaEjercicioRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    // LISTAR SOLO LAS RUTINAS DEL USUARIO AUTENTICADO
    @GetMapping
    public String listar(Model model, Authentication auth) {
        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        model.addAttribute("rutinas",
                rutinaRepository.findByUsuarioId(usuario.getId()));

        return "rutinas/rutina-lista";
    }

    // VER DETALLE DE UNA RUTINA (con verificación de dueño)
    @GetMapping("/{id}")
    public String ver(@PathVariable Long id, Model model, Authentication auth) {

        Rutina rutina = rutinaRepository.findById(id).orElse(null);
        if (rutina == null) return "redirect:/rutinas";

        // Solo el dueño puede ver la rutina
        if (!rutina.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/rutinas?error=no-autorizado";
        }

        // Obtener ejercicios ordenados
        List<RutinaEjercicio> ejercicios =
                rutinaEjercicioRepository.findByRutinaIdOrderByOrdenAsc(id);

        model.addAttribute("rutina", rutina);
        model.addAttribute("ejercicios", ejercicios);

        return "rutinas/rutina-detalle";
    }

    // FORMULARIO DE CREACIÓN DE RUTINA
    @GetMapping("/crear")
    public String crear(Model model) {
        List<Ejercicio> catalogo = ejercicioRepository.findAll();

        model.addAttribute("rutina", new Rutina());
        model.addAttribute("ejerciciosCatalogo", catalogo);

        return "rutinas/rutina-form";
    }

    // GUARDAR RUTINA NUEVA
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("rutina") Rutina rutina, Authentication auth) {

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        rutina.setUsuario(usuario);
        rutinaRepository.save(rutina);

        return "redirect:/rutinas?exito=true";
    }

    // EDITAR RUTINA
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, Authentication auth) {

        Rutina rutina = rutinaRepository.findById(id).orElse(null);
        if (rutina == null) return "redirect:/rutinas";

        if (!rutina.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/rutinas?error=no-autorizado";
        }

        model.addAttribute("rutina", rutina);
        return "rutinas/rutina-form";
    }

    // ELIMINAR RUTINA
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication auth) {

        Rutina rutina = rutinaRepository.findById(id).orElse(null);
        if (rutina == null) return "redirect:/rutinas";

        if (!rutina.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/rutinas?error=no-autorizado";
        }

        rutinaRepository.delete(rutina);
        return "redirect:/rutinas?eliminado=true";
    }

    // FORMULARIO PARA AGREGAR EJERCICIO A RUTINA
    @GetMapping("/{id}/agregar-ejercicio")
    public String agregarEjercicioForm(@PathVariable Long id, Model model, Authentication auth) {

        Rutina rutina = rutinaRepository.findById(id).orElse(null);
        if (rutina == null) {
            return "redirect:/rutinas";
        }

        // Validar que sea el dueño
        if (!rutina.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/rutinas?error=no-autorizado";
        }

        // Lista de ejercicios del catálogo
        List<Ejercicio> catalogo = ejercicioRepository.findAll();

        model.addAttribute("rutina", rutina);
        model.addAttribute("ejercicios", catalogo);
        model.addAttribute("rutinaEjercicio", new RutinaEjercicio());

        return "rutinas/rutina-ejercicios/rutina-ejercicio-form";
    }

    // GUARDAR EJERCICIO EN RUTINA
    @PostMapping("/{id}/agregar-ejercicio")
    public String guardarEjercicioEnRutina(
            @PathVariable Long id,
            @RequestParam Long ejercicioId,
            @RequestParam Integer series,
            @RequestParam String repeticiones,
            @RequestParam Integer orden,
            Authentication auth
    ) {
        Rutina rutina = rutinaRepository.findById(id).orElse(null);
        if (rutina == null) {
            return "redirect:/rutinas";
        }

        // Verificar dueño
        if (!rutina.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/rutinas?error=no-autorizado";
        }

        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId).orElse(null);
        if (ejercicio == null) {
            return "redirect:/rutinas/" + id + "?error=ejercicio-no-encontrado";
        }

        RutinaEjercicio re = new RutinaEjercicio();
        re.setRutina(rutina);
        re.setEjercicio(ejercicio);
        re.setSeries(series);
        re.setRepeticiones(repeticiones);
        re.setOrden(orden);
        re.setRepeticionesOTiempo(repeticiones);  // Campo requerido por la BD

        rutinaEjercicioRepository.save(re);

        return "redirect:/rutinas/" + id + "?agregado=true";
    }

    // ELIMINAR EJERCICIO DE UNA RUTINA
    @GetMapping("/{rutinaId}/eliminar-ejercicio/{id}")
    public String eliminarEjercicioDeRutina(
            @PathVariable Long rutinaId,
            @PathVariable Long id,
            Authentication auth) {

        Rutina rutina = rutinaRepository.findById(rutinaId).orElse(null);
        if (rutina == null) return "redirect:/rutinas";

        // Verificar que sea el dueño
        if (!rutina.getUsuario().getEmail().equals(auth.getName())) {
            return "redirect:/rutinas?error=no-autorizado";
        }

        rutinaEjercicioRepository.deleteById(id);

        return "redirect:/rutinas/" + rutinaId + "?eliminado=true";
    }
}