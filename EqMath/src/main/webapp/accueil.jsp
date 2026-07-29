<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Page d'accueil</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial; margin: 50px; }
        .welcome { color: green; font-size: 1.2em; }
        table { border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f0f0f0; }
    </style>
</head>
<body>
    <%
        String user = (String) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        Map<String, Integer> compteurs = (Map<String, Integer>) session.getAttribute("compteurs");
        int compteur = compteurs != null ? compteurs.getOrDefault(user, 0) : 0;
    %>
    
    <h2 class="welcome">Bienvenue <%= user %> !</h2>
    <p>Vous vous êtes connecté <%= compteur %> fois à cette application.</p>
    
    <h3>Historique des connexions de la session :</h3>
    
    <% if (compteurs != null && !compteurs.isEmpty()) { %>
    <table>
        <tr>
            <th>Utilisateur</th>
            <th>Nombre de connexions</th>
        </tr>
        <% for (Map.Entry<String, Integer> entry : compteurs.entrySet()) { %>
        <tr>
            <td><%= entry.getKey() %></td>
            <td><%= entry.getValue() %></td>
        </tr>
        <% } %>
    </table>
    <% } %>
    
    <br>
    <a href="logout.jsp">Déconnexion</a>
    <br>
    <a href="index.html">Retour au menu principal</a>
</body>
</html>