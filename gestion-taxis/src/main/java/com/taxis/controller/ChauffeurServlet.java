package com.taxis.controller;

import com.taxis.dao.ChauffeurDAO;
import com.taxis.model.Chauffeur;
import com.taxis.model.StatutChauffeur;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/chauffeurs")
public class ChauffeurServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ChauffeurDAO chauffeurDAO;
    
    @Override
    public void init() {
        chauffeurDAO = new ChauffeurDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteChauffeur(request, response);
                    break;
                case "changerStatut":
                    changerStatut(request, response);
                    break;
                default:
                    listChauffeurs(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("update".equals(action)) {
                updateChauffeur(request, response);
            } else {
                insertChauffeur(request, response);
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listChauffeurs(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Chauffeur> chauffeurs = chauffeurDAO.getAll();
        request.setAttribute("chauffeurs", chauffeurs);
        request.getRequestDispatcher("/views/chauffeurs.jsp").forward(request, response);
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/chauffeur-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Chauffeur chauffeur = chauffeurDAO.getById(id);
        request.setAttribute("chauffeur", chauffeur);
        request.getRequestDispatcher("/views/chauffeur-form.jsp").forward(request, response);
    }
    
    private void insertChauffeur(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        Chauffeur chauffeur = new Chauffeur();
        
        chauffeur.setNom(request.getParameter("nom"));
        chauffeur.setPrenom(request.getParameter("prenom"));
        chauffeur.setPermis(request.getParameter("permis"));
        chauffeur.setTelephone(request.getParameter("telephone"));
        chauffeur.setEmail(request.getParameter("email"));
        
        String disponible = request.getParameter("disponible");
        chauffeur.setStatut(disponible != null ? StatutChauffeur.DISPONIBLE : StatutChauffeur.INDISPONIBLE);
        
        String dateEmbaucheStr = request.getParameter("dateEmbauche");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        chauffeur.setDateEmbauche(sdf.parse(dateEmbaucheStr));
        
        chauffeur.setRevenuTotal(0.0);
        chauffeur.setNombreCourses(0);
        chauffeur.setEvaluation(5.0);
        chauffeur.setLatitude(48.8566); // Paris par défaut
        chauffeur.setLongitude(2.3522);
        
        chauffeurDAO.ajouter(chauffeur);
        response.sendRedirect("chauffeurs");
    }
    
    private void updateChauffeur(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        Chauffeur chauffeur = chauffeurDAO.getById(id);
        chauffeur.setNom(request.getParameter("nom"));
        chauffeur.setPrenom(request.getParameter("prenom"));
        chauffeur.setPermis(request.getParameter("permis"));
        chauffeur.setTelephone(request.getParameter("telephone"));
        chauffeur.setEmail(request.getParameter("email"));
        
        String disponible = request.getParameter("disponible");
        chauffeur.setStatut(disponible != null ? StatutChauffeur.DISPONIBLE : StatutChauffeur.INDISPONIBLE);
        
        chauffeurDAO.modifier(chauffeur);
        response.sendRedirect("chauffeurs");
    }
    
    private void deleteChauffeur(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        chauffeurDAO.supprimer(id);
        response.sendRedirect("chauffeurs");
    }
    
    private void changerStatut(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nouveauStatut = request.getParameter("statut");
        
        Chauffeur chauffeur = chauffeurDAO.getById(id);
        if (chauffeur != null) {
            switch (nouveauStatut) {
                case "disponible":
                    chauffeur.setStatut(StatutChauffeur.DISPONIBLE);
                    break;
                case "en_course":
                    chauffeur.setStatut(StatutChauffeur.EN_COURSE);
                    break;
                case "en_pause":
                    chauffeur.setStatut(StatutChauffeur.EN_PAUSE);
                    break;
                case "indisponible":
                    chauffeur.setStatut(StatutChauffeur.INDISPONIBLE);
                    break;
            }
            chauffeurDAO.modifier(chauffeur);
        }
        response.sendRedirect("chauffeurs");
    }
}