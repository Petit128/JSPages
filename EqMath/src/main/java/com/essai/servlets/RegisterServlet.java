package com.essai.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.essai.model.User;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        System.out.println("Tentative d'inscription - Username: " + username);
        System.out.println("Email: " + email);
        
        // Vérifier si l'utilisateur existe déjà
        if (LoginServlet.users.containsKey(username)) {
            System.out.println("Erreur: L'utilisateur existe déjà");
            response.sendRedirect("register.html?error=exists");
            return;
        }
        
        // Vérifier que les mots de passe correspondent
        if (!password.equals(confirmPassword)) {
            System.out.println("Erreur: Les mots de passe ne correspondent pas");
            response.sendRedirect("register.html?error=password");
            return;
        }
        
        // Vérifier la longueur du mot de passe
        if (password.length() < 6) {
            System.out.println("Erreur: Mot de passe trop court");
            response.sendRedirect("register.html?error=short");
            return;
        }
        
        // Créer le nouvel utilisateur
        User newUser = new User(username, password, email);
        LoginServlet.users.put(username, newUser);
        
        System.out.println("Inscription réussie pour: " + username);
        System.out.println("Total utilisateurs: " + LoginServlet.users.size());
        
        // Rediriger vers la page de connexion
        response.sendRedirect("login.html?registered=1");
    }
}