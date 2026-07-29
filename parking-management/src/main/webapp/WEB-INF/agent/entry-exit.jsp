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
    <title>Agent - Entrées/Sorties</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-bottom: 30px; }
        .card { background: white; border-radius: 10px; padding: 25px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card h3 { margin-bottom: 20px; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; }
        input, select { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; }
        button { background: #28a745; color: white; padding: 12px; border: none; border-radius: 5px; cursor: pointer; width: 100%; font-size: 16px; }
        .btn-exit { background: #dc3545; }
        .btn-reservation { background: #17a2b8; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .stats-bar { display: flex; gap: 15px; margin-bottom: 20px; }
        .stat { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px; border-radius: 10px; flex: 1; text-align: center; }
        .stat-number { font-size: 28px; font-weight: bold; }
        .message, .error { padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .message { background: #d4edda; color: #155724; }
        .error { background: #f8d7da; color: #721c24; }
        .small-btn { padding: 5px 10px; width: auto; }
        .code-highlight { font-family: monospace; font-size: 16px; font-weight: bold; background: #f0f0f0; padding: 5px; border-radius: 3px; }
    </style>
</head>
<body>
    <%
        User agent = (User) session.getAttribute("user");
        if (agent == null || !"AGENT".equals(agent.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<ParkingEntry> activeEntries = (List<ParkingEntry>) request.getAttribute("activeEntries");
        List<ParkingSpot> availableSpots = (List<ParkingSpot>) request.getAttribute("availableSpots");
        List<Reservation> todayReservations = (List<Reservation>) request.getAttribute("todayReservations");
    %>
    <div class="header">
        <h1>🚗 Gestion des entrées et sorties</h1>
        <a href="../logout" style="color:white">Déconnexion</a>
    </div>
    <div class="container">
        <a href="dashboard" class="back-link">← Retour au dashboard</a>
        
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        <% if (session.getAttribute("error") != null) { %>
            <div class="error"><%= session.getAttribute("error") %></div>
            <% session.removeAttribute("error"); %>
        <% } %>
        
        <div class="stats-bar">
            <div class="stat"><div class="stat-number"><%= availableSpots != null ? availableSpots.size() : 0 %></div><div>Places libres</div></div>
            <div class="stat"><div class="stat-number"><%= activeEntries != null ? activeEntries.size() : 0 %></div><div>Véhicules garés</div></div>
            <div class="stat"><div class="stat-number"><%= todayReservations != null ? todayReservations.size() : 0 %></div><div>Réservations confirmées</div></div>
        </div>
        
        <div class="grid">
            <!-- Entrée normale -->
            <div class="card">
                <h3>✅ ENTRÉE SANS RÉSERVATION</h3>
                <form action="entry-exit" method="post">
                    <input type="hidden" name="action" value="entry">
                    <div class="form-group">
                        <label>Plaque d'immatriculation :</label>
                        <input type="text" name="vehiclePlate" placeholder="ex: ABC-123" required>
                    </div>
                    <div class="form-group">
                        <label>ID Client :</label>
                        <input type="number" name="userId" value="3" required>
                        <small>Client par défaut: 3 (john_doe)</small>
                    </div>
                    <div class="form-group">
                        <label>Place de parking :</label>
                        <select name="spotId" required>
                            <option value="">Sélectionner une place</option>
                            <% for (ParkingSpot spot : availableSpots) { %>
                                <option value="<%= spot.getId() %>"><%= spot.getSpotNumber() %> - <%= spot.getSpotType() %> - <%= spot.getHourlyRate() %> Ar/h</option>
                            <% } %>
                        </select>
                    </div>
                    <button type="submit">📥 Enregistrer l'entrée</button>
                </form>
            </div>
            
            <!-- Entrée par réservation -->
            <div class="card">
                <h3>🎫 ENTRÉE AVEC RÉSERVATION</h3>
                <form action="entry-exit" method="post">
                    <input type="hidden" name="action" value="entryByReservation">
                    <div class="form-group">
                        <label>Code de réservation :</label>
                        <input type="text" name="reservationCode" placeholder="ex: RES123456789" required>
                        <small>Le code est fourni au client après validation</small>
                    </div>
                    <button type="submit" class="btn-reservation">🎟️ Valider et entrer</button>
                </form>
            </div>
            
            <!-- Sortie -->
            <div class="card">
                <h3>❌ SORTIE</h3>
                <form action="entry-exit" method="post">
                    <input type="hidden" name="action" value="exit">
                    <div class="form-group">
                        <label>Véhicule à faire sortir :</label>
                        <select name="entryId" required>
                            <option value="">Sélectionner un véhicule</option>
                            <% for (ParkingEntry entry : activeEntries) { 
                                long minutes = (System.currentTimeMillis() - entry.getEntryTime().getTime()) / (60 * 1000);
                                long hours = minutes / 60;
                            %>
                                <option value="<%= entry.getId() %>"><%= entry.getVehiclePlate() %> - entré à <%= entry.getEntryTime() %> (il y a <%= hours %>h)</option>
                            <% } %>
                        </select>
                    </div>
                    <button type="submit" class="btn-exit">💰 Calculer et sortir</button>
                </form>
            </div>
        </div>
        
        <div class="card">
            <h3>📋 VÉHICULES ACTUELLEMENT GARÉS</h3>
            <table>
                <thead>
                    <tr><th>ID</th><th>Plaque</th><th>Place</th><th>Heure entrée</th><th>Durée</th><th>Action</th></tr>
                </thead>
                <tbody>
                    <% if (activeEntries != null && !activeEntries.isEmpty()) {
                        for (ParkingEntry entry : activeEntries) {
                            long minutes = (System.currentTimeMillis() - entry.getEntryTime().getTime()) / (60 * 1000);
                            long hours = minutes / 60;
                            minutes = minutes % 60;
                    %>
                        <tr>
                            <td><%= entry.getId() %></td>
                            <td><%= entry.getVehiclePlate() %></td>
                            <td>#<%= entry.getSpotId() %></td>
                            <td><%= entry.getEntryTime() %></td>
                            <td><%= hours %>h <%= minutes %>m</td>
                            <td>
                                <form method="post" style="display:inline">
                                    <input type="hidden" name="action" value="exit">
                                    <input type="hidden" name="entryId" value="<%= entry.getId() %>">
                                    <button type="submit" class="small-btn" style="background:#dc3545">Sortir</button>
                                </form>
                            </td>
                        </tr>
                    <% }
                    } else { %>
                        <tr><td colspan="6" style="text-align:center">Aucun véhicule garé</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <div class="card">
            <h3>📅 RÉSERVATIONS CONFIRMÉES DU JOUR</h3>
            <table>
                <thead>
                    <tr><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th></tr>
                </thead>
                <tbody>
                    <% if (todayReservations != null && !todayReservations.isEmpty()) {
                        for (Reservation res : todayReservations) { %>
                            <tr>
                                <td><%= res.getUserName() %></td>
                                <td>#<%= res.getSpotId() %></td>
                                <td><%= res.getVehiclePlate() %></td>
                                <td><%= res.getStartTime() %></td>
                                <td><%= res.getEndTime() %></td>
                                <td><span class="code-highlight"><%= res.getReservationCode() %></span></td>
                            </tr>
                    <% }
                    } else { %>
                        <tr><td colspan="6" style="text-align:center">Aucune réservation confirmée aujourd'hui</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>