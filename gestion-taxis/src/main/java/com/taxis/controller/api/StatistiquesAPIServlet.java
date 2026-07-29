package com.taxis.controller.api;

import com.taxis.metier.StatistiquesAvancees;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/statistiques/*")
public class StatistiquesAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StatistiquesAvancees statsAvancees;
    private Gson gson;
    
    @Override
    public void init() {
        statsAvancees = new StatistiquesAvancees();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Toutes les statistiques
                out.print(gson.toJson(statsAvancees.getToutesStatistiques()));
            } else if (pathInfo.equals("/dashboard")) {
                // Statistiques pour le dashboard
                out.print(gson.toJson(statsAvancees.getDashboardStats()));
            } else if (pathInfo.equals("/top-chauffeurs")) {
                // Top chauffeurs
                out.print(gson.toJson(statsAvancees.getTopChauffeurs(5)));
            } else if (pathInfo.equals("/revenus/jour")) {
                // Revenus par jour
                out.print(gson.toJson(statsAvancees.getRevenusParPeriode("jour")));
            } else if (pathInfo.equals("/revenus/semaine")) {
                // Revenus par semaine
                out.print(gson.toJson(statsAvancees.getRevenusParPeriode("semaine")));
            } else if (pathInfo.equals("/revenus/mois")) {
                // Revenus par mois
                out.print(gson.toJson(statsAvancees.getRevenusParPeriode("mois")));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(errorResponse("Endpoint non trouvé")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(errorResponse(e.getMessage())));
        }
    }
    
    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}