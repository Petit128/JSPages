package com.taxis.controller;

import com.taxis.dao.ChauffeurDAO;
import com.taxis.dao.UserDAO;
import com.taxis.model.Chauffeur;
import com.taxis.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet({"/login", "/logout"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private ChauffeurDAO chauffeurDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        chauffeurDAO = new ChauffeurDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            User user = userDAO.authentifier(username, password);
            
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userRole", user.getRole().name());
                session.setAttribute("userName", user.getPrenom() + " " + user.getNom());
                session.setAttribute("userId", user.getId());
                
                System.out.println("Utilisateur connecté: " + username + ", Rôle: " + user.getRole().name());
                
                // Redirection selon le rôle
                switch (user.getRole()) {
                    case ADMIN:
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                        break;
                    case OPERATEUR:
                        response.sendRedirect(request.getContextPath() + "/operateur/dashboard");
                        break;
                    case CHAUFFEUR:
                        // Récupérer les infos du chauffeur
                        Chauffeur chauffeur = chauffeurDAO.getByUserId(user.getId());
                        System.out.println("Chauffeur trouvé: " + (chauffeur != null ? chauffeur.getNom() : "null"));
                        if(chauffeur != null) {
                            session.setAttribute("chauffeur", chauffeur);
                            session.setAttribute("chauffeurId", chauffeur.getId());
                            response.sendRedirect(request.getContextPath() + "/chauffeur/dashboard");
                        } else {
                            // Si pas de chauffeur associé, rediriger vers page d'erreur
                            response.sendRedirect(request.getContextPath() + "/?error=Chauffeur non trouvé");
                        }
                        break;
                    case CLIENT:
                        response.sendRedirect(request.getContextPath() + "/client/dashboard");
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + "/");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/?error=1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/?error=1");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }
}