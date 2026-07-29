<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.essai.model.Tache" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ajouter une tâche</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial; margin: 50px; }
        input { padding: 5px; }
    </style>
</head>
<body>
    <h2>Ajouter une nouvelle tâche</h2>
    
    <form action="ajout.jsp" method="post">
        <label>Description : <input type="text" name="description" required></label>
        <br><br>
        <input type="submit" value="Ajouter">
    </form>
    
    <%
        String description = request.getParameter("description");
        if (description != null && !description.trim().isEmpty()) {
            List<Tache> taches = (List<Tache>) session.getAttribute("taches");
            if (taches == null) {
                taches = new ArrayList<Tache>();
                session.setAttribute("taches", taches);
            }
            taches.add(new Tache(description));
            response.sendRedirect("liste.jsp");
        }
    %>
    
    <br>
    <a href="todolist.html">Retour au menu To Do List</a>
</body>
</html>