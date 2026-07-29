<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion Commandes Textile</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .header {
            text-align: center;
            color: white;
            margin-bottom: 40px;
        }
        
        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        
        .header p {
            font-size: 1.1em;
            opacity: 0.9;
        }
        
        .card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 30px;
            margin-bottom: 30px;
        }
        
        .features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .feature-card {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
            transition: transform 0.3s;
        }
        
        .feature-card:hover {
            transform: translateY(-5px);
        }
        
        .feature-card h3 {
            color: #667eea;
            margin-bottom: 10px;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 10px;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s;
            cursor: pointer;
        }
        
        .btn-primary {
            background-color: #667eea;
            color: white;
            border: none;
        }
        
        .btn-primary:hover {
            background-color: #5a67d8;
            transform: translateY(-2px);
        }
        
        .btn-secondary {
            background-color: #48bb78;
            color: white;
        }
        
        .btn-secondary:hover {
            background-color: #38a169;
        }
        
        .btn-outline {
            background: transparent;
            border: 2px solid white;
            color: white;
        }
        
        .btn-outline:hover {
            background: white;
            color: #667eea;
        }
        
        .button-group {
            text-align: center;
            margin-top: 20px;
        }
        
        .footer {
            text-align: center;
            color: white;
            margin-top: 40px;
            padding: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        
        @media (max-width: 768px) {
            .header h1 {
                font-size: 1.8em;
            }
            
            .features {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏭 Système de Gestion de Commandes Textile</h1>
            <p>Solution complète pour la gestion et le suivi de production</p>
        </div>
        
        <c:if test="${param.logout == 'success'}">
            <div class="alert-success">
                ✅ Vous avez été déconnecté avec succès !
            </div>
        </c:if>
        
        <div class="card">
            <h2 style="text-align: center; margin-bottom: 20px;">Bienvenue sur l'application</h2>
            <p style="text-align: center; margin-bottom: 30px;">Cette application permet de gérer efficacement les commandes clients et le suivi de production textile.</p>
            
            <div class="features">
                <div class="feature-card">
                    <h3>📋 Gestion des Commandes</h3>
                    <p>Ajout, modification et suivi des commandes clients</p>
                </div>
                <div class="feature-card">
                    <h3>⚙️ Suivi de Production</h3>
                    <p>Workflow automatisé : Coupe → Couture → Finition → Livraison</p>
                </div>
                <div class="feature-card">
                    <h3>🔒 Blocage Automatique</h3>
                    <p>Validation obligatoire des étapes dans l'ordre</p>
                </div>
                <div class="feature-card">
                    <h3>📊 Calcul des Délais</h3>
                    <p>Estimations et détection automatique des retards</p>
                </div>
            </div>
            
            <div class="button-group">
                <c:choose>
                    <c:when test="${not empty sessionScope.utilisateur}">
                        <c:choose>
                            <c:when test="${sessionScope.role == 'ADMIN'}">
                                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary">📊 Accéder au Dashboard</a>
                            </c:when>
                            <c:when test="${sessionScope.role == 'CLIENT'}">
                                <a href="${pageContext.request.contextPath}/client/dashboard" class="btn btn-primary">📊 Mon espace</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/commande/" class="btn btn-primary">📋 Voir les commandes</a>
                            </c:otherwise>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">🚪 Déconnexion</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">🔐 Se connecter</a>
                        <a href="${pageContext.request.contextPath}/inscription" class="btn btn-secondary">📝 S'inscrire</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <div class="footer">
            <p>© 2024 - Système de Gestion de Commandes Textile | Version 2.0</p>
        </div>
    </div>
</body>
</html>