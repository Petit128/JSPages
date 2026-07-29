<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="com.parking.model.Tariff" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Configuration des tarifs - Admin</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f4f4f4; }
        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 20px; display: flex; justify-content: space-between; }
        .container { padding: 20px; max-width: 1200px; margin: 0 auto; }
        .back-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .card { background: white; border-radius: 10px; padding: 25px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #f8f9fa; }
        input { width: 100px; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        button { background: #28a745; color: white; padding: 8px 15px; border: none; border-radius: 5px; cursor: pointer; }
        .message { background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .info-box { margin-top: 20px; padding: 15px; background: #e8f5e9; border-radius: 5px; }
    </style>
</head>
<body>
    <%
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<Tariff> tariffs = (List<Tariff>) request.getAttribute("allTariffs");
    %>
    <div class="header">
        <h1>💰 Configuration des tarifs</h1>
        <a href="../logout" style="color:white">Déconnexion</a>
    </div>
    <div class="container">
        <a href="dashboard" class="back-link">← Retour</a>
        
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        
        <div class="card">
            <h3>📊 Tarifs par type de place</h3>
            <form method="post" action="tariffs">
                <input type="hidden" name="action" value="update">
                <table>
                    <thead><tr><th>Type</th><th>Tarif horaire (Ar)</th><th>Réduction VIP</th><th>Réduction Abonné</th><th>Action</th></tr></thead>
                    <tbody>
                        <% for (Tariff t : tariffs) { %>
                            <tr>
                                <td><strong><%= t.getTariffType() %></strong></td>
                                <td><input type="number" step="0.5" name="hourlyRate_<%= t.getTariffType() %>" value="<%= t.getHourlyRate() %>"></td>
                                <td><input type="number" step="5" name="vipDiscount_<%= t.getTariffType() %>" value="<%= t.getVipDiscount() %>"> %</td>
                                <td><input type="number" step="5" name="subscriberDiscount_<%= t.getTariffType() %>" value="<%= t.getSubscriberDiscount() %>"> %</td>
                                <td><button type="submit" name="tariffType" value="<%= t.getTariffType() %>">💾 Mettre à jour</button></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </form>
            
            <div class="info-box">
                <h4>ℹ️ Informations tarifaires :</h4>
                <ul>
                    <li><strong>Tarif horaire :</strong> Prix de base par heure de stationnement</li>
                    <li><strong>Réduction VIP :</strong> Pourcentage de réduction pour les clients VIP</li>
                    <li><strong>Réduction Abonné :</strong> Réduction supplémentaire pour les abonnés</li>
                </ul>
                <p><strong>Note :</strong> Les abonnés actifs stationnent gratuitement pendant leur période d'abonnement.</p>
            </div>
        </div>
    </div>
</body>
</html>