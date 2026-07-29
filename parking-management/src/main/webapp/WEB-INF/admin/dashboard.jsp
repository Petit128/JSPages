<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.ParkingSpot" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Parking Management System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .stat-card { background: white; padding: 20px; border-radius: 10px; text-align: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .stat-number { font-size: 32px; font-weight: bold; color: #4CAF50; }
        .grid-actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 15px; margin-bottom: 30px; }
        .action-card { background: white; padding: 20px; border-radius: 10px; text-align: center; text-decoration: none; color: #333; transition: transform 0.3s; box-shadow: 0 2px 5px rgba(0,0,0,0.1); position: relative; }
        .action-card:hover { transform: translateY(-5px); }
        .action-card h3 { margin-bottom: 10px; }
        .action-card p { color: #666; font-size: 14px; }
        table { width: 100%; border-collapse: collapse; background: white; border-radius: 10px; overflow: hidden; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .logout-btn { background: #dc3545; color: white; padding: 8px 15px; text-decoration: none; border-radius: 5px; }
        .available { color: #28a745; font-weight: bold; }
        .occupied { color: #dc3545; font-weight: bold; }
        .message, .error { padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .message { background: #d4edda; color: #155724; }
        .badge { background: red; color: white; border-radius: 50%; padding: 2px 8px; font-size: 12px; margin-left: 5px; }
    </style>
</head>
<body>
    <%
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        
        Integer totalSpots = (Integer) request.getAttribute("totalSpots");
        if (totalSpots == null) totalSpots = 0;
        
        Integer availableSpots = (Integer) request.getAttribute("availableSpots");
        if (availableSpots == null) availableSpots = 0;
        
        Double occupancyRate = (Double) request.getAttribute("occupancyRate");
        if (occupancyRate == null) occupancyRate = 0.0;
        
        Double dailyRevenue = (Double) request.getAttribute("dailyRevenue");
        if (dailyRevenue == null) dailyRevenue = 0.0;
        
        Integer activeSubscriptions = (Integer) request.getAttribute("activeSubscriptions");
        if (activeSubscriptions == null) activeSubscriptions = 0;
        
        Integer totalUsers = (Integer) request.getAttribute("totalUsers");
        if (totalUsers == null) totalUsers = 0;
        
        Integer pendingReservationsCount = (Integer) request.getAttribute("pendingReservationsCount");
        if (pendingReservationsCount == null) pendingReservationsCount = 0;
        
        List<ParkingSpot> allSpots = (List<ParkingSpot>) request.getAttribute("allSpots");
    %>
    <div class="header">
        <h1>👑 Admin Dashboard - Parking Management</h1>
        <div><span>Bonjour, <%= admin.getFullName() %></span> | <a href="../logout" class="logout-btn">Déconnexion</a></div>
    </div>
    <div class="container">
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        
        <div class="stats">
            <div class="stat-card"><h3>Total places</h3><div class="stat-number"><%= totalSpots %></div></div>
            <div class="stat-card"><h3>Places libres</h3><div class="stat-number"><%= availableSpots %></div></div>
            <div class="stat-card"><h3>Taux occupation</h3><div class="stat-number"><%= Math.round(occupancyRate) %>%</div></div>
            <div class="stat-card"><h3>CA aujourd'hui</h3><div class="stat-number"><%= String.format("%.0f", dailyRevenue) %> Ar</div></div>
            <div class="stat-card"><h3>Abonnements actifs</h3><div class="stat-number"><%= activeSubscriptions %></div></div>
            <div class="stat-card"><h3>Utilisateurs</h3><div class="stat-number"><%= totalUsers %></div></div>
        </div>
        
        <div class="grid-actions">
            <a href="spots" class="action-card"><h3>🅿️ Gestion des places</h3><p>Ajouter, modifier ou supprimer des places</p></a>
            <a href="users" class="action-card"><h3>👥 Gestion des utilisateurs</h3><p>Gérer les comptes clients et agents</p></a>
            <a href="tariffs" class="action-card"><h3>💰 Configuration des tarifs</h3><p>Modifier les prix et réductions</p></a>
            <a href="reservations" class="action-card">
                <h3>📅 Gestion des réservations</h3>
                <p>Valider ou rejeter les demandes</p>
                <% if (pendingReservationsCount > 0) { %>
                    <span class="badge"><%= pendingReservationsCount %> en attente</span>
                <% } %>
            </a>
        </div>
        
        <h3>📊 État des places</h3>
        <table>
            <thead><tr><th>Place</th><th>Type</th><th>Tarif horaire</th><th>Statut</th><th>VIP</th></tr></thead>
            <tbody>
                <% if (allSpots != null) {
                    for (ParkingSpot spot : allSpots) { %>
                        <tr>
                            <td><%= spot.getSpotNumber() %></td>
                            <td><%= spot.getSpotType() %></td>
                            <td><%= spot.getHourlyRate() %> Ar</td>
                            <td class="<%= spot.isAvailable() ? "available" : "occupied" %>"><%= spot.isAvailable() ? "Libre" : "Occupée" %></td>
                            <td><%= spot.isReservedForVIP() ? "⭐ VIP" : "-" %></td>
                        </tr>
                <%  }
                } else { %>
                    <tr><td colspan="5" style="text-align:center">Aucune place trouvée</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>