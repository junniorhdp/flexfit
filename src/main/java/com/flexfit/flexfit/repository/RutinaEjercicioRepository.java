package com.flexfit.flexfit.repository;

import com.flexfit.flexfit.model.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RutinaEjercicioRepository extends JpaRepository<RutinaEjercicio, Long> {

    // Buscar por rutina ordenado por campo "orden"
    List<RutinaEjercicio> findByRutinaIdOrderByOrdenAsc(Long rutinaId);
}
