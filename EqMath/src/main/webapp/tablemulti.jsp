<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Table de multiplication</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial; margin: 50px; }
        table { border-collapse: collapse; }
        td { padding: 8px; border: 1px solid #ddd; }
        td:first-child { font-weight: bold; background-color: #f0f0f0; }
    </style>
</head>
<body>
    <h2>Table de multiplication</h2>
    
    <%
        try {
            int nombre = Integer.parseInt(request.getParameter("nombre"));
    %>
    
    <h3>Table de <%= nombre %></h3>
    <table>
        <% for (int i = 1; i <= 10; i++) { %>
        <tr>
            <td><%= nombre %> x <%= i %></td>
            <td><%= nombre * i %></td>
        </tr>
        <% } %>
    </table>
    
    <%
        } catch (NumberFormatException e) {
    %>
    <p style="color: red;">Erreur : Veuillez saisir un nombre valide</p>
    <%
        }
    %>
    
    <br>
    <a href="tablemulti.html">Nouvelle table</a>
    <br>
    <a href="index.html">Retour au menu</a>
</body>
</html>