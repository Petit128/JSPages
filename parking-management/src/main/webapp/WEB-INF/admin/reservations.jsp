<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.Reservation" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Gestion des réservations</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1400px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .card { background: white; border-radius: 10px; padding: 20px; margin-bottom: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .card h3 { margin-bottom: 15px; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        .btn-confirm { background: #28a745; color: white; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; }
        .btn-reject { background: #ffc107; color: #333; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; }
        .btn-cancel { background: #dc3545; color: white; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; }
        .btn-delete { background: #6c757d; color: white; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; }
        .status-pending { background: #ffc107; color: #333; padding: 3px 8px; border-radius: 3px; }
        .status-confirmee { background: #28a745; color: white; padding: 3px 8px; border-radius: 3px; }
        .status-terminee { background: #17a2b8; color: white; padding: 3px 8px; border-radius: 3px; }
        .status-annulee { background: #dc3545; color: white; padding: 3px 8px; border-radius: 3px; }
        .message { background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .error { background: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .code { font-family: monospace; font-size: 12px; font-weight: bold; background: #f0f0f0; padding: 3px 6px; border-radius: 3px; }
        .tab-buttons { display: flex; gap: 10px; margin-bottom: 20px; }
        .tab-btn { background: #e9ecef; border: none; padding: 10px 20px; cursor: pointer; border-radius: 5px; }
        .tab-btn.active { background: #667eea; color: white; }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
        .motif-input { display: none; margin-top: 5px; }
        .motif-input input { width: 200px; padding: 5px; }
    </style>
</head>
<body>
    <%
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<Reservation> pendingReservations = (List<Reservation>) request.getAttribute("pendingReservations");
        List<Reservation> confirmedReservations = (List<Reservation>) request.getAttribute("confirmedReservations");
        List<Reservation> completedReservations = (List<Reservation>) request.getAttribute("completedReservations");
        List<Reservation> cancelledReservations = (List<Reservation>) request.getAttribute("cancelledReservations");
    %>
    <div class="header">
        <h1>👑 Admin - Gestion complète des réservations</h1>
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
        
        <div class="tab-buttons">
            <button class="tab-btn active" onclick="showTab('pending')">⏳ En attente (<%= pendingReservations != null ? pendingReservations.size() : 0 %>)</button>
            <button class="tab-btn" onclick="showTab('confirmed')">✅ Confirmées (<%= confirmedReservations != null ? confirmedReservations.size() : 0 %>)</button>
            <button class="tab-btn" onclick="showTab('completed')">✔️ Terminées (<%= completedReservations != null ? completedReservations.size() : 0 %>)</button>
            <button class="tab-btn" onclick="showTab('cancelled')">🗑️ Annulées (<%= cancelledReservations != null ? cancelledReservations.size() : 0 %>)</button>
        </div>
        
        <!-- Onglet En attente -->
        <div id="pending" class="tab-content active">
            <div class="card">
                <h3>⏳ Réservations en attente de validation</h3>
                <table>
                    <thead><tr><th>ID</th><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th><th colspan="2">Action</th></tr></thead>
                    <tbody>
                        <% if (pendingReservations != null && !pendingReservations.isEmpty()) {
                            for (Reservation res : pendingReservations) { %>
                                <tr id="row-<%= res.getId() %>">
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
                                    </td>
                                    <td>
                                        <button class="btn-reject" onclick="showMotif(<%= res.getId() %>, 'reject')">❌ Rejeter</button>
                                        <div id="motif-<%= res.getId() %>" class="motif-input">
                                            <form method="post" style="display:inline">
                                                <input type="hidden" name="action" value="reject">
                                                <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                                <input type="text" name="motif" placeholder="Motif du rejet" size="20">
                                                <button type="submit">OK</button>
                                                <button type="button" onclick="hideMotif(<%= res.getId() %>)">Annuler</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                        <%  }
                        } else { %>
                            <tr><td colspan="9" style="text-align:center">Aucune réservation en attente</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Onglet Confirmées -->
        <div id="confirmed" class="tab-content">
            <div class="card">
                <h3>✅ Réservations confirmées</h3>
                <table>
                    <thead><tr><th>ID</th><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th><th>Action</th></tr></thead>
                    <tbody>
                        <% if (confirmedReservations != null && !confirmedReservations.isEmpty()) {
                            for (Reservation res : confirmedReservations) { %>
                                <tr id="row2-<%= res.getId() %>">
                                    <td><%= res.getId() %></td>
                                    <td><%= res.getUserName() %></td>
                                    <td>#<%= res.getSpotId() %></td>
                                    <td><%= res.getVehiclePlate() != null ? res.getVehiclePlate() : "-" %></td>
                                    <td><%= res.getStartTime() %></td>
                                    <td><%= res.getEndTime() %></td>
                                    <td><span class="code"><%= res.getReservationCode() != null ? res.getReservationCode() : "-" %></span></td>
                                    <td>
                                        <button class="btn-cancel" onclick="showMotif2(<%= res.getId() %>, 'cancel')">🗑️ Annuler</button>
                                        <div id="motif2-<%= res.getId() %>" class="motif-input">
                                            <form method="post" style="display:inline">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                                <input type="text" name="motif" placeholder="Motif d'annulation" size="20">
                                                <button type="submit">OK</button>
                                                <button type="button" onclick="hideMotif2(<%= res.getId() %>)">Annuler</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                        <%  }
                        } else { %>
                            <tr><td colspan="8" style="text-align:center">Aucune réservation confirmée</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Onglet Terminées -->
        <div id="completed" class="tab-content">
            <div class="card">
                <h3>✔️ Réservations terminées</h3>
                <table>
                    <thead><tr><th>ID</th><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th><th>Action</th></tr></thead>
                    <tbody>
                        <% if (completedReservations != null && !completedReservations.isEmpty()) {
                            for (Reservation res : completedReservations) { %>
                                <tr>
                                    <td><%= res.getId() %></td>
                                    <td><%= res.getUserName() %></td>
                                    <td>#<%= res.getSpotId() %></td>
                                    <td><%= res.getVehiclePlate() != null ? res.getVehiclePlate() : "-" %></td>
                                    <td><%= res.getStartTime() %></td>
                                    <td><%= res.getEndTime() %></td>
                                    <td><span class="code"><%= res.getReservationCode() != null ? res.getReservationCode() : "-" %></span></td>
                                    <td>
                                        <form method="post" style="display:inline" onsubmit="return confirm('Supprimer définitivement cette réservation ?')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                            <button type="submit" class="btn-delete">💀 Supprimer</button>
                                        </form>
                                    </td>
                                </tr>
                        <%  }
                        } else { %>
                            <tr><td colspan="8" style="text-align:center">Aucune réservation terminée</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- Onglet Annulées -->
        <div id="cancelled" class="tab-content">
            <div class="card">
                <h3>🗑️ Réservations annulées</h3>
                <table>
                    <thead><tr><th>ID</th><th>Client</th><th>Place</th><th>Plaque</th><th>Début</th><th>Fin</th><th>Code</th><th>Motif</th><th>Action</th></tr></thead>
                    <tbody>
                        <% if (cancelledReservations != null && !cancelledReservations.isEmpty()) {
                            for (Reservation res : cancelledReservations) { %>
                                <tr>
                                    <td><%= res.getId() %></td>
                                    <td><%= res.getUserName() %></td>
                                    <td>#<%= res.getSpotId() %></td>
                                    <td><%= res.getVehiclePlate() != null ? res.getVehiclePlate() : "-" %></td>
                                    <td><%= res.getStartTime() %></td>
                                    <td><%= res.getEndTime() %></td>
                                    <td><span class="code"><%= res.getReservationCode() != null ? res.getReservationCode() : "-" %></span></td>
                                    <td><%= res.getMotifAnnulation() != null ? res.getMotifAnnulation() : "-" %></td>
                                    <td>
                                        <form method="post" style="display:inline" onsubmit="return confirm('Supprimer définitivement cette réservation ?')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="reservationId" value="<%= res.getId() %>">
                                            <button type="submit" class="btn-delete">💀 Supprimer</button>
                                        </form>
                                    </td>
                                </tr>
                        <%  }
                        } else { %>
                            <tr><td colspan="9" style="text-align:center">Aucune réservation annulée</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <script>
        function showTab(tabName) {
            document.querySelectorAll('.tab-content').forEach(tab => {
                tab.classList.remove('active');
            });
            document.getElementById(tabName).classList.add('active');
            
            document.querySelectorAll('.tab-btn').forEach(btn => {
                btn.classList.remove('active');
            });
            event.target.classList.add('active');
        }
        
        function showMotif(id, action) {
            document.getElementById('motif-' + id).style.display = 'inline-block';
        }
        
        function hideMotif(id) {
            document.getElementById('motif-' + id).style.display = 'none';
        }
        
        function showMotif2(id, action) {
            document.getElementById('motif2-' + id).style.display = 'inline-block';
        }
        
        function hideMotif2(id) {
            document.getElementById('motif2-' + id).style.display = 'none';
        }
    </script>
</body>
</html>