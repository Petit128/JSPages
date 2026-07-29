package com.parking.controller;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingEntry;
import com.parking.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ParkingService parkingService;
    
    @Override
    public void init() {
        parkingService = new ParkingService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Vérifier si l'utilisateur est connecté
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Rediriger selon le rôle
        if ("ADMIN".equals(user.getRole())) {
            response.sendRedirect("admin/dashboard");
            return;
        } else if ("AGENT".equals(user.getRole())) {
            response.sendRedirect("agent/dashboard");
            return;
        }
        
        // CLIENT : afficher le dashboard client
        int availableSpots = parkingService.getAvailableSpots();
        List<ParkingEntry> currentOccupancy = parkingService.getCurrentOccupancy();
        List<ParkingEntry> userHistory = parkingService.getUserEntries(user.getId());
        double dailyRevenue = parkingService.calculateDailyRevenue();
        
        request.setAttribute("availableSpots", availableSpots);
        request.setAttribute("currentOccupancy", currentOccupancy);
        request.setAttribute("dailyRevenue", dailyRevenue);
        request.setAttribute("userHistory", userHistory);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}