package com.flexfit.flexfit.service;

import com.flexfit.flexfit.model.Ejercicio;
import com.flexfit.flexfit.enums.TipoEntrenamiento;
import java.util.List;
import java.util.Optional;

public interface EjercicioService {

    Ejercicio guardar(Ejercicio ejercicio);

    Optional<Ejercicio> buscarPorId(Long id);

    List<Ejercicio> listarTodos();

    List<Ejercicio> listarPorTipo(TipoEntrenamiento tipo);

    void eliminar(Long id);
}