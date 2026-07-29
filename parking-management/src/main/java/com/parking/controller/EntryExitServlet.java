package com.parking.controller;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingEntry;
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

@WebServlet("/entry-exit")
public class EntryExitServlet extends HttpServlet {
    
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
            response.sendRedirect("login");
            return;
        }
        
        List<ParkingEntry> activeEntries = parkingService.getCurrentOccupancy();
        List<ParkingSpot> availableSpots = parkingService.getAvailableSpotsList();
        
        request.setAttribute("activeEntries", activeEntries);
        request.setAttribute("availableSpots", availableSpots);
        
        request.getRequestDispatcher("/entry-exit.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        
        if ("entry".equals(action)) {
            recordEntry(request, response, user);
        } else if ("exit".equals(action)) {
            recordExit(request, response);
        }
        
        response.sendRedirect("entry-exit");
    }
    
    private void recordEntry(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            int spotId = Integer.parseInt(request.getParameter("spotId"));
            String vehiclePlate = request.getParameter("vehiclePlate");
            
            ParkingEntry entry = parkingService.recordEntry(user.getId(), spotId, vehiclePlate);
            
            if (entry != null) {
                request.getSession().setAttribute("message", "Entry recorded successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to record entry. Spot may not be available.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid spot selection");
        }
    }
    
    private void recordExit(HttpServletRequest request, HttpServletResponse response) {
        try {
            int entryId = Integer.parseInt(request.getParameter("entryId"));
            ParkingEntry entry = parkingService.recordExit(entryId);
            
            if (entry != null) {
                request.getSession().setAttribute("message", "Exit recorded. Amount paid: $" + entry.getAmountPaid());
            } else {
                request.getSession().setAttribute("error", "Failed to record exit");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid entry selection");
        }
    }
}