package com.flexfit.flexfit.enums;

// Define los principales grupos musculares
public enum MusculoPrincipal {
    PECHO("Pecho"),
    ESPALDA("Espalda"),
    PIERNAS("Piernas"),
    HOMBRO("Hombro"),
    BICEPS("Bíceps"),
    TRICEPS("Tríceps"),
    ABDOMEN("Abdomen"),
    GLUTEOS("Glúteos"),
    PANTORRILLAS("Pantorrillas"),
    ANTEBRAZO("Antebrazo"),
    CUERPO_COMPLETO("Cuerpo completo");

    private final String displayName;

    MusculoPrincipal(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}