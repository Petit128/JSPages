package com.textile.controller;

import com.textile.dao.UtilisateurDAO;
import com.textile.model.Utilisateur;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtilisateurDAO utilisateurDAO;
    
    @Override
    public void init() {
        utilisateurDAO = new UtilisateurDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Chemin corrigé : les JSP sont dans /jsp/
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/inscription.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nom = request.getParameter("nom");
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        String confirmMotDePasse = request.getParameter("confirmMotDePasse");
        
        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            request.setAttribute("erreur", "Le nom est requis");
            request.getRequestDispatcher("/jsp/inscription.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("erreur", "L'email est requis");
            request.getRequestDispatcher("/jsp/inscription.jsp").forward(request, response);
            return;
        }
        
        if (motDePasse == null || motDePasse.length() < 4) {
            request.setAttribute("erreur", "Le mot de passe doit contenir au moins 4 caractères");
            request.getRequestDispatcher("/jsp/inscription.jsp").forward(request, response);
            return;
        }
        
        if (!motDePasse.equals(confirmMotDePasse)) {
            request.setAttribute("erreur", "Les mots de passe ne correspondent pas");
            request.getRequestDispatcher("/jsp/inscription.jsp").forward(request, response);
            return;
        }
        
        try {
            // Vérifier si l'utilisateur existe déjà
            Utilisateur existant = utilisateurDAO.getUtilisateurByEmail(email);
            if (existant != null) {
                request.setAttribute("erreur", "Cet email est déjà utilisé");
                request.getRequestDispatcher("/jsp/inscription.jsp").forward(request, response);
                return;
            }
            
            // Créer le nouvel utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(nom);
            utilisateur.setEmail(email);
            utilisateur.setMotDePasse(motDePasse);
            utilisateur.setRole("CLIENT");
            utilisateur.setActif(true);
            
            utilisateurDAO.ajouterUtilisateur(utilisateur);
            
            // Message de succès et redirection
            HttpSession session = request.getSession();
            session.setAttribute("succes", "Inscription réussie ! Veuillez vous connecter.");
            response.sendRedirect(request.getContextPath() + "/login");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur technique: " + e.getMessage());
            request.getRequestDispatcher("/jsp/inscription.jsp").forward(request, response);
        }
    }
}