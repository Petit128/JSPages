package com.taxis.controller;

import com.taxis.dao.ChauffeurDAO;
import com.taxis.model.Chauffeur;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/chauffeur/dashboard")
public class ChauffeurDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ChauffeurDAO chauffeurDAO;
    
    @Override
    public void init() {
        chauffeurDAO = new ChauffeurDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Chauffeur chauffeur = (Chauffeur) session.getAttribute("chauffeur");
        
        if (chauffeur == null) {
            Integer chauffeurId = (Integer) session.getAttribute("chauffeurId");
            if (chauffeurId != null) {
                try {
                    chauffeur = chauffeurDAO.getById(chauffeurId);
                    session.setAttribute("chauffeur", chauffeur);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (chauffeur == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        request.getRequestDispatcher("/views/chauffeur/chauffeur-dashboard.jsp").forward(request, response);
    }
}