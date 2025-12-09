package com.flexfit.flexfit.repository;

import com.flexfit.flexfit.model.Ejercicio;
import com.flexfit.flexfit.enums.TipoEntrenamiento;
import com.flexfit.flexfit.enums.MusculoPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    // Búsqueda por nombre
    List<Ejercicio> findByNombreContainingIgnoreCase(String nombre);

    // Para ReportesController
    List<Ejercicio> findByTipoEntrenamiento(TipoEntrenamiento tipoEntrenamiento);

    List<Ejercicio> findByMusculoPrimario(MusculoPrincipal musculoPrimario);

    long countByTipoEntrenamiento(TipoEntrenamiento tipoEntrenamiento);

    // Filtrado multicriterio para EjercicioController
    // Nota: Usamos musculoPrimario en lugar de grupoMuscular y tipoEntrenamiento en lugar de dificultad
    @Query("SELECT e FROM Ejercicio e WHERE " +
            "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:musculoPrimario IS NULL OR e.musculoPrimario = :musculoPrimario) AND " +
            "(:tipoEntrenamiento IS NULL OR e.tipoEntrenamiento = :tipoEntrenamiento)")
    List<Ejercicio> findByFiltros(
            @Param("nombre") String nombre,
            @Param("musculoPrimario") MusculoPrincipal musculoPrimario,
            @Param("tipoEntrenamiento") TipoEntrenamiento tipoEntrenamiento
    );

    // Para dropdown de filtros - músculos únicos
    @Query("SELECT DISTINCT e.musculoPrimario FROM Ejercicio e WHERE e.musculoPrimario IS NOT NULL")
    List<MusculoPrincipal> findDistinctMusculosPrimarios();

    // Para dropdown de filtros - tipos de entrenamiento únicos
    @Query("SELECT DISTINCT e.tipoEntrenamiento FROM Ejercicio e WHERE e.tipoEntrenamiento IS NOT NULL")
    List<TipoEntrenamiento> findDistinctTiposEntrenamiento();
}