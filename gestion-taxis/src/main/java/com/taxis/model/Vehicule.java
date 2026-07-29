// model/Vehicule.java
package com.taxis.model;

import java.io.Serializable;

public class Vehicule implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String immatriculation;
    private String marque;
    private String modele;
    private int annee;
    private String couleur;
    private int nombrePlaces;
    private boolean disponible;
    private double kilometrage;
    private String etat;
    
    public Vehicule() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    
    public int getNombrePlaces() { return nombrePlaces; }
    public void setNombrePlaces(int nombrePlaces) { this.nombrePlaces = nombrePlaces; }
    
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
    public double getKilometrage() { return kilometrage; }
    public void setKilometrage(double kilometrage) { this.kilometrage = kilometrage; }
    
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}