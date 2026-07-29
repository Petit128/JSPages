package com.textile.model;

import java.util.Date;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String role; // ADMIN, RESPONSABLE_PRODUCTION, OPERATEUR, CLIENT
    private boolean actif;
    private Date createdAt;
    private Date lastLogin;
    
    public Utilisateur() {}
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
}