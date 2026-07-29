package com.taxis.controller;

import com.taxis.dao.ClientDAO;
import com.taxis.model.Client;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;
    
    @Override
    public void init() {
        clientDAO = new ClientDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/client/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String adresse = request.getParameter("adresse");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas");
            request.getRequestDispatcher("/views/client/register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Vérifier si l'email existe déjà
            Client existing = clientDAO.getByEmail(email);
            if (existing != null) {
                request.setAttribute("error", "Cet email est déjà utilisé");
                request.getRequestDispatcher("/views/client/register.jsp").forward(request, response);
                return;
            }
            
            Client client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);
            client.setTelephone(telephone);
            client.setAdresse(adresse);
            client.setPassword(password); // À encoder avec BCrypt en production
            
            clientDAO.ajouter(client);
            
            // Auto-login après inscription
            HttpSession session = request.getSession();
            session.setAttribute("client", client);
            session.setAttribute("clientId", client.getId());
            session.setAttribute("clientNom", client.getNomComplet());
            session.setAttribute("userRole", "CLIENT");
            
            response.sendRedirect(request.getContextPath() + "/client/dashboard");
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de l'inscription");
            request.getRequestDispatcher("/views/client/register.jsp").forward(request, response);
        }
    }
}