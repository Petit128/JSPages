package com.taxis.controller;

import com.taxis.metier.CourseMetier;
import com.taxis.metier.Statistiques;
import com.taxis.metier.StatistiquesAvancees;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/statistiques")
public class StatistiquesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourseMetier courseMetier;
    private StatistiquesAvancees statsAvancees;
    
    @Override
    public void init() {
        courseMetier = new CourseMetier();
        statsAvancees = new StatistiquesAvancees();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Statistiques de base
            Statistiques stats = courseMetier.getStatistiques();
            request.setAttribute("stats", stats);
            
            // Statistiques avancées
            request.setAttribute("statsAvancees", statsAvancees.getToutesStatistiques());
            request.setAttribute("topChauffeurs", statsAvancees.getTopChauffeurs(5));
            request.setAttribute("topVehicules", statsAvancees.getTopVehicules(5));
            request.setAttribute("revenusParJour", statsAvancees.getRevenusParPeriode("jour"));
            request.setAttribute("revenusParMois", statsAvancees.getRevenusParPeriode("mois"));
            
            request.getRequestDispatcher("/views/statistiques.jsp").forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }
}