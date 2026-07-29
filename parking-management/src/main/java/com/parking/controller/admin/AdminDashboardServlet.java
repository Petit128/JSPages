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

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
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
        
        int totalSpots = parkingService.getTotalSpots();
        int availableSpots = parkingService.getAvailableSpots();
        int occupiedSpots = totalSpots - availableSpots;
        
        double occupancyRate = totalSpots > 0 ? (double) occupiedSpots / totalSpots * 100 : 0;
        double dailyRevenue = parkingService.calculateDailyRevenue();
        double monthlyRevenue = parkingService.calculateMonthlyRevenue();
        int activeSubscriptions = parkingService.getActiveSubscriptionsCount();
        int todayEntries = parkingService.getTodayEntriesCount();
        int totalUsers = parkingService.getAllUsers().size();
        
        // Récupérer le nombre de réservations en attente
        int pendingReservationsCount = parkingService.getPendingReservations().size();
        
        List<ParkingSpot> allSpots = parkingService.getAllSpots();
        
        request.setAttribute("totalSpots", totalSpots);
        request.setAttribute("availableSpots", availableSpots);
        request.setAttribute("occupiedSpots", occupiedSpots);
        request.setAttribute("occupancyRate", occupancyRate);
        request.setAttribute("dailyRevenue", dailyRevenue);
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("activeSubscriptions", activeSubscriptions);
        request.setAttribute("todayEntries", todayEntries);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("pendingReservationsCount", pendingReservationsCount);
        request.setAttribute("allSpots", allSpots);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
    }
}