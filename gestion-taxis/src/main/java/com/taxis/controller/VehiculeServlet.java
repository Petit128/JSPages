// controller/VehiculeServlet.java
package com.taxis.controller;

import com.taxis.dao.VehiculeDAO;
import com.taxis.model.Vehicule;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/vehicules")
public class VehiculeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VehiculeDAO vehiculeDAO;
    
    @Override
    public void init() {
        vehiculeDAO = new VehiculeDAO();
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
                    deleteVehicule(request, response);
                    break;
                default:
                    listVehicules(request, response);
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
                updateVehicule(request, response);
            } else {
                insertVehicule(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listVehicules(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Vehicule> vehicules = vehiculeDAO.getAll();
        request.setAttribute("vehicules", vehicules);
        request.getRequestDispatcher("/views/vehicules.jsp").forward(request, response);
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/vehicule-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Vehicule vehicule = vehiculeDAO.getById(id);
        request.setAttribute("vehicule", vehicule);
        request.getRequestDispatcher("/views/vehicule-form.jsp").forward(request, response);
    }
    
    private void insertVehicule(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Vehicule vehicule = new Vehicule();
        
        vehicule.setImmatriculation(request.getParameter("immatriculation"));
        vehicule.setMarque(request.getParameter("marque"));
        vehicule.setModele(request.getParameter("modele"));
        vehicule.setAnnee(Integer.parseInt(request.getParameter("annee")));
        vehicule.setCouleur(request.getParameter("couleur"));
        vehicule.setNombrePlaces(Integer.parseInt(request.getParameter("nombrePlaces")));
        vehicule.setDisponible("on".equals(request.getParameter("disponible")));
        vehicule.setKilometrage(Double.parseDouble(request.getParameter("kilometrage")));
        vehicule.setEtat(request.getParameter("etat"));
        
        vehiculeDAO.ajouter(vehicule);
        response.sendRedirect("vehicules");
    }
    
    private void updateVehicule(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        Vehicule vehicule = vehiculeDAO.getById(id);
        vehicule.setImmatriculation(request.getParameter("immatriculation"));
        vehicule.setMarque(request.getParameter("marque"));
        vehicule.setModele(request.getParameter("modele"));
        vehicule.setAnnee(Integer.parseInt(request.getParameter("annee")));
        vehicule.setCouleur(request.getParameter("couleur"));
        vehicule.setNombrePlaces(Integer.parseInt(request.getParameter("nombrePlaces")));
        vehicule.setDisponible("on".equals(request.getParameter("disponible")));
        vehicule.setKilometrage(Double.parseDouble(request.getParameter("kilometrage")));
        vehicule.setEtat(request.getParameter("etat"));
        
        vehiculeDAO.modifier(vehicule);
        response.sendRedirect("vehicules");
    }
    
    private void deleteVehicule(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        vehiculeDAO.supprimer(id);
        response.sendRedirect("vehicules");
    }
}