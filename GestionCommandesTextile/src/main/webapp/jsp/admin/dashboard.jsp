<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Gestion Textile</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .welcome {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .stat-number {
            font-size: 2.5em;
            font-weight: bold;
            color: #667eea;
        }
        
        .actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        
        .action-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            text-decoration: none;
            color: #333;
            transition: transform 0.3s;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .action-card:hover {
            transform: translateY(-5px);
        }
        
        .action-icon {
            font-size: 3em;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <jsp:include page="/jsp/include/navbar.jsp" />
    
    <div class="container">
        <div class="welcome">
            <h2>👋 Bonjour, ${sessionScope.nom} !</h2>
            <p>Bienvenue dans votre espace d'administration</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number">${dashboard.totalCommandes}</div>
                <div>Total commandes</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${dashboard.commandesEnProduction}</div>
                <div>En production</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${dashboard.commandesEnAttente}</div>
                <div>En attente</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${dashboard.commandesTerminees}</div>
                <div>Terminées</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">${dashboard.commandesEnRetard}</div>
                <div>En retard</div>
            </div>
        </div>
        
        <div class="actions">
            <a href="${pageContext.request.contextPath}/commande/nouvelle" class="action-card">
                <div class="action-icon">➕</div>
                <h3>Nouvelle commande</h3>
                <p>Créer une commande client</p>
            </a>
            <a href="${pageContext.request.contextPath}/commande/" class="action-card">
                <div class="action-icon">📋</div>
                <h3>Liste des commandes</h3>
                <p>Consulter toutes les commandes</p>
            </a>
            <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="action-card">
                <div class="action-icon">👥</div>
                <h3>Gestion utilisateurs</h3>
                <p>Ajouter/modifier des utilisateurs</p>
            </a>
            <a href="${pageContext.request.contextPath}/admin/parametres" class="action-card">
                <div class="action-icon">⚙️</div>
                <h3>Paramètres</h3>
                <p>Configurer l'application</p>
            </a>
        </div>
    </div>
</body>
</html>