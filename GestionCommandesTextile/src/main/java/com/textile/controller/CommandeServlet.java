package com.textile.controller;

import com.textile.metier.GestionProduction;
import com.textile.model.*;
import com.textile.dao.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/commande/*")
public class CommandeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private GestionProduction gestionProduction;
    private CommandeDAO commandeDAO;
    private HistoriqueDAO historiqueDAO;
    
    @Override
    public void init() {
        gestionProduction = new GestionProduction();
        commandeDAO = new CommandeDAO();
        historiqueDAO = new HistoriqueDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        System.out.println("Action demandée: " + action);
        
        try {
            if (action == null || action.equals("/") || action.equals("")) {
                listerCommandes(request, response);
            } else if (action.equals("/nouvelle")) {
                afficherFormulaireNouvelleCommande(request, response);
            } else if (action.equals("/details")) {
                afficherDetailsCommande(request, response);
            } else if (action.equals("/workflow")) {
                afficherWorkflow(request, response);
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
        System.out.println("Action POST: " + action);
        
        try {
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            if (action.equals("/creer")) {
                creerCommande(request, response);
            } else if (action.equals("/demarrerEtape")) {
                demarrerEtape(request, response);
            } else if (action.equals("/terminerEtape")) {
                terminerEtape(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    private void listerCommandes(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Integer utilisateurId = (Integer) session.getAttribute("utilisateurId");
        
        List<Commande> commandes;
        
        // Filtrer selon le rôle
        if ("ADMIN".equals(role) || "RESPONSABLE_PRODUCTION".equals(role) || "OPERATEUR".equals(role)) {
            // Admin et production voient TOUTES les commandes
            commandes = commandeDAO.getAllCommandes();
        } else {
            // Client voit seulement SES commandes
            if (utilisateurId != null) {
                commandes = commandeDAO.getCommandesByUtilisateurId(utilisateurId);
            } else {
                commandes = commandeDAO.getAllCommandes();
            }
        }
        
        request.setAttribute("commandes", commandes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/listeCommandes.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherFormulaireNouvelleCommande(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/nouvelleCommande.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherDetailsCommande(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID manquant");
            return;
        }
        
        int id = Integer.parseInt(idParam);
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Integer utilisateurId = (Integer) session.getAttribute("utilisateurId");
        
        // Vérifier l'accès à la commande
        if (!"ADMIN".equals(role) && !commandeDAO.isCommandeAccessible(id, utilisateurId, role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'avez pas accès à cette commande");
            return;
        }
        
        Commande commande = commandeDAO.getCommandeById(id);
        if (commande == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Commande non trouvée");
            return;
        }
        
        List<EtapeProduction> etapes = gestionProduction.getWorkflowCommande(id);
        Map<String, Object> delais = gestionProduction.calculerDelais(id);
        List<HistoriqueAction> historique = historiqueDAO.getHistoriqueByCommandeId(id);
        
        request.setAttribute("commande", commande);
        request.setAttribute("etapes", etapes);
        request.setAttribute("delais", delais);
        request.setAttribute("historique", historique);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/detailsCommande.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherWorkflow(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        List<EtapeProduction> etapes = gestionProduction.getWorkflowCommande(id);
        
        request.setAttribute("etapes", etapes);
        request.setAttribute("commandeId", id);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/workflow.jsp");
        dispatcher.forward(request, response);
    }
    
    private void creerCommande(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        HttpSession session = request.getSession();
        Integer utilisateurId = (Integer) session.getAttribute("utilisateurId");
        String role = (String) session.getAttribute("role");
        String nomUtilisateur = (String) session.getAttribute("nom");
        
        String client = request.getParameter("client");
        String typeArticle = request.getParameter("typeArticle");
        int quantite = Integer.parseInt(request.getParameter("quantite"));
        String taille = request.getParameter("taille");
        String couleur = request.getParameter("couleur");
        String dateLivraisonStr = request.getParameter("dateLivraison");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateLivraison = sdf.parse(dateLivraisonStr);
        
        // Pour un client, le nom du client = son propre nom
        if ("CLIENT".equals(role)) {
            client = nomUtilisateur;
        }
        
        gestionProduction.creerCommande(utilisateurId, client, typeArticle, quantite, 
                                        taille, couleur, dateLivraison);
        
        response.sendRedirect(request.getContextPath() + "/commande/");
    }
    
    private void demarrerEtape(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        int commandeId = Integer.parseInt(request.getParameter("commandeId"));
        String nomEtape = request.getParameter("nomEtape");
        
        try {
            gestionProduction.demarrerEtape(commandeId, nomEtape);
            request.getSession().setAttribute("succes", "L'étape " + nomEtape + " a été démarrée");
        } catch (IllegalStateException e) {
            request.getSession().setAttribute("erreur", e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/commande/details?id=" + commandeId);
    }
    
    private void terminerEtape(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        int commandeId = Integer.parseInt(request.getParameter("commandeId"));
        String nomEtape = request.getParameter("nomEtape");
        String commentaire = request.getParameter("commentaire");
        
        gestionProduction.terminerEtape(commandeId, nomEtape, commentaire);
        request.getSession().setAttribute("succes", "L'étape " + nomEtape + " a été terminée");
        
        response.sendRedirect(request.getContextPath() + "/commande/details?id=" + commandeId);
    }
}