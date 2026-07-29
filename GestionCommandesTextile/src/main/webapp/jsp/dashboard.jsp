<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de Bord - Gestion Textile</title>
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
        
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
        }
        
        .navbar a {
            color: white;
            text-decoration: none;
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        h2 {
            color: #333;
            margin-bottom: 20px;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
        }
        
        .stat-number {
            font-size: 3em;
            font-weight: bold;
            color: #667eea;
        }
        
        .stat-label {
            color: #666;
            margin-top: 10px;
            font-size: 1.1em;
        }
        
        .stat-icon {
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 10px;
        }
        
        @media (max-width: 768px) {
            .stats-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="navbar">
        <a href="${pageContext.request.contextPath}/">← Accueil</a>
    </div>
    
    <div class="container">
        <h2>📊 Tableau de bord - Production</h2>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">📋</div>
                <div class="stat-number">${dashboard.totalCommandes}</div>
                <div class="stat-label">Total commandes</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">⏳</div>
                <div class="stat-number">${dashboard.commandesEnAttente}</div>
                <div class="stat-label">En attente</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">⚙️</div>
                <div class="stat-number">${dashboard.commandesEnProduction}</div>
                <div class="stat-label">En production</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">✅</div>
                <div class="stat-number">${dashboard.commandesTerminees}</div>
                <div class="stat-label">Terminées</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">⚠️</div>
                <div class="stat-number">${dashboard.commandesEnRetard}</div>
                <div class="stat-label">En retard</div>
            </div>
        </div>
        
        <div class="card">
            <h3>📈 Actions rapides</h3>
            <a href="${pageContext.request.contextPath}/commande/nouvelle" class="btn">➕ Créer une commande</a>
            <a href="${pageContext.request.contextPath}/commande/" class="btn">📋 Voir toutes les commandes</a>
        </div>
    </div>
</body>
</html>