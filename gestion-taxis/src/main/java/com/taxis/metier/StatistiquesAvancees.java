package com.taxis.metier;

import com.taxis.dao.ChauffeurDAO;
import com.taxis.dao.CourseDAO;
import com.taxis.dao.VehiculeDAO;
import com.taxis.model.Chauffeur;
import com.taxis.model.Course;
import com.taxis.model.StatutCourse;
import com.taxis.model.Vehicule;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class StatistiquesAvancees {
    
    private CourseDAO courseDAO;
    private ChauffeurDAO chauffeurDAO;
    private VehiculeDAO vehiculeDAO;
    
    public StatistiquesAvancees() {
        this.courseDAO = new CourseDAO();
        this.chauffeurDAO = new ChauffeurDAO();
        this.vehiculeDAO = new VehiculeDAO();
    }
    
    /**
     * Statistiques complètes pour le dashboard
     */
    public Map<String, Object> getToutesStatistiques() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        List<Course> toutesCourses = courseDAO.getAll();
        List<Chauffeur> tousChauffeurs = chauffeurDAO.getAll();
        List<Vehicule> tousVehicules = vehiculeDAO.getAll();
        
        // Statistiques de base
        stats.put("totalCourses", toutesCourses.size());
        stats.put("totalChauffeurs", tousChauffeurs.size());
        stats.put("totalVehicules", tousVehicules.size());
        
        // Courses par statut
        Map<String, Long> coursesParStatut = toutesCourses.stream()
            .collect(Collectors.groupingBy(c -> c.getStatut().name(), Collectors.counting()));
        stats.put("coursesParStatut", coursesParStatut);
        
        // Revenus
        double revenuTotal = toutesCourses.stream()
            .filter(c -> c.getStatut() == StatutCourse.TERMINEE)
            .mapToDouble(Course::getPrix)
            .sum();
        stats.put("revenuTotal", revenuTotal);
        
        // Distance totale
        double distanceTotale = toutesCourses.stream()
            .mapToDouble(Course::getDistance)
            .sum();
        stats.put("distanceTotale", distanceTotale);
        
        // Taux d'utilisation des chauffeurs
        long chauffeursDispos = tousChauffeurs.stream()
            .filter(c -> c.isDisponible())
            .count();
        stats.put("tauxUtilisationChauffeurs", tousChauffeurs.isEmpty() ? 0 : 
            (double)(tousChauffeurs.size() - chauffeursDispos) / tousChauffeurs.size() * 100);
        
        // Taux d'utilisation des véhicules
        long vehiculesDispos = tousVehicules.stream()
            .filter(Vehicule::isDisponible)
            .count();
        stats.put("tauxUtilisationVehicules", tousVehicules.isEmpty() ? 0 : 
            (double)(tousVehicules.size() - vehiculesDispos) / tousVehicules.size() * 100);
        
        // Temps moyen d'attente
        double tempsMoyenAttente = toutesCourses.stream()
            .filter(c -> c.getTempsAttente() > 0)
            .mapToDouble(Course::getTempsAttente)
            .average()
            .orElse(0);
        stats.put("tempsMoyenAttente", tempsMoyenAttente);
        
        // Courses annulées vs réussies
        long coursesTerminees = toutesCourses.stream()
            .filter(c -> c.getStatut() == StatutCourse.TERMINEE)
            .count();
        long coursesAnnulees = toutesCourses.stream()
            .filter(c -> c.getStatut() == StatutCourse.ANNULEE)
            .count();
        stats.put("tauxReussite", toutesCourses.isEmpty() ? 0 : (double)coursesTerminees / toutesCourses.size() * 100);
        stats.put("tauxAnnulation", toutesCourses.isEmpty() ? 0 : (double)coursesAnnulees / toutesCourses.size() * 100);
        
        return stats;
    }
    
    /**
     * Nombre de courses par chauffeur
     */
    public Map<String, Integer> getCoursesParChauffeur() throws SQLException {
        Map<String, Integer> result = new HashMap<>();
        List<Chauffeur> chauffeurs = chauffeurDAO.getAll();
        
        for (Chauffeur c : chauffeurs) {
            List<Course> courses = courseDAO.getByChauffeurId(c.getId());
            result.put(c.getNomComplet(), courses.size());
        }
        
        return result;
    }
    
    /**
     * Revenus par chauffeur
     */
    public Map<String, Double> getRevenusParChauffeur() throws SQLException {
        Map<String, Double> result = new HashMap<>();
        List<Chauffeur> chauffeurs = chauffeurDAO.getAll();
        
        for (Chauffeur c : chauffeurs) {
            List<Course> courses = courseDAO.getByChauffeurId(c.getId());
            double revenu = courses.stream()
                .filter(course -> course.getStatut() == StatutCourse.TERMINEE)
                .mapToDouble(Course::getRevenuChauffeur)
                .sum();
            result.put(c.getNomComplet(), revenu);
        }
        
        return result;
    }
    
    /**
     * Revenus par véhicule
     */
    public Map<String, Double> getRevenusParVehicule() throws SQLException {
        Map<String, Double> result = new HashMap<>();
        List<Vehicule> vehicules = vehiculeDAO.getAll();
        List<Course> toutesCourses = courseDAO.getAll();
        
        for (Vehicule v : vehicules) {
            double revenu = toutesCourses.stream()
                .filter(c -> c.getVehiculeId() == v.getId() && c.getStatut() == StatutCourse.TERMINEE)
                .mapToDouble(Course::getPrix)
                .sum();
            result.put(v.getMarque() + " " + v.getModele() + " (" + v.getImmatriculation() + ")", revenu);
        }
        
        return result;
    }
    
    /**
     * Revenus par période (jour, semaine, mois)
     */
    public Map<String, Double> getRevenusParPeriode(String periode) throws SQLException {
        Map<String, Double> result = new LinkedHashMap<>();
        List<Course> courses = courseDAO.getAll();
        Calendar cal = Calendar.getInstance();
        
        switch (periode.toLowerCase()) {
            case "jour":
                // Derniers 7 jours
                for (int i = 6; i >= 0; i--) {
                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_MONTH, -i);
                    String dateKey = new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                    double revenu = courses.stream()
                        .filter(c -> c.getStatut() == StatutCourse.TERMINEE)
                        .filter(c -> {
                            Calendar cCal = Calendar.getInstance();
                            cCal.setTime(c.getDateHeure());
                            return cCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                                   cCal.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR);
                        })
                        .mapToDouble(Course::getPrix)
                        .sum();
                    result.put(dateKey, revenu);
                }
                break;
                
            case "semaine":
                // Dernières 8 semaines
                for (int i = 7; i >= 0; i--) {
                    cal.setTime(new Date());
                    cal.add(Calendar.WEEK_OF_YEAR, -i);
                    int semaine = cal.get(Calendar.WEEK_OF_YEAR);
                    int annee = cal.get(Calendar.YEAR);
                    String semaineKey = "S" + semaine + "-" + annee;
                    
                    double revenu = courses.stream()
                        .filter(c -> c.getStatut() == StatutCourse.TERMINEE)
                        .filter(c -> {
                            Calendar cCal = Calendar.getInstance();
                            cCal.setTime(c.getDateHeure());
                            return cCal.get(Calendar.WEEK_OF_YEAR) == semaine &&
                                   cCal.get(Calendar.YEAR) == annee;
                        })
                        .mapToDouble(Course::getPrix)
                        .sum();
                    result.put(semaineKey, revenu);
                }
                break;
                
            case "mois":
                // Derniers 12 mois
                for (int i = 11; i >= 0; i--) {
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, -i);
                    String moisKey = new java.text.SimpleDateFormat("MMM yyyy").format(cal.getTime());
                    int mois = cal.get(Calendar.MONTH);
                    int annee = cal.get(Calendar.YEAR);
                    
                    double revenu = courses.stream()
                        .filter(c -> c.getStatut() == StatutCourse.TERMINEE)
                        .filter(c -> {
                            Calendar cCal = Calendar.getInstance();
                            cCal.setTime(c.getDateHeure());
                            return cCal.get(Calendar.MONTH) == mois &&
                                   cCal.get(Calendar.YEAR) == annee;
                        })
                        .mapToDouble(Course::getPrix)
                        .sum();
                    result.put(moisKey, revenu);
                }
                break;
        }
        
        return result;
    }
    
    /**
     * Top chauffeurs par revenus
     */
    public List<Map<String, Object>> getTopChauffeurs(int limit) throws SQLException {
        List<Map<String, Object>> top = new ArrayList<>();
        List<Chauffeur> chauffeurs = chauffeurDAO.getAll();
        
        for (Chauffeur c : chauffeurs) {
            List<Course> courses = courseDAO.getByChauffeurId(c.getId());
            double revenu = courses.stream()
                .filter(course -> course.getStatut() == StatutCourse.TERMINEE)
                .mapToDouble(Course::getRevenuChauffeur)
                .sum();
            int nbCourses = (int) courses.stream()
                .filter(course -> course.getStatut() == StatutCourse.TERMINEE)
                .count();
            
            Map<String, Object> chauffeurStats = new HashMap<>();
            chauffeurStats.put("id", c.getId());
            chauffeurStats.put("nom", c.getNomComplet());
            chauffeurStats.put("revenu", revenu);
            chauffeurStats.put("nbCourses", nbCourses);
            chauffeurStats.put("evaluation", c.getEvaluation());
            top.add(chauffeurStats);
        }
        
        return top.stream()
            .sorted((a, b) -> Double.compare((Double)b.get("revenu"), (Double)a.get("revenu")))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Top véhicules par utilisation
     */
    public List<Map<String, Object>> getTopVehicules(int limit) throws SQLException {
        List<Map<String, Object>> top = new ArrayList<>();
        List<Vehicule> vehicules = vehiculeDAO.getAll();
        List<Course> toutesCourses = courseDAO.getAll();
        
        for (Vehicule v : vehicules) {
            long nbCourses = toutesCourses.stream()
                .filter(c -> c.getVehiculeId() == v.getId())
                .count();
            double distance = toutesCourses.stream()
                .filter(c -> c.getVehiculeId() == v.getId())
                .mapToDouble(Course::getDistance)
                .sum();
            
            Map<String, Object> vehiculeStats = new HashMap<>();
            vehiculeStats.put("id", v.getId());
            vehiculeStats.put("nom", v.getMarque() + " " + v.getModele());
            vehiculeStats.put("immatriculation", v.getImmatriculation());
            vehiculeStats.put("nbCourses", nbCourses);
            vehiculeStats.put("distance", distance);
            vehiculeStats.put("kilometrage", v.getKilometrage());
            top.add(vehiculeStats);
        }
        
        return top.stream()
            .sorted((a, b) -> Long.compare((Long)b.get("nbCourses"), (Long)a.get("nbCourses")))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Statistiques pour le dashboard API
     */
    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        List<Chauffeur> tousChauffeurs = chauffeurDAO.getAll();
        List<Vehicule> tousVehicules = vehiculeDAO.getAll();
        List<Course> coursesAujourdhui = getCoursesDuJour();
        
        stats.put("totalChauffeurs", tousChauffeurs.size());
        stats.put("totalVehicules", tousVehicules.size());
        stats.put("coursesAujourdhui", coursesAujourdhui.size());
        
        // Revenus du mois
        double revenusMois = getRevenusMois();
        stats.put("revenusMois", revenusMois);
        
        // Chauffeurs disponibles
        long chauffeursDispos = tousChauffeurs.stream()
            .filter(c -> c.isDisponible())
            .count();
        stats.put("chauffeursDisponibles", chauffeursDispos);
        
        // Véhicules disponibles
        long vehiculesDispos = tousVehicules.stream()
            .filter(Vehicule::isDisponible)
            .count();
        stats.put("vehiculesDisponibles", vehiculesDispos);
        
        return stats;
    }
    
    /**
     * Courses du jour
     */
    private List<Course> getCoursesDuJour() throws SQLException {
        List<Course> toutesCourses = courseDAO.getAll();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        
        return toutesCourses.stream()
            .filter(c -> {
                Calendar courseCal = Calendar.getInstance();
                courseCal.setTime(c.getDateHeure());
                return courseCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                       courseCal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Revenus du mois
     */
    private double getRevenusMois() throws SQLException {
        List<Course> toutesCourses = courseDAO.getAll();
        Calendar now = Calendar.getInstance();
        int moisActuel = now.get(Calendar.MONTH);
        int anneeActuelle = now.get(Calendar.YEAR);
        
        return toutesCourses.stream()
            .filter(c -> c.getStatut() == StatutCourse.TERMINEE)
            .filter(c -> {
                Calendar courseCal = Calendar.getInstance();
                courseCal.setTime(c.getDateHeure());
                return courseCal.get(Calendar.MONTH) == moisActuel &&
                       courseCal.get(Calendar.YEAR) == anneeActuelle;
            })
            .mapToDouble(Course::getPrix)
            .sum();
    }
}