<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.Subscription" %>
<%@ page import="com.parking.model.ParkingSpot" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Abonnements - Parking Management System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .card { background: white; border-radius: 10px; padding: 25px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 30px; }
        .subscription-active { background: linear-gradient(135deg, #28a745 0%, #20c997 100%); color: white; padding: 25px; border-radius: 10px; margin-bottom: 30px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; }
        select, input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 5px; }
        button { background: #28a745; color: white; padding: 12px; border: none; border-radius: 5px; cursor: pointer; width: 100%; font-size: 16px; }
        .cancel-btn { background: #dc3545; }
        .subscription-types { display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin: 20px 0; }
        .type-card { border: 2px solid #ddd; border-radius: 10px; padding: 15px; text-align: center; cursor: pointer; transition: all 0.3s; }
        .type-card:hover { border-color: #28a745; transform: translateY(-2px); }
        .type-card.selected { border-color: #28a745; background: #e8f5e9; }
        .price { font-size: 24px; font-weight: bold; color: #28a745; margin: 10px 0; }
        .discount { font-size: 14px; color: #ffc107; }
        .message, .error { padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .message { background: #d4edda; color: #155724; }
        .error { background: #f8d7da; color: #721c24; }
    </style>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null || !"CLIENT".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }
        Subscription subscription = (Subscription) request.getAttribute("subscription");
        List<ParkingSpot> availableSpots = (List<ParkingSpot>) request.getAttribute("availableSpots");
    %>
    <div class="header">
        <h1>🎫 Gestion des abonnements</h1>
        <a href="logout" style="color:white">Déconnexion</a>
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
        
        <% if (subscription != null && "ACTIVE".equals(subscription.getStatus())) { %>
            <div class="subscription-active">
                <h3>✅ Vous avez un abonnement actif !</h3>
                <p><strong>Type :</strong> <%= subscription.getSubscriptionType() %></p>
                <p><strong>Place réservée :</strong> #<%= subscription.getSpotId() %></p>
                <p><strong>Date de début :</strong> <%= subscription.getStartDate() %></p>
                <p><strong>Date de fin :</strong> <%= subscription.getEndDate() %></p>
                <p><strong>Prix :</strong> <%= subscription.getPrice() %> Ar</p>
                <form action="subscription" method="post" style="margin-top:15px">
                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="subscriptionId" value="<%= subscription.getId() %>">
                    <button type="submit" class="cancel-btn">❌ Résilier l'abonnement</button>
                </form>
            </div>
        <% } else { %>
            <div class="card">
                <h3>🎯 Souscrire à un abonnement</h3>
                <form action="subscription" method="post" id="subForm">
                    <input type="hidden" name="action" value="create">
                    <input type="hidden" name="subscriptionType" id="selectedType">
                    
                    <div class="form-group">
                        <label>Choisir une place :</label>
                        <select name="spotId" required>
                            <option value="">Sélectionner...</option>
                            <% for (ParkingSpot spot : availableSpots) { %>
                                <option value="<%= spot.getId() %>"><%= spot.getSpotNumber() %> - <%= spot.getSpotType() %> - <%= spot.getHourlyRate() %> Ar/h</option>
                            <% } %>
                        </select>
                    </div>
                    
                    <div class="subscription-types">
                        <div class="type-card" data-type="WEEKLY">
                            <h4>📆 Hebdomadaire</h4>
                            <div class="price">-20%</div>
                            <div class="discount">Économisez 20%</div>
                        </div>
                        <div class="type-card" data-type="MONTHLY">
                            <h4>📅 Mensuel</h4>
                            <div class="price">-30%</div>
                            <div class="discount">Économisez 30%</div>
                        </div>
                        <div class="type-card" data-type="YEARLY">
                            <h4>🎉 Annuel</h4>
                            <div class="price">-50%</div>
                            <div class="discount">Économisez 50%</div>
                        </div>
                    </div>
                    
                    <button type="submit">💳 Souscrire</button>
                </form>
            </div>
        <% } %>
    </div>
    
    <script>
        document.querySelectorAll('.type-card').forEach(card => {
            card.addEventListener('click', function() {
                document.querySelectorAll('.type-card').forEach(c => c.classList.remove('selected'));
                this.classList.add('selected');
                document.getElementById('selectedType').value = this.getAttribute('data-type');
            });
        });
        
        document.getElementById('subForm').addEventListener('submit', function(e) {
            if (!document.getElementById('selectedType').value) {
                alert('Veuillez sélectionner un type d\'abonnement');
                e.preventDefault();
            }
        });
    </script>
</body>
</html>