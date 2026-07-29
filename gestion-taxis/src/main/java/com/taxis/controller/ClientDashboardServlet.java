package com.taxis.controller;

import com.taxis.dao.ClientDAO;
import com.taxis.dao.CourseDAO;
import com.taxis.model.Client;
import com.taxis.model.Course;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/client/dashboard")
public class ClientDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourseDAO courseDAO;
    private ClientDAO clientDAO;
    
    @Override
    public void init() {
        courseDAO = new CourseDAO();
        clientDAO = new ClientDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("client");
        
        if (client == null) {
            response.sendRedirect(request.getContextPath() + "/client/login");
            return;
        }
        
        try {
            List<Course> courses = courseDAO.getByClientId(client.getId());
            request.setAttribute("client", client);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/views/client/client-dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}