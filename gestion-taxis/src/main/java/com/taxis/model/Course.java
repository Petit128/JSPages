package com.taxis.model;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int chauffeurId;
    private int vehiculeId;
    private String clientNom;
    private String clientTelephone;
    private String clientEmail;
    private String adresseDepart;
    private String adresseArrivee;
    private double latitudeDepart;
    private double longitudeDepart;
    private double latitudeArrivee;
    private double longitudeArrivee;
    private Date dateHeure;
    private double distance;
    private double duree;
    private double tempsAttente;
    private int nombreBagages;
    private boolean animauxPresent;
    private boolean reservation;
    private double prix;
    private double commission;
    private double revenuChauffeur;
    private StatutCourse statut;
    private Date dateDebut;
    private Date dateFin;
    
    public Course() {
        this.statut = StatutCourse.EN_ATTENTE;
        this.nombreBagages = 0;
        this.animauxPresent = false;
        this.reservation = false;
        this.tempsAttente = 0;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(int chauffeurId) { this.chauffeurId = chauffeurId; }
    
    public int getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(int vehiculeId) { this.vehiculeId = vehiculeId; }
    
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    
    public String getClientTelephone() { return clientTelephone; }
    public void setClientTelephone(String clientTelephone) { this.clientTelephone = clientTelephone; }
    
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    
    public String getAdresseDepart() { return adresseDepart; }
    public void setAdresseDepart(String adresseDepart) { this.adresseDepart = adresseDepart; }
    
    public String getAdresseArrivee() { return adresseArrivee; }
    public void setAdresseArrivee(String adresseArrivee) { this.adresseArrivee = adresseArrivee; }
    
    public double getLatitudeDepart() { return latitudeDepart; }
    public void setLatitudeDepart(double latitudeDepart) { this.latitudeDepart = latitudeDepart; }
    
    public double getLongitudeDepart() { return longitudeDepart; }
    public void setLongitudeDepart(double longitudeDepart) { this.longitudeDepart = longitudeDepart; }
    
    public double getLatitudeArrivee() { return latitudeArrivee; }
    public void setLatitudeArrivee(double latitudeArrivee) { this.latitudeArrivee = latitudeArrivee; }
    
    public double getLongitudeArrivee() { return longitudeArrivee; }
    public void setLongitudeArrivee(double longitudeArrivee) { this.longitudeArrivee = longitudeArrivee; }
    
    public Date getDateHeure() { return dateHeure; }
    public void setDateHeure(Date dateHeure) { this.dateHeure = dateHeure; }
    
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    
    public double getDuree() { return duree; }
    public void setDuree(double duree) { this.duree = duree; }
    
    public double getTempsAttente() { return tempsAttente; }
    public void setTempsAttente(double tempsAttente) { this.tempsAttente = tempsAttente; }
    
    public int getNombreBagages() { return nombreBagages; }
    public void setNombreBagages(int nombreBagages) { this.nombreBagages = nombreBagages; }
    
    public boolean isAnimauxPresent() { return animauxPresent; }
    public void setAnimauxPresent(boolean animauxPresent) { this.animauxPresent = animauxPresent; }
    
    public boolean isReservation() { return reservation; }
    public void setReservation(boolean reservation) { this.reservation = reservation; }
    
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    
    public double getCommission() { return commission; }
    public void setCommission(double commission) { this.commission = commission; }
    
    public double getRevenuChauffeur() { return revenuChauffeur; }
    public void setRevenuChauffeur(double revenuChauffeur) { this.revenuChauffeur = revenuChauffeur; }
    
    public StatutCourse getStatut() { return statut; }
    public void setStatut(StatutCourse statut) { this.statut = statut; }
    
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public void setStatut(String string) {
        // Conversion de String vers Enum
        if (string != null) {
            switch (string) {
                case "EN_ATTENTE": this.statut = StatutCourse.EN_ATTENTE; break;
                case "ASSIGNEE": this.statut = StatutCourse.ASSIGNEE; break;
                case "EN_COURS": this.statut = StatutCourse.EN_COURS; break;
                case "TERMINEE": this.statut = StatutCourse.TERMINEE; break;
                case "ANNULEE": this.statut = StatutCourse.ANNULEE; break;
                default: this.statut = StatutCourse.EN_ATTENTE;
            }
        }
    }
}