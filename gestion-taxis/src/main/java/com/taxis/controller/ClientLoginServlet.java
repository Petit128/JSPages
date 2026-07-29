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

@WebServlet("/client/login")
public class ClientLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;
    
    @Override
    public void init() {
        clientDAO = new ClientDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            Client client = clientDAO.authentifier(email, password);
            
            if (client != null) {
                HttpSession session = request.getSession();
                session.setAttribute("client", client);
                session.setAttribute("clientId", client.getId());
                session.setAttribute("clientNom", client.getNomComplet());
                session.setAttribute("userRole", "CLIENT");
                
                response.sendRedirect(request.getContextPath() + "/client/dashboard");
            } else {
                request.setAttribute("error", "Email ou mot de passe incorrect");
                request.getRequestDispatcher("/views/client/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur de connexion");
            request.getRequestDispatcher("/views/client/login.jsp").forward(request, response);
        }
    }
}