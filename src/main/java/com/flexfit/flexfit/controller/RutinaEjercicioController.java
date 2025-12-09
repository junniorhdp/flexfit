package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.model.Rutina;
import com.flexfit.flexfit.model.RutinaEjercicio;
import com.flexfit.flexfit.model.Ejercicio;
import com.flexfit.flexfit.repository.RutinaEjercicioRepository;
import com.flexfit.flexfit.repository.EjercicioRepository;
import com.flexfit.flexfit.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rutina-ejercicios")
public class RutinaEjercicioController {

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private RutinaEjercicioRepository rutinaEjercicioRepository;

    @GetMapping("/{rutinaId}")
    public String listar(@PathVariable Long rutinaId, Model model) {
        Rutina rutina = rutinaRepository.findById(rutinaId).orElse(null);
        List<RutinaEjercicio> ejercicios =
                rutinaEjercicioRepository.findByRutinaIdOrderByOrdenAsc(rutinaId);

        model.addAttribute("rutina", rutina);
        model.addAttribute("ejercicios", ejercicios);

        return "rutina-ejercicios/rutina-ejercicios";
    }

    @GetMapping("/agregar/{rutinaId}")
    public String agregar(@PathVariable Long rutinaId, Model model) {

        RutinaEjercicio re = new RutinaEjercicio();
        Rutina rutina = rutinaRepository.findById(rutinaId).orElse(null);

        // DEBUG: Verificar cuántos ejercicios hay en la base de datos
        List<Ejercicio> ejercicios = ejercicioRepository.findAll();
        System.out.println("[v0] Total de ejercicios encontrados: " + ejercicios.size());

        if (ejercicios.isEmpty()) {
            System.out.println("[v0] ⚠️ WARNING: No hay ejercicios en la base de datos!");
        } else {
            System.out.println("[v0] Ejercicios disponibles:");
            for (Ejercicio e : ejercicios) {
                System.out.println("  - ID: " + e.getId() + ", Nombre: " + e.getNombre());
            }
        }

        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaEjercicio", re);
        model.addAttribute("ejercicios", ejercicios);

        return "rutinas/rutina-ejercicios/rutina-ejercicio-form";
    }

    @PostMapping("/guardar/{rutinaId}")
    public String guardar(@PathVariable Long rutinaId,
                          @RequestParam Long ejercicioId,
                          @RequestParam Integer series,
                          @RequestParam String repeticiones,
                          @RequestParam Integer orden) {

        System.out.println("[v0] Guardando ejercicio en rutina...");
        System.out.println("[v0] Rutina ID: " + rutinaId);
        System.out.println("[v0] Ejercicio ID: " + ejercicioId);
        System.out.println("[v0] Series: " + series);
        System.out.println("[v0] Repeticiones: " + repeticiones);
        System.out.println("[v0] Orden: " + orden);

        // Buscar rutina y ejercicio
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        // Crear y guardar la relación
        RutinaEjercicio rutinaEjercicio = new RutinaEjercicio();
        rutinaEjercicio.setRutina(rutina);
        rutinaEjercicio.setEjercicio(ejercicio);
        rutinaEjercicio.setSeries(series);
        rutinaEjercicio.setRepeticiones(repeticiones);
        rutinaEjercicio.setOrden(orden);

        rutinaEjercicioRepository.save(rutinaEjercicio);
        System.out.println("[v0] ✅ Ejercicio agregado exitosamente!");

        return "redirect:/rutinas/detalle/" + rutinaId;
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        RutinaEjercicio re = rutinaEjercicioRepository.findById(id).orElse(null);

        model.addAttribute("rutinaEjercicio", re);
        model.addAttribute("rutina", re.getRutina());
        model.addAttribute("ejercicios", ejercicioRepository.findAll());

        return "rutina-ejercicios/rutina-ejercicio-form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        RutinaEjercicio linea = rutinaEjercicioRepository.findById(id).orElse(null);

        Long rutinaId = linea.getRutina().getId();

        rutinaEjercicioRepository.delete(linea);

        return "redirect:/rutinas/detalle/" + rutinaId;
    }
}
