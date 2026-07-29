package com.textile.controller;

import com.textile.dao.UtilisateurDAO;
import com.textile.dao.ParametreDAO;
import com.textile.model.Utilisateur;
import com.textile.metier.GestionProduction;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtilisateurDAO utilisateurDAO;
    private ParametreDAO parametreDAO;
    private GestionProduction gestionProduction;
    
    @Override
    public void init() {
        utilisateurDAO = new UtilisateurDAO();
        parametreDAO = new ParametreDAO();
        gestionProduction = new GestionProduction();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Vérifier si l'utilisateur est admin
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/") || action.equals("/dashboard")) {
                afficherDashboard(request, response);
            } else if (action.equals("/utilisateurs")) {
                listerUtilisateurs(request, response);
            } else if (action.equals("/utilisateur/nouveau")) {
                afficherFormulaireUtilisateur(request, response);
            } else if (action.equals("/utilisateur/modifier")) {
                afficherModificationUtilisateur(request, response);
            } else if (action.equals("/utilisateur/supprimer")) {
                supprimerUtilisateur(request, response);
            } else if (action.equals("/parametres")) {
                afficherParametres(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            if (action.equals("/utilisateur/creer")) {
                creerUtilisateur(request, response);
            } else if (action.equals("/utilisateur/update")) {
                updateUtilisateur(request, response);
            } else if (action.equals("/parametres/update")) {
                updateParametres(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    private void afficherDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        Map<String, Object> dashboard = gestionProduction.getTableauDeBord();
        request.setAttribute("dashboard", dashboard);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/admin/dashboard.jsp");
        dispatcher.forward(request, response);
    }
    
    private void listerUtilisateurs(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        List<Utilisateur> utilisateurs = utilisateurDAO.getAllUtilisateurs();
        request.setAttribute("utilisateurs", utilisateurs);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/admin/listeUtilisateurs.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherFormulaireUtilisateur(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/admin/nouvelUtilisateur.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherModificationUtilisateur(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurById(id);
        request.setAttribute("utilisateur", utilisateur);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/admin/modifierUtilisateur.jsp");
        dispatcher.forward(request, response);
    }
    
    private void creerUtilisateur(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getParameter("nom"));
        utilisateur.setEmail(request.getParameter("email"));
        utilisateur.setMotDePasse(request.getParameter("motDePasse"));
        utilisateur.setRole(request.getParameter("role"));
        utilisateur.setActif(true);
        
        utilisateurDAO.ajouterUtilisateur(utilisateur);
        
        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
    }
    
    private void updateUtilisateur(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurById(Integer.parseInt(request.getParameter("id")));
        utilisateur.setNom(request.getParameter("nom"));
        utilisateur.setEmail(request.getParameter("email"));
        utilisateur.setRole(request.getParameter("role"));
        utilisateur.setActif(Boolean.parseBoolean(request.getParameter("actif")));
        
        utilisateurDAO.updateUtilisateur(utilisateur);
        
        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
    }
    
    private void supprimerUtilisateur(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        utilisateurDAO.deleteUtilisateur(id);
        
        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
    }
    
    private void afficherParametres(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        Map<String, String> parametres = parametreDAO.getAllParametres();
        request.setAttribute("parametres", parametres);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/admin/parametres.jsp");
        dispatcher.forward(request, response);
    }
    
 // Ajoutez cette méthode dans AdminServlet.java
    private void updateParametres(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String dureeCoupe = request.getParameter("duree_coupe_jours");
        String dureeCouture = request.getParameter("duree_couture_jours");
        String dureeFinition = request.getParameter("duree_finition_jours");
        String dureeLivraison = request.getParameter("duree_livraison_jours");
        String seuilAlerte = request.getParameter("seuil_alerte_retard");
        String notificationEmail = request.getParameter("notification_email");
        
        if (dureeCoupe != null) {
            parametreDAO.updateValeur("duree_coupe_jours", dureeCoupe);
        }
        if (dureeCouture != null) {
            parametreDAO.updateValeur("duree_couture_jours", dureeCouture);
        }
        if (dureeFinition != null) {
            parametreDAO.updateValeur("duree_finition_jours", dureeFinition);
        }
        if (dureeLivraison != null) {
            parametreDAO.updateValeur("duree_livraison_jours", dureeLivraison);
        }
        if (seuilAlerte != null) {
            parametreDAO.updateValeur("seuil_alerte_retard", seuilAlerte);
        }
        if (notificationEmail != null) {
            parametreDAO.updateValeur("notification_email", notificationEmail);
        }
        
        request.getSession().setAttribute("succes", "Paramètres mis à jour avec succès !");
        response.sendRedirect(request.getContextPath() + "/admin/parametres");
    }
}