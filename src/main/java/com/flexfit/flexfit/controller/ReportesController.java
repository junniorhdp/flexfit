package com.flexfit.flexfit.controller;

import com.flexfit.flexfit.enums.TipoEntrenamiento;
import com.flexfit.flexfit.enums.MusculoPrincipal;
import com.flexfit.flexfit.model.Rutina;
import com.flexfit.flexfit.repository.EjercicioRepository;
import com.flexfit.flexfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.flexfit.flexfit.repository.RutinaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String index() {
        return "reportes/index";
    }

    @GetMapping("/ejercicios")
    public String reporteEjercicios(
            @RequestParam(required = false) TipoEntrenamiento tipoEntrenamiento,
            @RequestParam(required = false) String musculo,
            Model model) {

        if (tipoEntrenamiento != null) {
            model.addAttribute("ejercicios", ejercicioRepository.findByTipoEntrenamiento(tipoEntrenamiento));
        } else if (musculo != null && !musculo.isEmpty()) {
            try {
                MusculoPrincipal musculoEnum = MusculoPrincipal.valueOf(musculo);
                model.addAttribute("ejercicios", ejercicioRepository.findByMusculoPrimario(musculoEnum));
            } catch (IllegalArgumentException e) {
                model.addAttribute("ejercicios", ejercicioRepository.findAll());
            }
        } else {
            model.addAttribute("ejercicios", ejercicioRepository.findAll());
        }

        model.addAttribute("tiposEntrenamiento", TipoEntrenamiento.values());
        model.addAttribute("musculosPrincipales", MusculoPrincipal.values());
        model.addAttribute("filtroActivo", tipoEntrenamiento != null || (musculo != null && !musculo.isEmpty()));

        return "reportes/ejercicios";
    }

    // <CHANGE> Agregar endpoint para reporte de rutinas
    @GetMapping("/rutinas")
    public String reporteRutinas(
            @RequestParam(required = false) Long usuarioId,
            Model model) {

        List<Rutina> rutinas;

        if (usuarioId != null) {
            rutinas = rutinaRepository.findByUsuarioId(usuarioId);
        } else {
            rutinas = rutinaRepository.findAll();
        }

        model.addAttribute("rutinas", rutinas);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("filtroActivo", usuarioId != null);

        return "reportes/rutinas";
    }

    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {
        model.addAttribute("totalEjercicios", ejercicioRepository.count());
        model.addAttribute("totalRutinas", rutinaRepository.count());
        model.addAttribute("totalUsuarios", usuarioRepository.count());

        for (TipoEntrenamiento tipo : TipoEntrenamiento.values()) {
            long count = ejercicioRepository.countByTipoEntrenamiento(tipo);
            model.addAttribute("ejercicios" + tipo.name(), count);
        }

        return "reportes/estadisticas";
    }
}