<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Suivi commande - Espace Client</title>
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
            max-width: 1000px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .card h3 {
            color: #333;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
        }
        
        .info-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        
        .info-label {
            font-weight: bold;
            color: #555;
        }
        
        .info-value {
            color: #333;
        }
        
        .workflow {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
            margin: 20px 0;
        }
        
        .etape {
            flex: 1;
            min-width: 180px;
            background: #f8f9fa;
            border-radius: 8px;
            padding: 15px;
            text-align: center;
            position: relative;
        }
        
        .etape.terminee {
            background: #e8f5e9;
            border-left: 4px solid #4caf50;
        }
        
        .etape.en-cours {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
        }
        
        .etape.retard {
            background: #ffebee;
            border-left: 4px solid #f44336;
        }
        
        .etape h4 {
            margin-bottom: 10px;
            color: #333;
        }
        
        .etape-date {
            font-size: 0.8em;
            color: #666;
            margin-top: 8px;
        }
        
        .statut-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: bold;
            margin-top: 10px;
        }
        
        .statut-terminee { background: #4caf50; color: white; }
        .statut-en-cours { background: #2196f3; color: white; }
        .statut-en-attente { background: #ff9800; color: white; }
        
        .progress-container {
            background: #e2e8f0;
            border-radius: 10px;
            height: 20px;
            overflow: hidden;
            margin: 20px 0;
        }
        
        .progress-bar {
            background: linear-gradient(90deg, #667eea, #764ba2);
            height: 100%;
            transition: width 0.5s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 0.8em;
            font-weight: bold;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-warning {
            background: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }
        
        .btn-back {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
        }
        
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                text-align: center;
            }
            
            .nav-links {
                margin-top: 10px;
            }
            
            .workflow {
                flex-direction: column;
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
        <div style="margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/client/commandes" class="btn-back">← Retour à mes commandes</a>
        </div>
        
        <c:if test="${not empty delais.enRetard && delais.enRetard}">
            <div class="alert alert-warning">
                ⚠️ Cette commande est en retard de ${delais.retardJours} jour(s) sur la livraison prévue.
            </div>
        </c:if>
        
        <!-- Informations commande -->
        <div class="card">
            <h3>📋 Détails de la commande #${commande.id}</h3>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">Client :</span>
                    <span class="info-value">${commande.client}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Article :</span>
                    <span class="info-value">${commande.typeArticle}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Quantité :</span>
                    <span class="info-value">${commande.quantite} pièces</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Taille / Couleur :</span>
                    <span class="info-value">${commande.taille} / ${commande.couleur}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Date commande :</span>
                    <span class="info-value"><fmt:formatDate value="${commande.dateCommande}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">Livraison prévue :</span>
                    <span class="info-value"><fmt:formatDate value="${commande.dateLivraisonPrevue}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">Livraison estimée :</span>
                    <span class="info-value"><fmt:formatDate value="${delais.dateLivraisonEstimee}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">Statut :</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${commande.statut == 'EN_ATTENTE'}">📝 En attente</c:when>
                            <c:when test="${commande.statut == 'EN_PRODUCTION'}">⚙️ En production</c:when>
                            <c:when test="${commande.statut == 'TERMINE'}">✅ Terminée</c:when>
                            <c:otherwise>📦 Livrée</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>
        
        <!-- Barre de progression -->
        <div class="card">
            <h3>📊 Avancement de la production</h3>
            <c:set var="avancement" value="0"/>
            <c:forEach items="${etapes}" var="e">
                <c:if test="${e.statut == 'TERMINEE'}">
                    <c:set var="avancement" value="${avancement + 25}"/>
                </c:if>
                <c:if test="${e.statut == 'EN_COURS'}">
                    <c:set var="avancement" value="${avancement + 12.5}"/>
                </c:if>
            </c:forEach>
            <div class="progress-container">
                <div class="progress-bar" style="width: ${avancement}%;">
                    ${avancement}%
                </div>
            </div>
        </div>
        
        <!-- Workflow de production -->
        <div class="card">
            <h3>⚙️ Suivi de production</h3>
            <div class="workflow">
                <c:forEach items="${etapes}" var="etape">
                    <div class="etape 
                        <c:choose>
                            <c:when test="${etape.statut == 'TERMINEE'}">terminee</c:when>
                            <c:when test="${etape.statut == 'EN_COURS'}">en-cours</c:when>
                        </c:choose>
                        ${delais.retardsEtapes[etape.nomEtape] ? 'retard' : ''}">
                        <h4>
                            <c:choose>
                                <c:when test="${etape.nomEtape == 'COUPE'}">✂️ Coupe</c:when>
                                <c:when test="${etape.nomEtape == 'COUTURE'}">🪡 Couture</c:when>
                                <c:when test="${etape.nomEtape == 'FINITION'}">✨ Finition</c:when>
                                <c:when test="${etape.nomEtape == 'LIVRAISON'}">🚚 Livraison</c:when>
                            </c:choose>
                        </h4>
                        
                        <div class="statut-badge statut-${etape.statut == 'TERMINEE' ? 'terminee' : (etape.statut == 'EN_COURS' ? 'en-cours' : 'en-attente')}">
                            <c:choose>
                                <c:when test="${etape.statut == 'TERMINEE'}">✅ Terminée</c:when>
                                <c:when test="${etape.statut == 'EN_COURS'}">🔄 En cours</c:when>
                                <c:otherwise>⏸️ En attente</c:otherwise>
                            </c:choose>
                        </div>
                        
                        <c:if test="${etape.dateDebut != null}">
                            <div class="etape-date">
                                Début: <fmt:formatDate value="${etape.dateDebut}" pattern="dd/MM/yyyy"/>
                            </div>
                        </c:if>
                        
                        <c:if test="${etape.dateFin != null}">
                            <div class="etape-date">
                                Fin: <fmt:formatDate value="${etape.dateFin}" pattern="dd/MM/yyyy"/>
                            </div>
                        </c:if>
                        
                        <c:if test="${etape.dateFinPrevue != null && etape.statut != 'TERMINEE'}">
                            <div class="etape-date">
                                Prévue: <fmt:formatDate value="${etape.dateFinPrevue}" pattern="dd/MM/yyyy"/>
                            </div>
                        </c:if>
                        
                        <c:if test="${delais.retardsEtapes[etape.nomEtape]}">
                            <div class="etape-date" style="color: #f44336;">
                                ⚠️ En retard
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>