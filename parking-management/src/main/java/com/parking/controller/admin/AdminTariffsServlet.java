package com.parking.controller.admin;

import com.parking.metier.ParkingService;
import com.parking.model.Tariff;
import com.parking.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/tariffs")
public class AdminTariffsServlet extends HttpServlet {
    
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
        
        List<Tariff> allTariffs = parkingService.getAllTariffs();
        request.setAttribute("allTariffs", allTariffs);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/admin/tariffs.jsp").forward(request, response);
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
        
        if ("update".equals(action)) {
            updateTariff(request, response);
        }
        
        response.sendRedirect("tariffs");
    }
    
    private void updateTariff(HttpServletRequest request, HttpServletResponse response) {
        try {
            String tariffType = request.getParameter("tariffType");
            double hourlyRate = Double.parseDouble(request.getParameter("hourlyRate"));
            double dailyRate = Double.parseDouble(request.getParameter("dailyRate"));
            double weeklyRate = Double.parseDouble(request.getParameter("weeklyRate"));
            double monthlyRate = Double.parseDouble(request.getParameter("monthlyRate"));
            double vipDiscount = Double.parseDouble(request.getParameter("vipDiscount"));
            double subscriberDiscount = Double.parseDouble(request.getParameter("subscriberDiscount"));
            
            Tariff tariff = parkingService.getTariffByType(tariffType);
            if (tariff != null) {
                tariff.setHourlyRate(hourlyRate);
                tariff.setDailyRate(dailyRate);
                tariff.setWeeklyRate(weeklyRate);
                tariff.setMonthlyRate(monthlyRate);
                tariff.setVipDiscount(vipDiscount);
                tariff.setSubscriberDiscount(subscriberDiscount);
                
                boolean success = parkingService.updateTariff(tariff);
                
                if (success) {
                    request.getSession().setAttribute("message", "Tariff updated successfully");
                } else {
                    request.getSession().setAttribute("error", "Failed to update tariff");
                }
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid input: " + e.getMessage());
        }
    }
}