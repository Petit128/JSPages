package com.parking.controller.admin;

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

@WebServlet("/admin/reservations")
public class AdminReservationsServlet extends HttpServlet {
    
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
        if (!"ADMIN".equals(user.getRole())) {
            resp.sendRedirect("../dashboard");
            return;
        }
        
        // Récupérer tous les types de réservations
        List<Reservation> pendingReservations = parkingService.getPendingReservations();
        List<Reservation> confirmedReservations = parkingService.getConfirmedReservations();
        List<Reservation> completedReservations = parkingService.getCompletedReservations();
        List<Reservation> cancelledReservations = parkingService.getCancelledReservations();
        
        req.setAttribute("pendingReservations", pendingReservations);
        req.setAttribute("confirmedReservations", confirmedReservations);
        req.setAttribute("completedReservations", completedReservations);
        req.setAttribute("cancelledReservations", cancelledReservations);
        req.setAttribute("user", user);
        
        req.getRequestDispatcher("/WEB-INF/admin/reservations.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        
        try {
            int reservationId = Integer.parseInt(req.getParameter("reservationId"));
            
            if ("confirm".equals(action)) {
                parkingService.confirmReservation(reservationId);
                req.getSession().setAttribute("message", "✅ Réservation confirmée !");
                
            } else if ("reject".equals(action)) {
                String motif = req.getParameter("motif");
                if (motif == null || motif.trim().isEmpty()) {
                    motif = "Rejetée par l'administrateur";
                }
                parkingService.cancelReservationByAdmin(reservationId, motif);
                req.getSession().setAttribute("message", "❌ Réservation rejetée");
                
            } else if ("cancel".equals(action)) {
                String motif = req.getParameter("motif");
                if (motif == null || motif.trim().isEmpty()) {
                    motif = "Annulée par l'administrateur";
                }
                parkingService.cancelReservationByAdmin(reservationId, motif);
                req.getSession().setAttribute("message", "🗑️ Réservation annulée");
                
            } else if ("delete".equals(action)) {
                parkingService.deleteReservation(reservationId);
                req.getSession().setAttribute("message", "💀 Réservation supprimée définitivement");
            }
            
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID de réservation invalide");
        }
        
        resp.sendRedirect("reservations");
    }
}