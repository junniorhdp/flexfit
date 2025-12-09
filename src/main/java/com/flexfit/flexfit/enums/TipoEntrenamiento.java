package com.flexfit.flexfit.enums;

public enum TipoEntrenamiento {
    PESO_LIBRE("Peso libre"),
    CALISTENIA("Calistenia"),
    CROSSFIT("Crossfit"),
    YOGA("Yoga"),
    CARDIO("Cardio");

    private final String displayName;

    TipoEntrenamiento(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
