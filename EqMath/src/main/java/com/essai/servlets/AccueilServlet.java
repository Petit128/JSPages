package com.essai.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.essai.model.User;

public class AccueilServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Accueil</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 50px; }");
        out.println(".welcome { color: green; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2 class='welcome'>Bienvenue " + user.getUsername() + " !</h2>");
        out.println("<p>Email: " + user.getEmail() + "</p>");
        out.println("<br><a href='logout'>Déconnexion</a>");
        out.println("<br><a href='index.html'>Retour au menu</a>");
        out.println("</body>");
        out.println("</html>");
    }
}