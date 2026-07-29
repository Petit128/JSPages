// metier/Statistiques.java
package com.taxis.metier;

import java.io.Serializable;

public class Statistiques implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCourses;
    private int coursesEnCours;
    private int coursesTerminees;
    private int coursesPlanifiees;
    private double revenuTotal;
    private double distanceTotale;
    private int chauffeursDisponibles;
    private int chauffeursTotal;
    private int vehiculesDisponibles;
    private int vehiculesTotal;
    
    // Getters and Setters
    public int getTotalCourses() { return totalCourses; }
    public void setTotalCourses(int totalCourses) { this.totalCourses = totalCourses; }
    
    public int getCoursesEnCours() { return coursesEnCours; }
    public void setCoursesEnCours(int coursesEnCours) { this.coursesEnCours = coursesEnCours; }
    
    public int getCoursesTerminees() { return coursesTerminees; }
    public void setCoursesTerminees(int coursesTerminees) { this.coursesTerminees = coursesTerminees; }
    
    public int getCoursesPlanifiees() { return coursesPlanifiees; }
    public void setCoursesPlanifiees(int coursesPlanifiees) { this.coursesPlanifiees = coursesPlanifiees; }
    
    public double getRevenuTotal() { return revenuTotal; }
    public void setRevenuTotal(double revenuTotal) { this.revenuTotal = revenuTotal; }
    
    public double getDistanceTotale() { return distanceTotale; }
    public void setDistanceTotale(double distanceTotale) { this.distanceTotale = distanceTotale; }
    
    public int getChauffeursDisponibles() { return chauffeursDisponibles; }
    public void setChauffeursDisponibles(int chauffeursDisponibles) { this.chauffeursDisponibles = chauffeursDisponibles; }
    
    public int getChauffeursTotal() { return chauffeursTotal; }
    public void setChauffeursTotal(int chauffeursTotal) { this.chauffeursTotal = chauffeursTotal; }
    
    public int getVehiculesDisponibles() { return vehiculesDisponibles; }
    public void setVehiculesDisponibles(int vehiculesDisponibles) { this.vehiculesDisponibles = vehiculesDisponibles; }
    
    public int getVehiculesTotal() { return vehiculesTotal; }
    public void setVehiculesTotal(int vehiculesTotal) { this.vehiculesTotal = vehiculesTotal; }
    
    public double getTauxDisponibiliteChauffeurs() {
        if (chauffeursTotal == 0) return 0;
        return (double) chauffeursDisponibles / chauffeursTotal * 100;
    }
    
    public double getTauxDisponibiliteVehicules() {
        if (vehiculesTotal == 0) return 0;
        return (double) vehiculesDisponibles / vehiculesTotal * 100;
    }
}