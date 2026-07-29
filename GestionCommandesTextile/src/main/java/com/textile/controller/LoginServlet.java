package com.textile.controller;

import com.textile.dao.UtilisateurDAO;
import com.textile.model.Utilisateur;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtilisateurDAO utilisateurDAO;
    
    @Override
    public void init() {
        utilisateurDAO = new UtilisateurDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Chemin corrigé
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/login.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        
        try {
            if (utilisateurDAO.verifierAuthentification(email, motDePasse)) {
                Utilisateur utilisateur = utilisateurDAO.getUtilisateurByEmail(email);
                
                HttpSession session = request.getSession();
                session.setAttribute("utilisateur", utilisateur);
                session.setAttribute("utilisateurId", utilisateur.getId());
                session.setAttribute("role", utilisateur.getRole());
                session.setAttribute("nom", utilisateur.getNom());
                session.setAttribute("email", utilisateur.getEmail());
                
                // Mettre à jour la dernière connexion
                utilisateurDAO.updateLastLogin(utilisateur.getId(), new Timestamp(System.currentTimeMillis()));
                
                // Rediriger selon le rôle
                String role = utilisateur.getRole();
                if ("ADMIN".equals(role)) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else if ("RESPONSABLE_PRODUCTION".equals(role) || "OPERATEUR".equals(role)) {
                    response.sendRedirect(request.getContextPath() + "/commande/");
                } else {
                    response.sendRedirect(request.getContextPath() + "/client/dashboard");
                }
            } else {
                request.setAttribute("erreur", "Email ou mot de passe incorrect");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur technique: " + e.getMessage());
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}