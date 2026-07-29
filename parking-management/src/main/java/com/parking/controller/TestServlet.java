package com.parking.controller;

import com.parking.dao.DatabaseConnection;
import com.parking.dao.UserDAO;
import com.parking.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Parking System Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 20px; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h1>Parking Management System - Test Page</h1>");
        
        // Test 1: Database Connection
        out.println("<h2>1. Database Connection Test</h2>");
        try {
            Connection conn = DatabaseConnection.getConnection();
            out.println("<p class='success'>✓ Database connection successful!</p>");
            conn.close();
        } catch (Exception e) {
            out.println("<p class='error'>✗ Database connection failed: " + e.getMessage() + "</p>");
        }
        
        // Test 2: Users List
        out.println("<h2>2. Users List</h2>");
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();
            
            if (users.isEmpty()) {
                out.println("<p class='error'>No users found in database</p>");
            } else {
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Username</th><th>Full Name</th><th>Role</th><th>User Type</th></tr>");
                for (User u : users) {
                    out.println("<tr>");
                    out.println("<td>" + u.getId() + "</td>");
                    out.println("<td>" + u.getUsername() + "</td>");
                    out.println("<td>" + u.getFullName() + "</td>");
                    out.println("<td>" + u.getRole() + "</td>");
                    out.println("<td>" + u.getUserType() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
        } catch (Exception e) {
            out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
        }
        
        // Test 3: Login Test
        out.println("<h2>3. Login Test</h2>");
        out.println("<p>Use these credentials to login:</p>");
        out.println("<ul>");
        out.println("<li><strong>Admin:</strong> admin / admin123</li>");
        out.println("<li><strong>Agent:</strong> agent1 / agent123</li>");
        out.println("<li><strong>Client VIP:</strong> jane_smith / password456</li>");
        out.println("<li><strong>Client Regular:</strong> john_doe / password123</li>");
        out.println("</ul>");
        
        out.println("<p><a href='login'>Go to Login Page</a></p>");
        out.println("</body></html>");
    }
}