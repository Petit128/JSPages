package com.parking.controller;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingSpot;
import com.parking.model.Subscription;
import com.parking.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/subscription")
public class SubscriptionServlet extends HttpServlet {
    
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
        
        User user = (User) session.getAttribute("user");
        
        Subscription subscription = parkingService.getUserSubscription(user.getId());
        List<ParkingSpot> availableSpots = parkingService.getAvailableSpotsList();
        
        request.setAttribute("subscription", subscription);
        request.setAttribute("availableSpots", availableSpots);
        
        request.getRequestDispatcher("/subscription.jsp").forward(request, response);
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
        
        if ("create".equals(action)) {
            createSubscription(request, response, user);
        } else if ("cancel".equals(action)) {
            cancelSubscription(request, response);
        }
        
        response.sendRedirect("subscription");
    }
    
    private void createSubscription(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            int spotId = Integer.parseInt(request.getParameter("spotId"));
            String subscriptionType = request.getParameter("subscriptionType");
            
            boolean success = parkingService.createSubscription(user.getId(), spotId, subscriptionType);
            
            if (success) {
                request.getSession().setAttribute("message", "Subscription created successfully!");
            } else {
                request.getSession().setAttribute("error", "Failed to create subscription");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid input");
        }
    }
    
    private void cancelSubscription(HttpServletRequest request, HttpServletResponse response) {
        try {
            int subscriptionId = Integer.parseInt(request.getParameter("subscriptionId"));
            boolean success = parkingService.cancelSubscription(subscriptionId);
            
            if (success) {
                request.getSession().setAttribute("message", "Subscription cancelled successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to cancel subscription");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid subscription ID");
        }
    }
}