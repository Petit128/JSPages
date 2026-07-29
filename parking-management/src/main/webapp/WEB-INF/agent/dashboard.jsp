<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.ParkingEntry" %>
<%@ page import="com.parking.model.ParkingSpot" %>
<%@ page import="com.parking.model.Reservation" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agent Dashboard - Parking Management</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 30px; }
        .stat-card { background: white; padding: 20px; border-radius: 10px; text-align: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .stat-number { font-size: 32px; font-weight: bold; color: #4CAF50; }
        .actions { display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 30px; }
        .action-btn { background: #007bff; color: white; padding: 15px; text-decoration: none; border-radius: 5px; text-align: center; font-weight: bold; }
        .action-btn:hover { background: #0056b3; }
        .card { background: white; border-radius: 10px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .logout-btn { background: #dc3545; color: white; padding: 8px 15px; text-decoration: none; border-radius: 5px; }
        .message { background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .badge { background: red; color: white; border-radius: 50%; padding: 2px 8px; margin-left: 5px; font-size: 12px; }
    </style>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null || !"AGENT".equals(user.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<ParkingEntry> activeEntries = (List<ParkingEntry>) request.getAttribute("activeEntries");
        List<ParkingSpot> availableSpots = (List<ParkingSpot>) request.getAttribute("availableSpots");
        List<Reservation> todayReservations = (List<Reservation>) request.getAttribute("todayReservations");
        Integer availableCount = (Integer) request.getAttribute("availableCount");
        Integer occupiedCount = (Integer) request.getAttribute("occupiedCount");
        
        // Récupérer le nombre de réservations en attente
        Integer pendingReservationsCount = (Integer) request.getAttribute("pendingReservationsCount");
        if (pendingReservationsCount == null) pendingReservationsCount = 0;
    %>
    <div class="header">
        <h1>👤 Agent Dashboard - Parking Management</h1>
        <div><span>Bonjour, <%= user.getFullName() %></span> | <a href="../logout" class="logout-btn">Déconnexion</a></div>
    </div>
    <div class="container">
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        
        <div class="stats">
            <div class="stat-card"><h3>Places libres</h3><div class="stat-number"><%= availableCount %></div></div>
            <div class="stat-card"><h3>Places occupées</h3><div class="stat-number"><%= occupiedCount %></div></div>
            <div class="stat-card"><h3>Véhicules garés</h3><div class="stat-number"><%= activeEntries != null ? activeEntries.size() : 0 %></div></div>
            <div class="stat-card"><h3>Réservations aujourd'hui</h3><div class="stat-number"><%= todayReservations != null ? todayReservations.size() : 0 %></div></div>
        </div>
        
        <div class="actions">
            <a href="entry-exit" class="action-btn">🚗 Gestion entrées/sorties</a>
            <a href="reservations" class="action-btn">
                📅 Gérer les réservations
                <% if (pendingReservationsCount > 0) { %>
                    <span class="badge"><%= pendingReservationsCount %></span>
                <% } %>
            </a>
            <a href="../reservation" class="action-btn">📋 Voir toutes les réservations</a>
        </div>
        
        <div class="card">
            <h3>🚘 Véhicules actuellement garés</h3>
            <table>
                <thead><tr><th>Plaque</th><th>Place</th><th>Heure entrée</th><th>Durée</th></tr></thead>
                <tbody>
                    <% if (activeEntries != null && !activeEntries.isEmpty()) {
                        for (ParkingEntry entry : activeEntries) {
                            long minutes = (System.currentTimeMillis() - entry.getEntryTime().getTime()) / (60 * 1000);
                            long hours = minutes / 60;
                            minutes = minutes % 60;
                    %>
                        <tr>
                            <td><%= entry.getVehiclePlate() %></td>
                            <td>#<%= entry.getSpotId() %></td>
                                                        <td><%= entry.getEntryTime() %></td>
                            <td><%= hours %>h <%= minutes %>m</td>
                        </tr>
                    <% }
                    } else { %>
                        <tr><td colspan="4" style="text-align:center">Aucun véhicule garé</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <div class="card">
            <h3>📅 Réservations du jour</h3>
            <table>
                <thead><tr><th>Client</th><th>Place</th><th>Début</th><th>Fin</th><th>Durée</th></tr></thead>
                <tbody>
                    <% if (todayReservations != null && !todayReservations.isEmpty()) {
                        for (Reservation res : todayReservations) { %>
                            <tr>
                                <td><%= res.getUserName() %></td>
                                <td>#<%= res.getSpotId() %></td>
                                <td><%= res.getStartTime() %></td>
                                <td><%= res.getEndTime() %></td>
                                <td><%= res.getDurationHours() %>h</td>
                            </tr>
                    <% }
                    } else { %>
                        <tr><td colspan="5" style="text-align:center">Aucune réservation aujourd'hui</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>