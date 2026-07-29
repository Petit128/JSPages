package com.parking.controller.admin;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingSpot;
import com.parking.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/spots")
public class AdminSpotsServlet extends HttpServlet {
    
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
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            response.sendRedirect("../dashboard");
            return;
        }
        
        List<ParkingSpot> allSpots = parkingService.getAllSpots();
        request.setAttribute("allSpots", allSpots);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/admin/spots.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addSpot(request, response);
        } else if ("update".equals(action)) {
            updateSpot(request, response);
        } else if ("delete".equals(action)) {
            deleteSpot(request, response);
        }
        
        response.sendRedirect("spots");
    }
    
    private void addSpot(HttpServletRequest request, HttpServletResponse response) {
        try {
            String spotNumber = request.getParameter("spotNumber");
            String spotType = request.getParameter("spotType");
            String location = request.getParameter("location");
            double hourlyRate = Double.parseDouble(request.getParameter("hourlyRate"));
            
            ParkingSpot spot = new ParkingSpot(spotNumber, spotType, location, hourlyRate);
            boolean success = parkingService.addSpot(spot);
            
            if (success) {
                request.getSession().setAttribute("message", "Spot added successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to add spot");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid input");
        }
    }
    
    private void updateSpot(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String spotNumber = request.getParameter("spotNumber");
            String spotType = request.getParameter("spotType");
            String location = request.getParameter("location");
            double hourlyRate = Double.parseDouble(request.getParameter("hourlyRate"));
            
            ParkingSpot spot = new ParkingSpot(spotNumber, spotType, location, hourlyRate);
            spot.setId(id);
            boolean success = parkingService.updateSpot(spot);
            
            if (success) {
                request.getSession().setAttribute("message", "Spot updated successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to update spot");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid input");
        }
    }
    
    private void deleteSpot(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = parkingService.deleteSpot(id);
            
            if (success) {
                request.getSession().setAttribute("message", "Spot deleted successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to delete spot");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid spot ID");
        }
    }
}