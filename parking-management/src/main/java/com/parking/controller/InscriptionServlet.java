package com.parking.controller;

import com.parking.metier.ParkingService;
import com.parking.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ParkingService parkingService;
    
    @Override
    public void init() throws ServletException {
        parkingService = new ParkingService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        
        request.getRequestDispatcher("/inscription.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String vehiclePlate = request.getParameter("vehiclePlate");
        
        // Validation des champs
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Nom d'utilisateur requis");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Mot de passe requis");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Nom complet requis");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email requis");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        // Vérifier si l'utilisateur existe déjà
        User existingUser = parkingService.getUserByUsername(username);
        if (existingUser != null) {
            request.setAttribute("error", "Ce nom d'utilisateur existe déjà");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        User existingEmail = parkingService.getUserByEmail(email);
        if (existingEmail != null) {
            request.setAttribute("error", "Cet email est déjà utilisé");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
            return;
        }
        
        // Créer le nouvel utilisateur (rôle CLIENT par défaut)
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setRole("CLIENT");
        newUser.setUserType("REGULAR");
        newUser.setVehiclePlate(vehiclePlate);
        
        boolean success = parkingService.registerUser(newUser);
        
        if (success) {
            // Connecter automatiquement l'utilisateur
            HttpSession session = request.getSession();
            session.setAttribute("user", newUser);
            session.setAttribute("role", "CLIENT");
            session.setAttribute("message", "Inscription réussie ! Bienvenue " + fullName);
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Erreur lors de l'inscription");
            request.getRequestDispatcher("/inscription.jsp").forward(request, response);
        }
    }
}