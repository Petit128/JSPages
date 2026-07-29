package com.textile.model;

import java.util.Date;

public class EtapeProduction {
    private int id;
    private int commandeId;
    private String nomEtape; // COUPE, COUTURE, FINITION, LIVRAISON
    private Date dateDebut;
    private Date dateFin;
    private String statut; // EN_ATTENTE, EN_COURS, TERMINEE
    private String responsable;
    private String commentaire;
    private int dureePrevueJours; // Nouveau champ
    private Date dateFinPrevue; // Nouveau champ
    
    public EtapeProduction() {}
    
    // Getters et Setters existants
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }
    
    public String getNomEtape() { return nomEtape; }
    public void setNomEtape(String nomEtape) { this.nomEtape = nomEtape; }
    
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    
    // Nouveaux getters et setters
    public int getDureePrevueJours() { return dureePrevueJours; }
    public void setDureePrevueJours(int dureePrevueJours) { this.dureePrevueJours = dureePrevueJours; }
    
    public Date getDateFinPrevue() { return dateFinPrevue; }
    public void setDateFinPrevue(Date dateFinPrevue) { this.dateFinPrevue = dateFinPrevue; }
}