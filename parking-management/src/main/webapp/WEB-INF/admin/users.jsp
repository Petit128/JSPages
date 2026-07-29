<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des utilisateurs - Admin</title>
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
        .btn-delete { background: #dc3545; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
        .message { background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 20px; }
        .badge-admin { background: #dc3545; color: white; padding: 2px 8px; border-radius: 3px; font-size: 11px; }
        .badge-agent { background: #ffc107; color: #333; padding: 2px 8px; border-radius: 3px; font-size: 11px; }
        .badge-client { background: #28a745; color: white; padding: 2px 8px; border-radius: 3px; font-size: 11px; }
    </style>
</head>
<body>
    <%
        User admin = (User) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            response.sendRedirect("../login");
            return;
        }
        List<User> users = (List<User>) request.getAttribute("allUsers");
    %>
    <div class="header">
        <h1>👥 Gestion des utilisateurs</h1>
        <a href="../logout" style="color:white">Déconnexion</a>
    </div>
    <div class="container">
        <a href="dashboard" class="back-link">← Retour</a>
        
        <% if (session.getAttribute("message") != null) { %>
            <div class="message"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        
        <div class="card">
            <h3>📋 Liste des utilisateurs</h3>
            <table>
                <thead>
                    <tr><th>ID</th><th>Nom complet</th><th>Email</th><th>Téléphone</th><th>Rôle</th><th>Type</th><th>Plaque</th><th>Action</th></tr>
                </thead>
                <tbody>
                    <% for (User u : users) { %>
                        <tr>
                            <td><%= u.getId() %></td>
                            <td><%= u.getFullName() %></td>
                            <td><%= u.getEmail() %></td>
                            <td><%= u.getPhone() != null ? u.getPhone() : "-" %></td>
                            <td><span class="badge-<%= u.getRole().toLowerCase() %>"><%= u.getRole() %></span></td>
                            <td><%= u.getUserType() %></td>
                            <td><%= u.getVehiclePlate() != null ? u.getVehiclePlate() : "-" %></td>
                            <td>
                                <% if (!"ADMIN".equals(u.getRole())) { %>
                                    <form method="post" style="display:inline">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="<%= u.getId() %>">
                                        <button type="submit" class="btn-delete" onclick="return confirm('Supprimer cet utilisateur ?')">🗑️</button>
                                    </form>
                                <% } else { %> - <% } %>
                             </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>