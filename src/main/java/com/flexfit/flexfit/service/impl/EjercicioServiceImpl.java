package com.flexfit.flexfit.service.impl;

import com.flexfit.flexfit.model.Ejercicio;
import com.flexfit.flexfit.enums.TipoEntrenamiento;
import com.flexfit.flexfit.repository.EjercicioRepository;
import com.flexfit.flexfit.service.EjercicioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    public EjercicioServiceImpl(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    public Ejercicio guardar(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public Optional<Ejercicio> buscarPorId(Long id) {
        return ejercicioRepository.findById(id);
    }

    @Override
    public List<Ejercicio> listarTodos() {
        return ejercicioRepository.findAll();
    }

    @Override
    public List<Ejercicio> listarPorTipo(TipoEntrenamiento tipo) {
        return ejercicioRepository.findByTipoEntrenamiento(tipo);
    }

    @Override
    public void eliminar(Long id) {
        ejercicioRepository.deleteById(id);
    }
}
