package com.taxis.model;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String password;
    private Date dateInscription;
    private boolean actif;
    private double totalDepenses;
    private int nombreCourses;
    
    public Client() {
        this.dateInscription = new Date();
        this.actif = true;
        this.totalDepenses = 0;
        this.nombreCourses = 0;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Date getDateInscription() { return dateInscription; }
    public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public double getTotalDepenses() { return totalDepenses; }
    public void setTotalDepenses(double totalDepenses) { this.totalDepenses = totalDepenses; }
    
    public int getNombreCourses() { return nombreCourses; }
    public void setNombreCourses(int nombreCourses) { this.nombreCourses = nombreCourses; }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
}