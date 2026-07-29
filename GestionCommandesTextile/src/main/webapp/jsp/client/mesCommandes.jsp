<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes commandes - Espace Client</title>
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
        }
        
        .nav-links a:hover {
            background: rgba(255,255,255,0.2);
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background 0.3s;
        }
        
        .btn:hover {
            background: #5a67d8;
        }
        
        .btn-secondary {
            background: #48bb78;
        }
        
        .btn-secondary:hover {
            background: #38a169;
        }
        
        .table-container {
            background: white;
            border-radius: 10px;
            overflow-x: auto;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e2e8f0;
        }
        
        th {
            background: #f7fafc;
            color: #4a5568;
            font-weight: 600;
        }
        
        tr:hover {
            background: #f7fafc;
        }
        
        .statut {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: bold;
        }
        
        .statut-EN_ATTENTE {
            background: #fef5e7;
            color: #f39c12;
        }
        
        .statut-EN_PRODUCTION {
            background: #e3f2fd;
            color: #2196f3;
        }
        
        .statut-TERMINE {
            background: #e8f5e9;
            color: #4caf50;
        }
        
        .statut-LIVRE {
            background: #e0f2fe;
            color: #0284c7;
        }
        
        .btn-sm {
            padding: 5px 12px;
            font-size: 0.85em;
        }
        
        .badge {
            background: #ed8936;
            color: white;
            padding: 2px 8px;
            border-radius: 20px;
            font-size: 0.75em;
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
            
            .header {
                flex-direction: column;
                gap: 15px;
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
            <a href="${pageContext.request.contextPath}/client/notifications">Notifications</a>
            <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
        </div>
    </div>
    
    <div class="container">
        <div class="header">
            <h2>📋 Mes commandes</h2>
            <a href="${pageContext.request.contextPath}/commande/nouvelle" class="btn btn-secondary">➕ Nouvelle commande</a>
        </div>
        
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>N° Commande</th>
                        <th>Article</th>
                        <th>Quantité</th>
                        <th>Taille/Couleur</th>
                        <th>Date commande</th>
                        <th>Livraison prévue</th>
                        <th>Statut</th>
                        <th>Avancement</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${commandes}" var="commande">
                        <tr>
                            <td>#${commande.id}</td>
                            <td>${commande.typeArticle}</td>
                            <td>${commande.quantite}</td>
                            <td>${commande.taille} / ${commande.couleur}</td>
                            <td><fmt:formatDate value="${commande.dateCommande}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${commande.dateLivraisonPrevue}" pattern="dd/MM/yyyy"/></td>
                            <td>
                                <span class="statut statut-${commande.statut}">
                                    <c:choose>
                                        <c:when test="${commande.statut == 'EN_ATTENTE'}">En attente</c:when>
                                        <c:when test="${commande.statut == 'EN_PRODUCTION'}">En production</c:when>
                                        <c:when test="${commande.statut == 'TERMINE'}">Terminée</c:when>
                                        <c:when test="${commande.statut == 'LIVRE'}">Livrée</c:when>
                                        <c:otherwise>${commande.statut}</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td>
                                <div class="progress-bar" style="width: 100px; background: #e2e8f0; border-radius: 10px; overflow: hidden;">
                                    <div style="width: ${commande.statut == 'EN_ATTENTE' ? '25%' : commande.statut == 'EN_PRODUCTION' ? '50%' : commande.statut == 'TERMINE' ? '75%' : '100%'}; background: #667eea; color: white; text-align: center; font-size: 0.7em;">
                                        ${commande.statut == 'EN_ATTENTE' ? '25%' : commande.statut == 'EN_PRODUCTION' ? '50%' : commande.statut == 'TERMINE' ? '75%' : '100%'}
                                    </div>
                                </div>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/client/commande/details?id=${commande.id}" class="btn btn-sm">Suivre</a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty commandes}">
                        <tr>
                            <td colspan="9" style="text-align: center; padding: 40px;">
                                <p>📭 Vous n'avez pas encore de commandes</p>
                                <a href="${pageContext.request.contextPath}/commande/nouvelle" class="btn" style="margin-top: 10px; display: inline-block;">Passer une commande</a>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>