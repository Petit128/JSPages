// controller/CalculPrixServlet.java
package com.taxis.controller;

import com.taxis.metier.TarificationService;
import com.taxis.metier.TarificationService.DetailsPrix;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/calcul-prix")
public class CalculPrixServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TarificationService tarificationService;
    
    @Override
    public void init() {
        tarificationService = new TarificationService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("tarificationService", tarificationService);
        request.getRequestDispatcher("/views/calcul-prix.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Récupérer les paramètres
            double distance = Double.parseDouble(request.getParameter("distance"));
            double tempsAttente = Double.parseDouble(request.getParameter("tempsAttente"));
            int nombreBagages = Integer.parseInt(request.getParameter("nombreBagages"));
            boolean animauxPresent = request.getParameter("animauxPresent") != null;
            boolean reservation = request.getParameter("reservation") != null;
            
            // Date et heure
            Date dateHeure = new Date();
            String dateHeureStr = request.getParameter("dateHeure");
            if (dateHeureStr != null && !dateHeureStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                dateHeure = sdf.parse(dateHeureStr);
            }
            
            // Calculer le prix détaillé
            DetailsPrix details = tarificationService.calculerPrixDetaille(
                distance, tempsAttente, nombreBagages, animauxPresent, reservation, dateHeure
            );
            
            request.setAttribute("detailsPrix", details);
            request.setAttribute("tarificationService", tarificationService);
            request.getRequestDispatcher("/views/calcul-prix.jsp").forward(request, response);
            
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
            response.sendRedirect("calcul-prix");
        }
    }
}