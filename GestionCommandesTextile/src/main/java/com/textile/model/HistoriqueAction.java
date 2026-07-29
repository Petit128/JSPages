package com.textile.model;

import java.util.Date;

public class HistoriqueAction {
    private int id;
    private int commandeId;
    private String action;
    private String utilisateur;
    private Date dateAction;
    private String details;
    
    public HistoriqueAction() {}
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }
    
    public Date getDateAction() { return dateAction; }
    public void setDateAction(Date dateAction) { this.dateAction = dateAction; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}