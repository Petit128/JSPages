package com.taxis.model;

import java.io.Serializable;
import java.util.Date;

public class Chauffeur implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nom;
    private String prenom;
    private String permis;
    private String telephone;
    private String email;
    private String password;
    private StatutChauffeur statut; // DISPONIBLE, EN_COURSE, EN_PAUSE, INDISPONIBLE
    private Date dateEmbauche;
    private double revenuTotal;
    private double latitude;
    private double longitude;
    private int nombreCourses;
    private double evaluation;
    
    
    public Chauffeur() {
        this.statut = StatutChauffeur.DISPONIBLE;
        this.revenuTotal = 0.0;
        this.nombreCourses = 0;
        this.evaluation = 5.0;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getPermis() { return permis; }
    public void setPermis(String permis) { this.permis = permis; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public StatutChauffeur getStatut() { return statut; }
    public void setStatut(StatutChauffeur statut) { this.statut = statut; }
    
    public Date getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(Date dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    
    public double getRevenuTotal() { return revenuTotal; }
    public void setRevenuTotal(double revenuTotal) { this.revenuTotal = revenuTotal; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    
    public int getNombreCourses() { return nombreCourses; }
    public void setNombreCourses(int nombreCourses) { this.nombreCourses = nombreCourses; }
    
    public double getEvaluation() { return evaluation; }
    public void setEvaluation(double evaluation) { this.evaluation = evaluation; }
    
    public boolean isDisponible() {
        return statut == StatutChauffeur.DISPONIBLE;
    }
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public void ajouterCourse(double revenu) {
        this.nombreCourses++;
        this.revenuTotal += revenu;
    }


}