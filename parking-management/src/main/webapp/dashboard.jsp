<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.ParkingEntry" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client - Parking Management System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; }
        .header h1 { margin: 0; font-size: 24px; }
        .user-info { display: flex; align-items: center; gap: 15px; }
        .logout-btn { background-color: #dc3545; color: white; padding: 8px 15px; text-decoration: none; border-radius: 5px; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .stats-container { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .stat-card { background-color: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); text-align: center; }
        .stat-card h3 { margin: 0 0 10px 0; color: #666; }
        .stat-card .number { font-size: 32px; font-weight: bold; color: #4CAF50; }
        .actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin-bottom: 30px; }
        .action-btn { background-color: #007bff; color: white; padding: 15px; text-decoration: none; border-radius: 5px; text-align: center; font-size: 16px; transition: background-color 0.3s; }
        .action-btn:hover { background-color: #0056b3; }
        .table-container { background-color: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #f8f9fa; font-weight: bold; color: #333; }
        .badge { display: inline-block; padding: 3px 8px; border-radius: 3px; font-size: 12px; font-weight: bold; }
        .badge-vip { background-color: #ffc107; color: #333; }
        .badge-regular { background-color: #6c757d; color: white; }
        .message, .error { padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .message { background-color: #d4edda; color: #155724; }
        .error { background-color: #f8d7da; color: #721c24; }
    </style>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null || !"CLIENT".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }
        
        Integer availableSpots = (Integer) request.getAttribute("availableSpots");
        List<ParkingEntry> userHistory = (List<ParkingEntry>) request.getAttribute("userHistory");
    %>
    
    <div class="header">
        <h1>🚗 Parking Management System</h1>
        <div class="user-info">
            <span>Bonjour, <%= user.getFullName() %></span>
            <span class="badge badge-<%= user.getUserType().toLowerCase() %>"><%= user.getUserType() %></span>
            <a href="logout" class="logout-btn">Déconnexion</a>
        </div>
    </div>
    
    <div class="container">
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        <% if (session.getAttribute("error") != null) { %>
            <div class="error"><%= session.getAttribute("error") %></div>
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <div class="stats-container">
            <div class="stat-card">
                <h3>Places disponibles</h3>
                <div class="number"><%= availableSpots != null ? availableSpots : 0 %></div>
            </div>
            <div class="stat-card">
                <h3>Mes stationnements</h3>
                <div class="number"><%= userHistory != null ? userHistory.size() : 0 %></div>
            </div>
        </div>
        
        <div class="actions">
            <a href="reservation" class="action-btn">📅 Réserver une place</a>
            <a href="subscription" class="action-btn">🎫 Gérer mon abonnement</a>
        </div>
        
        <div class="table-container">
            <h2>📜 Historique de mes stationnements</h2>
            <table>
                <thead>
                    <tr>
                        <th>Plaque</th>
                        <th>Place</th>
                        <th>Entrée</th>
                        <th>Sortie</th>
                        <th>Montant</th>
                        <th>Statut</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (userHistory != null && !userHistory.isEmpty()) {
                        for (ParkingEntry entry : userHistory) {
                    %>
                        <tr>
                            <td><%= entry.getVehiclePlate() != null ? entry.getVehiclePlate() : "-" %></td>
                            <td>#<%= entry.getSpotId() %></td>
                            <td><%= entry.getEntryTime() %></td>
                            <td><%= entry.getExitTime() != null ? entry.getExitTime() : "En cours" %></td>
                            <td><%= entry.getAmountPaid() > 0 ? entry.getAmountPaid() + " Ar" : "-" %></td>
                            <td><%= entry.getPaymentStatus() %></td>
                        </tr>
                    <%      }
                        } else { %>
                        <tr>
                            <td colspan="6" style="text-align: center;">Aucun historique trouvé</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>