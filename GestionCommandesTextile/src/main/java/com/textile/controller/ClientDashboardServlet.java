package com.textile.controller;

import com.textile.dao.*;
import com.textile.model.*;
import com.textile.metier.GestionProduction;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/client/*")
public class ClientDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CommandeDAO commandeDAO;
    private GestionProduction gestionProduction;
    private NotificationDAO notificationDAO;
    
    @Override
    public void init() {
        commandeDAO = new CommandeDAO();
        gestionProduction = new GestionProduction();
        notificationDAO = new NotificationDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Vérifier si l'utilisateur est connecté
        HttpSession session = request.getSession();
        if (session.getAttribute("utilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getPathInfo();
        
        try {
            if (action == null || action.equals("/") || action.equals("/dashboard")) {
                afficherDashboard(request, response);
            } else if (action.equals("/commandes")) {
                listerCommandesClient(request, response);
            } else if (action.equals("/commande/details")) {
                afficherDetailsCommande(request, response);
            } else if (action.equals("/notifications")) {
                afficherNotifications(request, response);
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
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        
        // Récupérer les commandes du client
        // Note: Dans une vraie application, il faudrait lier les commandes à l'utilisateur
        List<Commande> commandes = commandeDAO.getAllCommandes();
        request.setAttribute("commandes", commandes);
        
        // Récupérer les notifications non lues
        int nbNotifications = notificationDAO.getNombreNonLues(email);
        request.setAttribute("nbNotifications", nbNotifications);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/client/dashboard.jsp");
        dispatcher.forward(request, response);
    }
    
    private void listerCommandesClient(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        HttpSession session = request.getSession();
        int utilisateurId = (int) session.getAttribute("utilisateurId");
        
        // Récupérer UNIQUEMENT les commandes de cet utilisateur
        List<Commande> commandes = commandeDAO.getCommandesByUtilisateurId(utilisateurId);
        request.setAttribute("commandes", commandes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/client/mesCommandes.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherDetailsCommande(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Commande commande = commandeDAO.getCommandeById(id);
        List<EtapeProduction> etapes = gestionProduction.getWorkflowCommande(id);
        Map<String, Object> delais = gestionProduction.calculerDelais(id);
        
        request.setAttribute("commande", commande);
        request.setAttribute("etapes", etapes);
        request.setAttribute("delais", delais);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/client/detailsCommande.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherNotifications(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        
        List<Notification> notifications = notificationDAO.getNotificationsByDestinataire(email);
        request.setAttribute("notifications", notifications);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/client/notifications.jsp");
        dispatcher.forward(request, response);
    }
}