package com.parking.controller.agent;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingEntry;
import com.parking.model.ParkingSpot;
import com.parking.model.Reservation;
import com.parking.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/agent/dashboard")
public class AgentDashboardServlet extends HttpServlet {
    
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
        if (!"AGENT".equals(user.getRole())) {
            response.sendRedirect("../dashboard");
            return;
        }
        
        List<ParkingEntry> activeEntries = parkingService.getCurrentOccupancy();
        List<ParkingSpot> availableSpots = parkingService.getAvailableSpotsList();
        List<Reservation> todayReservations = parkingService.getTodayReservations();
        int availableCount = parkingService.getAvailableSpots();
        int occupiedCount = parkingService.getOccupiedSpots();
        
        // Récupérer le nombre de réservations en attente
        int pendingReservationsCount = parkingService.getPendingReservations().size();
        
        request.setAttribute("activeEntries", activeEntries);
        request.setAttribute("availableSpots", availableSpots);
        request.setAttribute("todayReservations", todayReservations);
        request.setAttribute("availableCount", availableCount);
        request.setAttribute("occupiedCount", occupiedCount);
        request.setAttribute("pendingReservationsCount", pendingReservationsCount);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/agent/dashboard.jsp").forward(request, response);
    }
}