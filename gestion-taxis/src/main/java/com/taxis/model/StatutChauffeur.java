package com.taxis.model;

public enum StatutChauffeur {
    DISPONIBLE("Disponible", "success"),
    EN_COURSE("En course", "warning"),
    EN_PAUSE("En pause", "info"),
    INDISPONIBLE("Indisponible", "danger");
    
    private String libelle;
    private String badgeType;
    
    StatutChauffeur(String libelle, String badgeType) {
        this.libelle = libelle;
        this.badgeType = badgeType;
    }
    
    public String getLibelle() { return libelle; }
    public String getBadgeType() { return badgeType; }
}