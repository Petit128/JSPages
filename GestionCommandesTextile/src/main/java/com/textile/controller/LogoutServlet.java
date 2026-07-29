package com.textile.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String role = (String) session.getAttribute("role");
            String nom = (String) session.getAttribute("nom");
            System.out.println("Déconnexion: " + nom + " (" + role + ")");
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/index.jsp?logout=success");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}