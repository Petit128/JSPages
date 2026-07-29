package com.parking.controller.agent;

import com.parking.metier.ParkingService;
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

@WebServlet("/agent/reservations")
public class AgentReservationsServlet extends HttpServlet {
    
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"AGENT".equals(user.getRole())) {
            resp.sendRedirect("../dashboard");
            return;
        }
        
        List<Reservation> pendingReservations = parkingService.getPendingReservations();
        List<Reservation> confirmedReservations = parkingService.getConfirmedReservations();
        
        req.setAttribute("pendingReservations", pendingReservations);
        req.setAttribute("confirmedReservations", confirmedReservations);
        req.setAttribute("user", user);
        
        req.getRequestDispatcher("/WEB-INF/agent/reservations.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        int reservationId = Integer.parseInt(req.getParameter("reservationId"));
        
        if ("confirm".equals(action)) {
            parkingService.confirmReservation(reservationId);
            req.getSession().setAttribute("message", "✅ Réservation confirmée !");
        } else if ("reject".equals(action)) {
            parkingService.rejectReservation(reservationId);
            req.getSession().setAttribute("message", "❌ Réservation rejetée");
        }
        
        resp.sendRedirect("reservations");
    }
}