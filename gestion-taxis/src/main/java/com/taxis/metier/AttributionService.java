package com.taxis.metier;

import com.taxis.dao.*;
import com.taxis.model.*;
import java.sql.SQLException;
import java.util.*;

public class AttributionService {
    
    private ChauffeurDAO chauffeurDAO;
    private VehiculeDAO vehiculeDAO;
    private CourseDAO courseDAO;
    private TarificationService tarificationService;
    
    public AttributionService() {
        this.chauffeurDAO = new ChauffeurDAO();
        this.vehiculeDAO = new VehiculeDAO();
        this.courseDAO = new CourseDAO();
        this.tarificationService = new TarificationService();
    }
    
    /**
     * Calcul de la distance entre deux points (formule Haversine)
     */
    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
    
    /**
     * Attribution automatique avec algorithme avancé
     */
    public Course attribuerCourseAutomatique(Course course) throws SQLException {
        List<Chauffeur> chauffeursDispos = chauffeurDAO.getDisponibles();
        List<Vehicule> vehiculesDispos = vehiculeDAO.getDisponibles();
        
        if (chauffeursDispos.isEmpty()) {
            throw new SQLException("Aucun chauffeur disponible");
        }
        
        if (vehiculesDispos.isEmpty()) {
            throw new SQLException("Aucun véhicule disponible");
        }
        
        // Calculer un score pour chaque chauffeur
        Map<Chauffeur, Double> scores = new HashMap<>();
        
        for (Chauffeur chauffeur : chauffeursDispos) {
            double score = 0;
            
            // 1. Proximité géographique (40%)
            double distance = calculerDistance(
                course.getLatitudeDepart(), course.getLongitudeDepart(),
                chauffeur.getLatitude(), chauffeur.getLongitude()
            );
            double scoreProximite = Math.max(0, 100 - distance * 10);
            score += scoreProximite * 0.4;
            
            // 2. Charge de travail (30%)
            int coursesAujourdhui = courseDAO.getCoursesDuJourChauffeur(chauffeur.getId());
            double scoreCharge = Math.max(0, 100 - (coursesAujourdhui * 10));
            score += scoreCharge * 0.3;
            
            // 3. Évaluation (20%)
            double scoreEvaluation = chauffeur.getEvaluation() * 20;
            score += scoreEvaluation * 0.2;
            
            // 4. Ancienneté (10%)
            long joursAnciennete = (System.currentTimeMillis() - chauffeur.getDateEmbauche().getTime()) / (1000 * 60 * 60 * 24);
            double scoreAnciennete = Math.min(100, joursAnciennete);
            score += scoreAnciennete * 0.1;
            
            scores.put(chauffeur, score);
        }
        
        // Sélectionner le meilleur chauffeur
        Chauffeur meilleurChauffeur = Collections.max(scores.entrySet(), Map.Entry.comparingByValue()).getKey();
        
        // Sélectionner le véhicule
        Vehicule meilleurVehicule = selectionnerMeilleurVehicule(vehiculesDispos, course);
        
        // Calculer le prix
        var detailsPrix = tarificationService.calculerPrixDetaille(
            course.getDistance(),
            course.getTempsAttente(),
            course.getNombreBagages(),
            course.isAnimauxPresent(),
            course.isReservation(),
            course.getDateHeure() != null ? course.getDateHeure() : new Date()
        );
        
        // Assigner la course
        course.setChauffeurId(meilleurChauffeur.getId());
        course.setVehiculeId(meilleurVehicule.getId());
        course.setPrix(detailsPrix.getTotalCourse());
        course.setCommission(detailsPrix.getCommission());
        course.setRevenuChauffeur(detailsPrix.getRevenuChauffeur());
        course.setStatut(StatutCourse.ASSIGNEE);
        
        // Mettre à jour les disponibilités
        meilleurChauffeur.setStatut(StatutChauffeur.EN_COURSE);
        meilleurVehicule.setDisponible(false);
        
        chauffeurDAO.modifier(meilleurChauffeur);
        vehiculeDAO.modifier(meilleurVehicule);
        
        return course;
    }
    
    private Vehicule selectionnerMeilleurVehicule(List<Vehicule> vehicules, Course course) {
        return vehicules.stream()
            .max((v1, v2) -> Integer.compare(v1.getNombrePlaces(), v2.getNombrePlaces()))
            .orElse(vehicules.get(0));
    }
    
    public Course reassignerCourse(int courseId) throws SQLException {
        Course course = courseDAO.getById(courseId);
        if (course == null) {
            throw new SQLException("Course non trouvée");
        }
        
        // Libérer l'ancien chauffeur
        if (course.getChauffeurId() > 0) {
            Chauffeur ancienChauffeur = chauffeurDAO.getById(course.getChauffeurId());
            if (ancienChauffeur != null) {
                ancienChauffeur.setStatut(StatutChauffeur.DISPONIBLE);
                chauffeurDAO.modifier(ancienChauffeur);
            }
        }
        
        // Réinitialiser la course
        course.setChauffeurId(0);
        course.setVehiculeId(0);
        course.setStatut(StatutCourse.EN_ATTENTE);
        
        // Réattribuer automatiquement
        return attribuerCourseAutomatique(course);
    }
}