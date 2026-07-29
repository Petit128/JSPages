package com.taxis.model;

import java.io.Serializable;
import java.util.Date;

public class Parametres implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private double prixPriseEnCharge;      // En Ariary
    private double prixKmJour;              // En Ariary/km
    private double prixKmNuit;              // En Ariary/km
    private double prixAttenteMin;          // En Ariary/minute
    private double commissionEntreprise;    // En pourcentage (%)
    private double fraisBagages;            // En Ariary
    private double fraisAnimaux;            // En Ariary
    private double majorationNuit;          // En pourcentage (%)
    private double majorationWeekend;       // En pourcentage (%)
    private Date updatedAt;
    
    // Tarifs par défaut en Ariary (1€ = 5000 Ar)
    public static final double DEFAULT_PRIX_PRISE_EN_CHARGE = 17500;      // 3.50€ × 5000
    public static final double DEFAULT_PRIX_KM_JOUR = 6000;                // 1.20€ × 5000
    public static final double DEFAULT_PRIX_KM_NUIT = 9000;                // 1.80€ × 5000
    public static final double DEFAULT_PRIX_ATTENTE_MIN = 2500;            // 0.50€ × 5000
    public static final double DEFAULT_FRAIS_BAGAGES = 10000;              // 2.00€ × 5000
    public static final double DEFAULT_FRAIS_ANIMAUX = 15000;              // 3.00€ × 5000
    public static final double DEFAULT_MAJORATION_NUIT = 50;               // 50%
    public static final double DEFAULT_MAJORATION_WEEKEND = 20;            // 20%
    public static final double DEFAULT_COMMISSION = 20;                    // 20%
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public double getPrixPriseEnCharge() { return prixPriseEnCharge; }
    public void setPrixPriseEnCharge(double prixPriseEnCharge) { this.prixPriseEnCharge = prixPriseEnCharge; }
    
    public double getPrixKmJour() { return prixKmJour; }
    public void setPrixKmJour(double prixKmJour) { this.prixKmJour = prixKmJour; }
    
    public double getPrixKmNuit() { return prixKmNuit; }
    public void setPrixKmNuit(double prixKmNuit) { this.prixKmNuit = prixKmNuit; }
    
    public double getPrixAttenteMin() { return prixAttenteMin; }
    public void setPrixAttenteMin(double prixAttenteMin) { this.prixAttenteMin = prixAttenteMin; }
    
    public double getCommissionEntreprise() { return commissionEntreprise; }
    public void setCommissionEntreprise(double commissionEntreprise) { this.commissionEntreprise = commissionEntreprise; }
    
    public double getFraisBagages() { return fraisBagages; }
    public void setFraisBagages(double fraisBagages) { this.fraisBagages = fraisBagages; }
    
    public double getFraisAnimaux() { return fraisAnimaux; }
    public void setFraisAnimaux(double fraisAnimaux) { this.fraisAnimaux = fraisAnimaux; }
    
    public double getMajorationNuit() { return majorationNuit; }
    public void setMajorationNuit(double majorationNuit) { this.majorationNuit = majorationNuit; }
    
    public double getMajorationWeekend() { return majorationWeekend; }
    public void setMajorationWeekend(double majorationWeekend) { this.majorationWeekend = majorationWeekend; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}