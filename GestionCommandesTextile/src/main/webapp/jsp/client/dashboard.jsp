<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace Client - Gestion Textile</title>
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
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
        }
        
        .nav-links a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
            padding: 5px 10px;
            border-radius: 5px;
            position: relative;
        }
        
        .nav-links a:hover {
            background: rgba(255,255,255,0.2);
        }
        
        .notification-badge {
            background: #f56565;
            color: white;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 0.7em;
            position: absolute;
            top: -8px;
            right: -8px;
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .welcome-card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            margin-bottom: 30px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
            font-size: 2em;
            font-weight: bold;
            color: #667eea;
        }
        
        .commandes-table {
            background: white;
            border-radius: 10px;
            overflow-x: auto;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background: #f7fafc;
        }
        
        .statut {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.85em;
        }
        
        .statut-EN_ATTENTE { background: #fef5e7; color: #f39c12; }
        .statut-EN_PRODUCTION { background: #e3f2fd; color: #2196f3; }
        .statut-TERMINE { background: #e8f5e9; color: #4caf50; }
        
        .btn {
            padding: 5px 15px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 0.85em;
        }
        
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                text-align: center;
            }
            
            .nav-links {
                margin-top: 10px;
            }
            
            th, td {
                padding: 8px 10px;
                font-size: 0.85em;
            }
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h2>👤 Espace Client</h2>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/client/dashboard">Accueil</a>
            <a href="${pageContext.request.contextPath}/client/commandes">Mes commandes</a>
            <a href="${pageContext.request.contextPath}/client/notifications">
                🔔 Notifications
                <c:if test="${nbNotifications > 0}">
                    <span class="notification-badge">${nbNotifications}</span>
                </c:if>
            </a>
            <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
        </div>
    </div>
    
    <div class="container">
        <div class="welcome-card">
            <h2>👋 Bonjour, ${sessionScope.nom} !</h2>
            <p>Bienvenue dans votre espace client. Suivez l'avancement de vos commandes en temps réel.</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number">${commandes.size()}</div>
                <div>Total commandes</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">
                    <c:set var="prod" value="0"/>
                    <c:forEach items="${commandes}" var="c">
                        <c:if test="${c.statut == 'EN_PRODUCTION'}">
                            <c:set var="prod" value="${prod + 1}"/>
                        </c:if>
                    </c:forEach>
                    ${prod}
                </div>
                <div>En production</div>
            </div>
            <div class="stat-card">
                <div class="stat-number">
                    <c:set var="term" value="0"/>
                    <c:forEach items="${commandes}" var="c">
                        <c:if test="${c.statut == 'TERMINE'}">
                            <c:set var="term" value="${term + 1}"/>
                        </c:if>
                    </c:forEach>
                    ${term}
                </div>
                <div>Terminées</div>
            </div>
        </div>
        
        <h3>📋 Dernières commandes</h3>
        <div class="commandes-table">
            <table>
                <thead>
                    <tr>
                        <th>N°</th>
                        <th>Article</th>
                        <th>Quantité</th>
                        <th>Date commande</th>
                        <th>Livraison prévue</th>
                        <th>Statut</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${commandes}" var="commande" begin="0" end="4">
                        <tr>
                            <td>#${commande.id}</td>
                            <td>${commande.typeArticle}</td>
                            <td>${commande.quantite}</td>
                            <td><fmt:formatDate value="${commande.dateCommande}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${commande.dateLivraisonPrevue}" pattern="dd/MM/yyyy"/></td>
                            <td><span class="statut statut-${commande.statut}">${commande.statut}</span></td>
                            <td><a href="${pageContext.request.contextPath}/client/commande/details?id=${commande.id}" class="btn">Suivre</a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty commandes}">
                        <tr><td colspan="7" style="text-align: center;">Aucune commande trouvée</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>