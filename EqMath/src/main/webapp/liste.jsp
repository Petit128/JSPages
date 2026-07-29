<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.essai.model.Tache" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des tâches</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial; margin: 50px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f0f0f0; }
        tr:nth-child(even) { background-color: #f9f9f9; }
    </style>
</head>
<body>
    <h2>Liste des tâches</h2>
    
    <%
        List<Tache> taches = (List<Tache>) session.getAttribute("taches");
        
        if (taches == null || taches.isEmpty()) {
    %>
    <p>Aucune tâche enregistrée.</p>
    <%
        } else {
    %>
    <table>
        <tr>
            <th>Description</th>
            <th>Date de création</th>
            <th>Statut</th>
        </tr>
        <% for (Tache tache : taches) { %>
        <tr>
            <td><%= tache.getDescription() %></td>
            <td><%= tache.getDateFormatee() %></td>
            <td><%= tache.isTerminee() ? "Terminée" : "En cours" %></td>
        </tr>
        <% } %>
    </table>
    <%
        }
    %>
    
    <br>
    <a href="ajout.jsp">Ajouter une tâche</a>
    <br>
    <a href="todolist.html">Retour au menu To Do List</a>
</body>
</html>