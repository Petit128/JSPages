package com.parking.controller;

import com.parking.metier.ParkingService;
import com.parking.model.ParkingSpot;
import com.parking.model.Reservation;
import com.parking.model.User;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ParkingService parkingService;
    private SimpleDateFormat dateFormat;
    
    @Override
    public void init() throws ServletException {
        parkingService = new ParkingService();
        setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"));
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
        
        List<ParkingSpot> availableSpots = parkingService.getAvailableSpotsList();
        List<Reservation> userReservations = parkingService.getUserReservations(user.getId());
        
        request.setAttribute("availableSpots", availableSpots);
        request.setAttribute("userReservations", userReservations);
        
        request.getRequestDispatcher("/reservation.jsp").forward(request, response);
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
            createReservation(request, response, user);
        } else if ("cancel".equals(action)) {
            cancelReservation(request, response);
        }
        
        response.sendRedirect("reservation");
    }
    
    private void createReservation(HttpServletRequest request, HttpServletResponse response, User user) {
        try {
            int spotId = Integer.parseInt(request.getParameter("spotId"));
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            String vehiclePlate = request.getParameter("vehiclePlate");
            
            // Gérer les deux formats de date
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            Date startTime = null;
            Date endTime = null;
            
            try {
                startTime = dateFormat1.parse(startTimeStr);
            } catch (ParseException e) {
                startTime = dateFormat2.parse(startTimeStr);
            }
            
            try {
                endTime = dateFormat1.parse(endTimeStr);
            } catch (ParseException e) {
                endTime = dateFormat2.parse(endTimeStr);
            }
            
            // Créer la réservation avec statut EN_ATTENTE
            boolean success = parkingService.createPendingReservation(user.getId(), spotId, startTime, endTime, vehiclePlate);
            
            if (success) {
                request.getSession().setAttribute("message", "✅ Réservation créée ! En attente de validation par un agent.");
            } else {
                request.getSession().setAttribute("error", "❌ Place non disponible pour cette période");
            }
        } catch (ParseException | NumberFormatException e) {
            request.getSession().setAttribute("error", "Format de date invalide. Utilisez yyyy-MM-dd HH:mm");
        }
    }
    
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response) {
        try {
            int reservationId = Integer.parseInt(request.getParameter("reservationId"));
            boolean success = parkingService.cancelReservation(reservationId);
            
            if (success) {
                request.getSession().setAttribute("message", "Réservation annulée");
            } else {
                request.getSession().setAttribute("error", "Échec de l'annulation");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID invalide");
        }
    }

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
}