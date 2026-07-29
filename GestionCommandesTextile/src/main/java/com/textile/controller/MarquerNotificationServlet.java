package com.textile.controller;

import com.textile.dao.NotificationDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/api/marquerNotificationLue")
public class MarquerNotificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NotificationDAO notificationDAO;
    
    @Override
    public void init() {
        notificationDAO = new NotificationDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                notificationDAO.marquerCommeLue(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Rediriger vers la page des notifications
        response.sendRedirect(request.getContextPath() + "/client/notifications");
    }
}