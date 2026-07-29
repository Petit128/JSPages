package com.essai.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tache implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private Date dateCreation;
    private boolean terminee;
    
    public Tache(String description) {
        this.description = description;
        this.dateCreation = new Date();
        this.terminee = false;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public boolean isTerminee() {
        return terminee;
    }
    
    public void setTerminee(boolean terminee) {
        this.terminee = terminee;
    }
    
    public String getDateFormatee() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(dateCreation);
    }
}