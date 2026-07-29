<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Traitement connexion</title>
    <meta charset="UTF-8">
</head>
<body>
    <%
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Simulation d'une base de données d'utilisateurs
        Map<String, String> users = new HashMap<>();
        users.put("admin", "admin123");
        users.put("user", "password");
        users.put("test", "test");
        
        if (users.containsKey(username) && users.get(username).equals(password)) {
            // Authentification réussie
            session.setAttribute("user", username);
            
            // Gestion du compteur de connexions
            Map<String, Integer> compteurs = (Map<String, Integer>) session.getAttribute("compteurs");
            if (compteurs == null) {
                compteurs = new HashMap<>();
                session.setAttribute("compteurs", compteurs);
            }
            
            int compteur = compteurs.getOrDefault(username, 0) + 1;
            compteurs.put(username, compteur);
            
            response.sendRedirect("accueil.jsp");
        } else {
            // Échec de l'authentification
            response.sendRedirect("login.html?error=1");
        }
    %>
</body>
</html>