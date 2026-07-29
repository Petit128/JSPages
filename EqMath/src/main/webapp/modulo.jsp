<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Tables dans Z/nZ</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial; margin: 50px; }
        table { border-collapse: collapse; margin: 20px 0; }
        td { padding: 8px; border: 1px solid #ddd; text-align: center; }
        td:first-child, tr:first-child td { font-weight: bold; background-color: #f0f0f0; }
        .section { margin-bottom: 40px; }
    </style>
</head>
<body>
    <%
        try {
            int n = Integer.parseInt(request.getParameter("n"));
            if (n <= 0) throw new NumberFormatException();
    %>
    
    <h2>Tables dans Z/<%= n %>Z</h2>
    
    <div class="section">
        <h3>Table d'addition</h3>
        <table>
            <tr>
                <td>+</td>
                <% for (int i = 0; i < n; i++) { %>
                <td><%= i %></td>
                <% } %>
            </tr>
            <% for (int i = 0; i < n; i++) { %>
            <tr>
                <td><%= i %></td>
                <% for (int j = 0; j < n; j++) { %>
                <td><%= (i + j) % n %></td>
                <% } %>
            </tr>
            <% } %>
        </table>
    </div>
    
    <div class="section">
        <h3>Table de multiplication</h3>
        <table>
            <tr>
                <td>×</td>
                <% for (int i = 0; i < n; i++) { %>
                <td><%= i %></td>
                <% } %>
            </tr>
            <% for (int i = 0; i < n; i++) { %>
            <tr>
                <td><%= i %></td>
                <% for (int j = 0; j < n; j++) { %>
                <td><%= (i * j) % n %></td>
                <% } %>
            </tr>
            <% } %>
        </table>
    </div>
    
    <%
        } catch (Exception e) {
    %>
    <p style="color: red;">Erreur : Veuillez saisir un entier valide > 0</p>
    <%
        }
    %>
    
    <br>
    <a href="modulo.html">Nouveau calcul</a>
    <br>
    <a href="index.html">Retour au menu</a>
</body>
</html>