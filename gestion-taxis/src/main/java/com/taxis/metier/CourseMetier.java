package com.taxis.metier;

import com.taxis.dao.*;
import com.taxis.model.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CourseMetier {
    private CourseDAO courseDAO;
    private ChauffeurDAO chauffeurDAO;
    private VehiculeDAO vehiculeDAO;
    private TarificationService tarificationService;
    
    public CourseMetier() {
        this.courseDAO = new CourseDAO();
        this.chauffeurDAO = new ChauffeurDAO();
        this.vehiculeDAO = new VehiculeDAO();
        this.tarificationService = new TarificationService();
    }
    
    public double calculerPrix(double distance) {
        return tarificationService.calculerPrixSimple(distance, new Date());
    }
    
    public Course assignerAutomatique(Course course) throws SQLException {
        List<Chauffeur> chauffeursDispos = chauffeurDAO.getDisponibles();
        List<Vehicule> vehiculesDispos = vehiculeDAO.getDisponibles();
        
        if (chauffeursDispos.isEmpty()) {
            throw new SQLException("Aucun chauffeur disponible pour cette course");
        }
        
        if (vehiculesDispos.isEmpty()) {
            throw new SQLException("Aucun véhicule disponible pour cette course");
        }
        
        // Sélectionner le chauffeur avec le moins de revenus (répartition équitable)
        Chauffeur meilleurChauffeur = chauffeursDispos.get(0);
        double minRevenu = meilleurChauffeur.getRevenuTotal();
        
        for (Chauffeur c : chauffeursDispos) {
            if (c.getRevenuTotal() < minRevenu) {
                minRevenu = c.getRevenuTotal();
                meilleurChauffeur = c;
            }
        }
        
        // Prendre le premier véhicule disponible
        Vehicule vehicule = vehiculesDispos.get(0);
        
        // Calculer le prix
        double prix = calculerPrix(course.getDistance());
        
        // Assigner
        course.setChauffeurId(meilleurChauffeur.getId());
        course.setVehiculeId(vehicule.getId());
        course.setPrix(prix);
        course.setStatut(StatutCourse.ASSIGNEE);
        
        if (course.getDateHeure() == null) {
            course.setDateHeure(new Date());
        }
        
        // Mettre à jour les disponibilités
        meilleurChauffeur.setStatut(StatutChauffeur.EN_COURSE);
        vehicule.setDisponible(false);
        
        chauffeurDAO.modifier(meilleurChauffeur);
        vehiculeDAO.modifier(vehicule);
        
        return course;
    }
    
    public void terminerCourse(int courseId, double distanceReelle, double dureeReelle, 
                               int nombreBagages, boolean animauxPresent) throws SQLException {
        Course course = courseDAO.getById(courseId);
        if (course == null) {
            throw new SQLException("Course non trouvée");
        }
        
        course.setStatut(StatutCourse.TERMINEE);
        course.setDistance(distanceReelle);
        course.setDuree(dureeReelle);
        course.setDateFin(new Date());
        
        // Recalculer le prix avec les valeurs réelles
        var details = tarificationService.calculerPrixDetaille(
            distanceReelle,
            dureeReelle,
            nombreBagages,
            animauxPresent,
            course.isReservation(),
            course.getDateHeure()
        );
        
        course.setPrix(details.getTotalCourse());
        course.setCommission(details.getCommission());
        course.setRevenuChauffeur(details.getRevenuChauffeur());
        
        courseDAO.modifier(course);
        
        // Libérer le chauffeur et ajouter ses revenus
        Chauffeur chauffeur = chauffeurDAO.getById(course.getChauffeurId());
        Vehicule vehicule = vehiculeDAO.getById(course.getVehiculeId());
        
        if (chauffeur != null) {
            chauffeur.setStatut(StatutChauffeur.DISPONIBLE);
            chauffeur.setRevenuTotal(chauffeur.getRevenuTotal() + details.getRevenuChauffeur());
            chauffeur.setNombreCourses(chauffeur.getNombreCourses() + 1);
            chauffeurDAO.modifier(chauffeur);
        }
        
        if (vehicule != null) {
            vehicule.setDisponible(true);
            vehicule.setKilometrage(vehicule.getKilometrage() + distanceReelle);
            vehiculeDAO.modifier(vehicule);
        }
    }
    
    public Statistiques getStatistiques() throws SQLException {
        Statistiques stats = new Statistiques();
        
        List<Course> toutesCourses = courseDAO.getAll();
        List<Chauffeur> tousChauffeurs = chauffeurDAO.getAll();
        List<Vehicule> tousVehicules = vehiculeDAO.getAll();
        
        int enCours = 0;
        int terminees = 0;
        int planifiees = 0;
        double revenuTotal = 0;
        double distanceTotale = 0;
        
        for (Course course : toutesCourses) {
            switch (course.getStatut()) {
                case EN_COURS: enCours++; break;
                case TERMINEE: 
                    terminees++; 
                    revenuTotal += course.getPrix();
                    distanceTotale += course.getDistance();
                    break;
                case ASSIGNEE: planifiees++; break;
                default: break;
            }
        }
        
        stats.setTotalCourses(toutesCourses.size());
        stats.setCoursesEnCours(enCours);
        stats.setCoursesTerminees(terminees);
        stats.setCoursesPlanifiees(planifiees);
        stats.setRevenuTotal(revenuTotal);
        stats.setDistanceTotale(distanceTotale);
        
        int chauffeursDispos = 0;
        for (Chauffeur c : tousChauffeurs) {
            if (c.getStatut() == StatutChauffeur.DISPONIBLE) chauffeursDispos++;
        }
        
        int vehiculesDispos = 0;
        for (Vehicule v : tousVehicules) {
            if (v.isDisponible()) vehiculesDispos++;
        }
        
        stats.setChauffeursDisponibles(chauffeursDispos);
        stats.setChauffeursTotal(tousChauffeurs.size());
        stats.setVehiculesDisponibles(vehiculesDispos);
        stats.setVehiculesTotal(tousVehicules.size());
        
        return stats;
    }
}