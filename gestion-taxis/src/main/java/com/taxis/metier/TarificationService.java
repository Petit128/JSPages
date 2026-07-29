package com.taxis.metier;

import java.io.Serializable;
import java.util.Date;

public class TarificationService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Tarifs de base en Ariary (MGA)
    private static final double PRIX_PRISE_EN_CHARGE = 17500;      // 3.50€ × 5000
    private static final double PRIX_PAR_KM_JOUR = 6000;           // 1.20€ × 5000
    private static final double PRIX_PAR_KM_NUIT = 9000;           // 1.80€ × 5000
    private static final double PRIX_PAR_MINUTE_ATTENTE = 2500;    // 0.50€ × 5000
    
    // Horaires nuit (22h - 6h)
    private static final int DEBUT_NUIT = 22;
    private static final int FIN_NUIT = 6;
    
    // Frais supplémentaires en Ariary
    private static final double FRAIS_BAGAGES = 10000;             // 2.00€ × 5000
    private static final double FRAIS_ANIMAUX = 15000;             // 3.00€ × 5000
    private static final double MAJORATION_NUIT = 1.5;             // Majoration de 50% la nuit
    private static final double MAJORATION_WEEKEND = 1.2;          // Majoration de 20% le weekend
    private static final double MAJORATION_RESERVATION = 10000;    // 2.00€ × 5000
    
    // Commission
    private static final double COMMISSION_ENTREPRISE = 0.20;       // 20% de commission
    
    /**
     * Structure pour stocker le détail du calcul en Ariary
     */
    public static class DetailsPrix implements Serializable {
        private double prixBase;
        private double prixDistance;
        private double prixAttente;
        private double fraisSupplementaires;
        private double majorations;
        private double sousTotal;
        private double commission;
        private double totalCourse;
        private double revenuChauffeur;
        private double revenuEntreprise;
        private String details;
        
        public double getPrixBase() { return prixBase; }
        public double getPrixDistance() { return prixDistance; }
        public double getPrixAttente() { return prixAttente; }
        public double getFraisSupplementaires() { return fraisSupplementaires; }
        public double getMajorations() { return majorations; }
        public double getSousTotal() { return sousTotal; }
        public double getCommission() { return commission; }
        public double getTotalCourse() { return totalCourse; }
        public double getRevenuChauffeur() { return revenuChauffeur; }
        public double getRevenuEntreprise() { return revenuEntreprise; }
        public String getDetails() { return details; }
        
        public void setPrixBase(double prixBase) { this.prixBase = prixBase; }
        public void setPrixDistance(double prixDistance) { this.prixDistance = prixDistance; }
        public void setPrixAttente(double prixAttente) { this.prixAttente = prixAttente; }
        public void setFraisSupplementaires(double fraisSupplementaires) { this.fraisSupplementaires = fraisSupplementaires; }
        public void setMajorations(double majorations) { this.majorations = majorations; }
        public void setSousTotal(double sousTotal) { this.sousTotal = sousTotal; }
        public void setCommission(double commission) { this.commission = commission; }
        public void setTotalCourse(double totalCourse) { this.totalCourse = totalCourse; }
        public void setRevenuChauffeur(double revenuChauffeur) { this.revenuChauffeur = revenuChauffeur; }
        public void setRevenuEntreprise(double revenuEntreprise) { this.revenuEntreprise = revenuEntreprise; }
        public void setDetails(String details) { this.details = details; }
        
        @Override
        public String toString() {
            return details;
        }
    }
    
    private boolean estHeureNuit(Date date) {
        if (date == null) return false;
        @SuppressWarnings("deprecation")
        int heure = date.getHours();
        return (heure >= DEBUT_NUIT || heure < FIN_NUIT);
    }
    
    private boolean estWeekend(Date date) {
        if (date == null) return false;
        @SuppressWarnings("deprecation")
        int jour = date.getDay();
        return (jour == 0 || jour == 6);
    }
    
    private double getPrixKm(Date date) {
        if (estHeureNuit(date)) {
            return PRIX_PAR_KM_NUIT;
        }
        return PRIX_PAR_KM_JOUR;
    }
    
    private double calculerMajorations(Date date, boolean reservation) {
        double majorations = 0;
        StringBuilder sb = new StringBuilder();
        
        if (estHeureNuit(date)) {
            majorations += MAJORATION_NUIT;
            sb.append("Majoration nuit (x").append(MAJORATION_NUIT).append(") ");
        }
        
        if (estWeekend(date)) {
            majorations += MAJORATION_WEEKEND;
            sb.append("Majoration weekend (x").append(MAJORATION_WEEKEND).append(") ");
        }
        
        if (reservation) {
            majorations += MAJORATION_RESERVATION;
            sb.append("+ Frais réservation (");
            sb.append(String.format("%,.0f", MAJORATION_RESERVATION)).append(" Ar) ");
        }
        
        return majorations;
    }
    
    public DetailsPrix calculerPrixDetaille(double distance, double tempsAttente, 
                                            int nombreBagages, boolean animauxPresent,
                                            boolean reservation, Date dateHeure) {
        DetailsPrix details = new DetailsPrix();
        StringBuilder detailTexte = new StringBuilder();
        
        // 1. Prix de base (prise en charge)
        details.setPrixBase(PRIX_PRISE_EN_CHARGE);
        detailTexte.append(String.format("Prise en charge: %,.0f Ar\n", PRIX_PRISE_EN_CHARGE));
        
        // 2. Prix selon distance
        double prixKm = getPrixKm(dateHeure);
        double prixDistance = distance * prixKm;
        details.setPrixDistance(prixDistance);
        detailTexte.append(String.format("Distance (%.1f km x %,.0f Ar/km): %,.0f Ar\n", 
                          distance, prixKm, prixDistance));
        
        // 3. Prix du temps d'attente
        double prixAttente = tempsAttente * PRIX_PAR_MINUTE_ATTENTE;
        details.setPrixAttente(prixAttente);
        if (tempsAttente > 0) {
            detailTexte.append(String.format("Temps d'attente (%.0f min x %,.0f Ar): %,.0f Ar\n", 
                              tempsAttente, PRIX_PAR_MINUTE_ATTENTE, prixAttente));
        }
        
        // 4. Frais supplémentaires
        double fraisSupp = 0;
        fraisSupp += nombreBagages * FRAIS_BAGAGES;
        if (nombreBagages > 0) {
            detailTexte.append(String.format("Bagages (%d x %,.0f Ar): %,.0f Ar\n", 
                              nombreBagages, FRAIS_BAGAGES, nombreBagages * FRAIS_BAGAGES));
        }
        
        if (animauxPresent) {
            fraisSupp += FRAIS_ANIMAUX;
            detailTexte.append(String.format("Frais animaux: %,.0f Ar\n", FRAIS_ANIMAUX));
        }
        details.setFraisSupplementaires(fraisSupp);
        
        // 5. Sous-total avant majorations
        double sousTotal = details.getPrixBase() + details.getPrixDistance() + 
                          details.getPrixAttente() + details.getFraisSupplementaires();
        details.setSousTotal(sousTotal);
        detailTexte.append(String.format("Sous-total: %,.0f Ar\n", sousTotal));
        
        // 6. Majorations
        double majorations = calculerMajorations(dateHeure, reservation);
        details.setMajorations(majorations);
        if (majorations > 0) {
            detailTexte.append(String.format("Majorations: %,.0f Ar\n", majorations));
        }
        
        // 7. Total course avant commission
        double totalCourse = sousTotal + majorations;
        details.setTotalCourse(totalCourse);
        detailTexte.append(String.format("Total course: %,.0f Ar\n", totalCourse));
        
        // 8. Commission entreprise
        double commission = totalCourse * COMMISSION_ENTREPRISE;
        details.setCommission(commission);
        detailTexte.append(String.format("Commission entreprise (%.0f%%): %,.0f Ar\n", 
                          COMMISSION_ENTREPRISE * 100, commission));
        
        // 9. Revenu chauffeur
        double revenuChauffeur = totalCourse - commission;
        details.setRevenuChauffeur(revenuChauffeur);
        detailTexte.append(String.format("Revenu chauffeur: %,.0f Ar\n", revenuChauffeur));
        
        // 10. Revenu entreprise
        details.setRevenuEntreprise(commission);
        
        details.setDetails(detailTexte.toString());
        
        return details;
    }
    
    public double calculerPrixSimple(double distance, Date dateHeure) {
        double prixKm = getPrixKm(dateHeure);
        return PRIX_PRISE_EN_CHARGE + (distance * prixKm);
    }
    
    public double calculerRevenuChauffeur(double prixCourse) {
        return prixCourse * (1 - COMMISSION_ENTREPRISE);
    }
    
    public double calculerRevenuEntreprise(double prixCourse) {
        return prixCourse * COMMISSION_ENTREPRISE;
    }
    
    public double calculerCommission(double prixCourse) {
        return prixCourse * COMMISSION_ENTREPRISE;
    }
    
    // Getters pour l'affichage
    public double getPrixPriseEnCharge() { return PRIX_PRISE_EN_CHARGE; }
    public double getPrixParKmJour() { return PRIX_PAR_KM_JOUR; }
    public double getPrixParKmNuit() { return PRIX_PAR_KM_NUIT; }
    public double getPrixParMinuteAttente() { return PRIX_PAR_MINUTE_ATTENTE; }
    public double getFraisBagages() { return FRAIS_BAGAGES; }
    public double getFraisAnimaux() { return FRAIS_ANIMAUX; }
    public double getCommissionEntreprise() { return COMMISSION_ENTREPRISE; }
}