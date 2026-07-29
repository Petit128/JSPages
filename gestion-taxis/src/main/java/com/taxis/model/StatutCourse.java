package com.taxis.model;

public enum StatutCourse {
    EN_ATTENTE("En attente", "secondary"),
    ASSIGNEE("Assignée", "info"),
    EN_COURS("En cours", "warning"),
    TERMINEE("Terminée", "success"),
    ANNULEE("Annulée", "danger");
    
    private String libelle;
    private String badgeType;
    
    StatutCourse(String libelle, String badgeType) {
        this.libelle = libelle;
        this.badgeType = badgeType;
    }
    
    public String getLibelle() { return libelle; }
    public String getBadgeType() { return badgeType; }
}