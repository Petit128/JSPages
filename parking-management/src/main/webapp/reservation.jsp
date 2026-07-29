<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.ParkingSpot" %>
<%@ page import="com.parking.model.Reservation" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Réservation de place - Parking Management System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1000px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .card { background: white; border-radius: 10px; padding: 25px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 30px; }
        .card h3 { margin-bottom: 20px; color: #333; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; }
        select, input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; }
        button { background: #28a745; color: white; padding: 12px 25px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; width: 100%; }
        button:hover { background: #218838; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .cancel-btn { background: #dc3545; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
        .message, .error { padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .message { background: #d4edda; color: #155724; }
        .error { background: #f8d7da; color: #721c24; }
        .status-en_attente { background: #ffc107; color: #333; padding: 3px 8px; border-radius: 3px; }
        .status-confirmee { background: #28a745; color: white; padding: 3px 8px; border-radius: 3px; }
        .status-annulee { background: #dc3545; color: white; padding: 3px 8px; border-radius: 3px; }
        .code-reservation { font-family: monospace; font-size: 12px; font-weight: bold; }
        .info-box { background: #e8f5e9; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
    </style>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null || !"CLIENT".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }
        List<ParkingSpot> availableSpots = (List<ParkingSpot>) request.getAttribute("availableSpots");
        List<Reservation> userReservations = (List<Reservation>) request.getAttribute("userReservations");
    %>
    <div class="header">
        <h1>🚗 Réservation de place</h1>
        <a href="logout" style="color:white">Déconnexion</a>
    </div>
    <div class="container">
        <a href="dashboard" class="back-link">← Retour au tableau de bord</a>
        
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        <% if (session.getAttribute("error") != null) { %>
            <div class="error"><%= session.getAttribute("error") %></div>
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <div class="info-box">
            <strong>ℹ️ Comment ça marche ?</strong><br>
            1. Remplissez le formulaire ci-dessous pour réserver une place<br>
            2. Votre réservation sera en attente de validation par un agent<br>
            3. Une fois validée, vous recevrez un code unique<br>
            4. Présentez ce code à l'entrée du parking
        </div>
        
        <div class="card">
            <h3>📅 Nouvelle réservation</h3>
            <form action="reservation" method="post">
                <input type="hidden" name="action" value="create">
                
                <div class="form-group">
                    <label>Choisir une place :</label>
                    <select name="spotId" required>
                        <option value="">Sélectionner...</option>
                        <% if (availableSpots != null) {
                            for (ParkingSpot spot : availableSpots) { %>
                                <option value="<%= spot.getId() %>">
                                    <%= spot.getSpotNumber() %> - <%= spot.getSpotType() %> - <%= spot.getHourlyRate() %> Ar/h
                                </option>
                        <%   }
                        } %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Plaque d'immatriculation :</label>
                    <input type="text" name="vehiclePlate" placeholder="ex: ABC-123" required>
                </div>
                
                <div class="form-group">
                    <label>Date et heure de début :</label>
                    <input type="datetime-local" id="startTime" name="startTime" required>
                </div>
                
                <div class="form-group">
                    <label>Date et heure de fin :</label>
                    <input type="datetime-local" id="endTime" name="endTime" required>
                </div>
                
                <button type="submit">✔️ Demander la réservation</button>
            </form>
        </div>
        
        <div class="card">
            <h3>📋 Mes réservations</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Place</th>
                        <th>Plaque</th>
                        <th>Début</th>
                        <th>Fin</th>
                        <th>Prix</th>
                        <th>Statut</th>
                        <th>Code</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (userReservations != null && !userReservations.isEmpty()) {
                        for (Reservation res : userReservations) { %>
                            <tr>
                                <td><%= res.getId() %></td>
                                <td>#<%= res.getSpotId() %></td>
                                <td><%= res.getVehiclePlate() != null ? res.getVehiclePlate() : "-" %></td>
                                <td><%= res.getStartTime() %></td>
                                <td><%= res.getEndTime() %></td>
                                <td><%= res.getTotalPrice() %> Ar</td>
                                <td><span class="status-<%= res.getStatus().toLowerCase() %>"><%= res.getStatus() %></span></td>
                                <td><span class="code-reservation"><%= res.getReservationCode() != null ? res.getReservationCode() : "-" %></span></td>
                                <td>
                                    <% if ("EN_ATTENTE".equals(res.getStatus())) { %>
                                        <form method="post" style="display:inline">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                            <button type="submit" class="cancel-btn">Annuler</button>
                                        </form>
                                    <% } else { %>
                                        -
                                    <% } %>
                                </td>
                            </tr>
                        <% }
                    } else { %>
                        <tr><td colspan="9" style="text-align:center">Aucune réservation</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
    
    <script>
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        document.getElementById('startTime').min = now.toISOString().slice(0,16);
        document.getElementById('endTime').min = now.toISOString().slice(0,16);
        
        document.getElementById('endTime').addEventListener('change', function() {
            const start = document.getElementById('startTime').value;
            const end = this.value;
            if (start && end && end <= start) {
                alert('La date de fin doit être après la date de début');
                this.value = '';
            }
        });
    </script>
</body>
</html>