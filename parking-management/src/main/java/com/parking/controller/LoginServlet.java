package com.parking.controller;

import com.parking.model.User;
import com.parking.metier.ParkingService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
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
        
        // Si déjà connecté, rediriger vers le dashboard
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect("admin/dashboard");
            } else if ("AGENT".equals(user.getRole())) {
                response.sendRedirect("agent/dashboard");
            } else {
                response.sendRedirect("dashboard");
            }
            return;
        }
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Veuillez saisir nom d'utilisateur et mot de passe");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        User user = parkingService.authenticateUser(username, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            
            // Rediriger selon le rôle
            switch(user.getRole()) {
                case "ADMIN":
                    response.sendRedirect("admin/dashboard");
                    break;
                case "AGENT":
                    response.sendRedirect("agent/dashboard");
                    break;
                default:
                    response.sendRedirect("dashboard");
                    break;
            }
        } else {
            request.setAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}