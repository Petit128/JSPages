package com.essai.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.essai.model.User;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Base de données simulée (static pour être partagée entre les servlets)
    public static Map<String, User> users = new HashMap<>();
    
    static {
        // Ajouter quelques utilisateurs par défaut
        users.put("admin", new User("admin", "admin123", "admin@example.com"));
        users.put("user", new User("user", "password", "user@example.com"));
        System.out.println("Utilisateurs par défaut ajoutés: " + users.size());
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("Tentative de connexion - Username: " + username);
        System.out.println("Utilisateurs disponibles: " + users.keySet());
        
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.getPassword().equals(password)) {
                // Connexion réussie
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("username", username);
                
                System.out.println("Connexion réussie pour: " + username);
                response.sendRedirect("accueil.html");
                return;
            } else {
                System.out.println("Mot de passe incorrect pour: " + username);
            }
        } else {
            System.out.println("Utilisateur non trouvé: " + username);
        }
        
        // Connexion échouée
        response.sendRedirect("login.html?error=1");
    }
}