<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.ParkingSpot" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des places - Admin</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .card { background: white; border-radius: 10px; padding: 25px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 30px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        button { background: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; }
        .btn-delete { background: #dc3545; padding: 5px 10px; font-size: 12px; }
        .btn-edit { background: #007bff; padding: 5px 10px; font-size: 12px; }
        table { width: 100%; border-collapse: collapse; background: white; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .message, .error { padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .message { background: #d4edda; color: #155724; }
        .modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); justify-content: center; align-items: center; }
        .modal-content { background: white; padding: 30px; border-radius: 10px; width: 500px; }
    </style>
</head>
<body>
    <%
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<ParkingSpot> spots = (List<ParkingSpot>) request.getAttribute("allSpots");
    %>
    <div class="header">
        <h1>🅿️ Gestion des places de parking</h1>
        <a href="../logout" style="color:white">Déconnexion</a>
    </div>
    <div class="container">
        <a href="dashboard" class="back-link">← Retour</a>
        
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        
        <div class="card">
            <h3>➕ Ajouter une nouvelle place</h3>
            <form action="spots" method="post">
                <input type="hidden" name="action" value="add">
                <div style="display: grid; grid-template-columns: repeat(2,1fr); gap:15px">
                    <div class="form-group"><label>Numéro place:</label><input type="text" name="spotNumber" required></div>
                    <div class="form-group">
                        <label>Type:</label>
                        <select name="spotType">
                            <option value="STANDARD">STANDARD</option>
                            <option value="VIP">VIP</option>
                            <option value="DISABLED">DISABLED</option>
                            <option value="ELECTRIC">ELECTRIC</option>
                        </select>
                    </div>
                    <div class="form-group"><label>Localisation:</label><input type="text" name="location"></div>
                    <div class="form-group"><label>Tarif horaire (Ar):</label><input type="number" step="0.5" name="hourlyRate" required></div>
                </div>
                <button type="submit">Ajouter la place</button>
            </form>
        </div>
        
        <div class="card">
            <h3>📋 Liste des places</h3>
            <table>
                <thead><tr><th>ID</th><th>Numéro</th><th>Type</th><th>Localisation</th><th>Tarif/h</th><th>Statut</th><th>VIP</th><th>Action</th></tr></thead>
                <tbody>
                    <% for (ParkingSpot spot : spots) { %>
                        <tr>
                            <td><%= spot.getId() %></td>
                            <td><%= spot.getSpotNumber() %></td>
                            <td><%= spot.getSpotType() %></td>
                            <td><%= spot.getLocation() != null ? spot.getLocation() : "-" %></td>
                            <td><%= spot.getHourlyRate() %> Ar</td>
                            <td class="<%= spot.isAvailable() ? "available" : "occupied" %>"><%= spot.isAvailable() ? "Libre" : "Occupée" %></td>
                            <td><%= spot.isReservedForVIP() ? "Oui" : "Non" %></td>
                            <td>
                                <form method="post" style="display:inline">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= spot.getId() %>">
                                    <button type="submit" class="btn-delete" onclick="return confirm('Supprimer cette place ?')">🗑️</button>
                                </form>
                             </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>