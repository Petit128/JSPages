<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Commandes - Gestion Textile</title>
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
        
        .navbar h1 {
            font-size: 1.5em;
        }
        
        .nav-links a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
            padding: 5px 10px;
            border-radius: 5px;
            transition: background 0.3s;
        }
        
        .nav-links a:hover {
            background: rgba(255,255,255,0.2);
        }
        
        .container {
            max-width: 1400px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-primary:hover {
            background: #5a67d8;
            transform: translateY(-2px);
        }
        
        .btn-success {
            background: #48bb78;
            color: white;
        }
        
        .btn-warning {
            background: #ed8936;
            color: white;
        }
        
        .btn-danger {
            background: #f56565;
            color: white;
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
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .btn-sm {
            padding: 5px 10px;
            font-size: 0.85em;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                text-align: center;
            }
            
            .nav-links {
                margin-top: 10px;
            }
            
            .header {
                flex-direction: column;
                gap: 15px;
            }
            
            th, td {
                padding: 8px 10px;
                font-size: 0.9em;
            }
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>🏭 Gestion Commandes Textile</h1>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/">Accueil</a>
            <a href="${pageContext.request.contextPath}/commande/">Commandes</a>
            <a href="${pageContext.request.contextPath}/commande/dashboard">Dashboard</a>
        </div>
    </div>
    
    <div class="container">
        <div class="header">
            <h2>📋 Liste des Commandes</h2>
            <a href="${pageContext.request.contextPath}/commande/nouvelle" class="btn btn-primary">➕ Nouvelle commande</a>
        </div>
        
        <c:if test="${not empty sessionScope.succes}">
            <div class="alert alert-success">
                ✅ ${sessionScope.succes}
                <% session.removeAttribute("succes"); %>
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.erreur}">
            <div class="alert alert-error">
                ❌ ${sessionScope.erreur}
                <% session.removeAttribute("erreur"); %>
            </div>
        </c:if>
        
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Client</th>
                        <th>Article</th>
                        <th>Quantité</th>
                        <th>Taille/Couleur</th>
                        <th>Date Commande</th>
                        <th>Livraison Prévue</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${commandes}" var="commande">
                        <tr>
                            <td>${commande.id}</td>
                            <td>${commande.client}</td>
                            <td>${commande.typeArticle}</td>
                            <td>${commande.quantite}</td>
                            <td>${commande.taille} / ${commande.couleur}</td>
                            <td><fmt:formatDate value="${commande.dateCommande}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${commande.dateLivraisonPrevue}" pattern="dd/MM/yyyy"/></td>
                            <td>
                                <span class="statut statut-${commande.statut}">
                                    ${commande.statut}
                                </span>
                            </td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/commande/details?id=${commande.id}" class="btn btn-sm btn-primary">Détails</a>
                                <a href="${pageContext.request.contextPath}/commande/modifier?id=${commande.id}" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="${pageContext.request.contextPath}/commande/supprimer?id=${commande.id}" class="btn btn-sm btn-danger" 
                                   onclick="return confirm('Confirmer la suppression ?')">Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty commandes}">
                        <tr>
                            <td colspan="9" style="text-align: center;">Aucune commande trouvée</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>