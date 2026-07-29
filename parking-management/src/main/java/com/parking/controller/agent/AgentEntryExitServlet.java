package com.parking.controller.agent;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingEntry;
import com.parking.model.ParkingSpot;
import com.parking.model.Reservation;
import com.parking.model.User;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/agent/entry-exit")
public class AgentEntryExitServlet extends HttpServlet {
    
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
        
        List<ParkingEntry> activeEntries = parkingService.getCurrentOccupancy();
        List<ParkingSpot> availableSpots = parkingService.getAvailableSpotsList();
        List<Reservation> todayReservations = parkingService.getTodayReservations();
        
        req.setAttribute("activeEntries", activeEntries);
        req.setAttribute("availableSpots", availableSpots);
        req.setAttribute("todayReservations", todayReservations);
        req.setAttribute("user", user);
        
        req.getRequestDispatcher("/WEB-INF/agent/entry-exit.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        
        if ("entry".equals(action)) {
            recordEntry(req, resp);
        } else if ("exit".equals(action)) {
            recordExit(req, resp);
        } else if ("entryByReservation".equals(action)) {
            entryByReservation(req, resp);
        }
        
        resp.sendRedirect("entry-exit");
    }
    
    private void recordEntry(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String vehiclePlate = req.getParameter("vehiclePlate");
            int spotId = Integer.parseInt(req.getParameter("spotId"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            
            ParkingEntry entry = parkingService.recordEntry(userId, spotId, vehiclePlate);
            
            if (entry != null) {
                req.getSession().setAttribute("message", "✅ Entrée enregistrée pour " + vehiclePlate);
            } else {
                req.getSession().setAttribute("error", "❌ Place non disponible");
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Données invalides");
        }
    }
    
    private void entryByReservation(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String reservationCode = req.getParameter("reservationCode");
            
            // Vérifier si la réservation existe et est confirmée
            Reservation reservation = parkingService.getReservationByCode(reservationCode);
            
            if (reservation == null) {
                req.getSession().setAttribute("error", "❌ Code réservation invalide");
                return;
            }
            
            if (!"CONFIRMEE".equals(reservation.getStatus())) {
                req.getSession().setAttribute("error", "❌ Réservation non confirmée ou expirée");
                return;
            }
            
            // Vérifier si l'heure actuelle est dans la plage de réservation
            Date now = new Date();
            if (now.before(reservation.getStartTime()) || now.after(reservation.getEndTime())) {
                req.getSession().setAttribute("error", "❌ La réservation n'est pas valide à cette heure");
                return;
            }
            
            // Enregistrer l'entrée
            ParkingEntry entry = parkingService.recordEntry(
                reservation.getUserId(), 
                reservation.getSpotId(), 
                reservation.getVehiclePlate()
            );
            
            if (entry != null) {
                // Marquer la réservation comme terminée
                parkingService.completeReservation(reservation.getId());
                req.getSession().setAttribute("message", "✅ Entrée enregistrée pour réservation " + reservationCode);
            } else {
                req.getSession().setAttribute("error", "❌ La place est déjà occupée");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Erreur: " + e.getMessage());
        }
    }
    
    private void recordExit(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int entryId = Integer.parseInt(req.getParameter("entryId"));
            ParkingEntry entry = parkingService.recordExit(entryId);
            
            if (entry != null) {
                req.getSession().setAttribute("message", "✅ Sortie enregistrée. Montant: " + entry.getAmountPaid() + " Ar");
            } else {
                req.getSession().setAttribute("error", "Erreur lors de l'enregistrement");
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "ID invalide");
        }
    }
}