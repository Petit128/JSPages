<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.Reservation" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agent - Gestion des réservations</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .card { background: white; border-radius: 10px; padding: 20px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card h3 { margin-bottom: 15px; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .btn-confirm { background: #28a745; color: white; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; }
        .btn-reject { background: #dc3545; color: white; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; }
        .message { background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .error { background: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .code { font-family: monospace; font-size: 12px; font-weight: bold; background: #f0f0f0; padding: 3px 6px; border-radius: 3px; }
    </style>
</head>
<body>
    <%
        User agent = (User) session.getAttribute("user");
        if (agent == null || !"AGENT".equals(agent.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<Reservation> pendingReservations = (List<Reservation>) request.getAttribute("pendingReservations");
        List<Reservation> confirmedReservations = (List<Reservation>) request.getAttribute("confirmedReservations");
    %>
    <div class="header">
        <h1>👤 Agent - Gestion des réservations</h1>
        <a href="../logout" style="color:white">Déconnexion</a>
    </div>
    <div class="container">
        <a href="dashboard" class="back-link">← Retour</a>
        
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        <% if (session.getAttribute("error") != null) { %>
            <div class="error"><%= session.getAttribute("error") %></div>
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <div class="card">
            <h3>⏳ Réservations en attente de validation</h3>
            <table>
                <thead>
                    <tr><th>ID</th><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th><th>Action</th></tr>
                </thead>
                <tbody>
                    <% if (pendingReservations != null && !pendingReservations.isEmpty()) {
                        for (Reservation res : pendingReservations) { %>
                            <tr>
                                <td><%= res.getId() %></td>
                                <td><%= res.getUserName() %></td>
                                <td>#<%= res.getSpotId() %></td>
                                <td><%= res.getVehiclePlate() != null ? res.getVehiclePlate() : "-" %></td>
                                <td><%= res.getStartTime() %></td>
                                <td><%= res.getEndTime() %></td>
                                <td><span class="code"><%= res.getReservationCode() != null ? res.getReservationCode() : "-" %></span></td>
                                <td>
                                    <form method="post" style="display:inline">
                                        <input type="hidden" name="action" value="confirm">
                                        <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                        <button type="submit" class="btn-confirm">✅ Confirmer</button>
                                    </form>
                                    <form method="post" style="display:inline">
                                        <input type="hidden" name="action" value="reject">
                                        <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                        <button type="submit" class="btn-reject">❌ Rejeter</button>
                                    </form>
                                </td>
                            </tr>
                    <%  }
                    } else { %>
                        <tr><td colspan="8" style="text-align:center">Aucune réservation en attente</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <div class="card">
            <h3>✅ Réservations confirmées</h3>
            <table>
                <thead>
                    <tr><th>ID</th><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th></tr>
                </thead>
                <tbody>
                    <% if (confirmedReservations != null && !confirmedReservations.isEmpty()) {
                        for (Reservation res : confirmedReservations) { %>
                            <tr>
                                <td><%= res.getId() %></td>
                                <td><%= res.getUserName() %></td>
                                <td>#<%= res.getSpotId() %></td>
                                <td><%= res.getVehiclePlate() != null ? res.getVehiclePlate() : "-" %></td>
                                <td><%= res.getStartTime() %></td>
                                <td><%= res.getEndTime() %></td>
                                <td><span class="code"><%= res.getReservationCode() != null ? res.getReservationCode() : "-" %></span></td>
                            </tr>
                    <%  }
                    } else { %>
                        <tr><td colspan="7" style="text-align:center">Aucune réservation confirmée</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>