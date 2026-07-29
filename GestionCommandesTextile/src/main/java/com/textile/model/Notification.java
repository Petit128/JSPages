package com.textile.model;

import java.util.Date;

public class Notification {
    private int id;
    private int commandeId;
    private String destinataire;
    private String type;
    private String message;
    private boolean lue;
    private Date dateEnvoi;
    
    public Notification() {}
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }
    
    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }
    
    public Date getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(Date dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}