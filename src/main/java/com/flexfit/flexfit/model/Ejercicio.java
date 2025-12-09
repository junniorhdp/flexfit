package com.flexfit.flexfit.model;

import jakarta.persistence.*;
import com.flexfit.flexfit.enums.MusculoPrincipal;
import com.flexfit.flexfit.enums.TipoEntrenamiento;

@Entity
@Table(name = "ejercicios")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MusculoPrincipal musculoPrimario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrenamiento tipoEntrenamiento;

    @Column(length = 255)
    private String urlVideo;

    public Ejercicio() {}

    // GETTERS Y SETTERS CORRECTOS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MusculoPrincipal getMusculoPrimario() {
        return musculoPrimario;
    }

    public void setMusculoPrimario(MusculoPrincipal musculoPrimario) {
        this.musculoPrimario = musculoPrimario;
    }

    public TipoEntrenamiento getTipoEntrenamiento() {
        return tipoEntrenamiento;
    }

    public void setTipoEntrenamiento(TipoEntrenamiento tipoEntrenamiento) {
        this.tipoEntrenamiento = tipoEntrenamiento;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
