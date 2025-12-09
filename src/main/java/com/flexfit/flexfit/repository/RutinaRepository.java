package com.flexfit.flexfit.repository;

import com.flexfit.flexfit.model.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {

    // Rutinas por usuario
    List<Rutina> findByUsuarioId(Long usuarioId);

    long countByUsuarioId(Long usuarioId);

    // Contar ejercicios en rutinas de un usuario
    @Query("SELECT COUNT(re) FROM RutinaEjercicio re WHERE re.rutina.usuario.id = :usuarioId")
    long countEjerciciosByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Para reportes por fechas
    long countByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    long countByFechaCreacionAfter(LocalDateTime fecha);

    // Top usuarios con más rutinas
    @Query("SELECT r.usuario.nombre, COUNT(r) as total FROM Rutina r " +
            "GROUP BY r.usuario.id, r.usuario.nombre " +
            "ORDER BY total DESC")
    List<Object[]> findTopUsuariosByRutinas();
}