<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes notifications - Espace Client</title>
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
            max-width: 900px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .header {
            margin-bottom: 20px;
        }
        
        .notification-list {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .notification-item {
            padding: 15px 20px;
            border-bottom: 1px solid #e2e8f0;
            transition: background 0.3s;
            cursor: pointer;
        }
        
        .notification-item:hover {
            background: #f7fafc;
        }
        
        .notification-item.non-lue {
            background: #ebf8ff;
            border-left: 4px solid #667eea;
        }
        
        .notification-type {
            display: inline-block;
            padding: 2px 8px;
            border-radius: 4px;
            font-size: 0.75em;
            font-weight: bold;
            margin-bottom: 8px;
        }
        
        .type-ETAPE_TERMINEE { background: #48bb78; color: white; }
        .type-ETAPE_DEBUTE { background: #4299e1; color: white; }
        .type-PRODUCTION_DEBUT { background: #ed8936; color: white; }
        .type-RETARD_ALERTE { background: #f56565; color: white; }
        .type-COMMANDE_CREEE { background: #9f7aea; color: white; }
        
        .notification-message {
            font-size: 1em;
            color: #2d3748;
            margin-bottom: 8px;
        }
        
        .notification-date {
            font-size: 0.8em;
            color: #718096;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #718096;
        }
        
        .empty-state-icon {
            font-size: 4em;
            margin-bottom: 20px;
        }
        
        .btn-marquer {
            background: none;
            border: none;
            color: #667eea;
            cursor: pointer;
            font-size: 0.8em;
            margin-top: 5px;
        }
        
        .btn-marquer:hover {
            text-decoration: underline;
        }
        
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                text-align: center;
            }
            
            .nav-links {
                margin-top: 10px;
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
            <h2>🔔 Mes notifications</h2>
        </div>
        
        <div class="notification-list">
            <c:forEach items="${notifications}" var="notif">
                <div class="notification-item ${notif.lue ? '' : 'non-lue'}" 
                     onclick="location.href='${pageContext.request.contextPath}/client/commande/details?id=${notif.commandeId}'">
                    <div>
                        <span class="notification-type type-${notif.type}">
                            <c:choose>
                                <c:when test="${notif.type == 'ETAPE_TERMINEE'}">✅ Étape terminée</c:when>
                                <c:when test="${notif.type == 'ETAPE_DEBUTE'}">🔄 Étape débutée</c:when>
                                <c:when test="${notif.type == 'PRODUCTION_DEBUT'}">🏭 Production démarrée</c:when>
                                <c:when test="${notif.type == 'RETARD_ALERTE'}">⚠️ Alerte retard</c:when>
                                <c:otherwise>📢 Information</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="notification-message">
                        ${notif.message}
                    </div>
                    <div class="notification-date">
                        📅 <fmt:formatDate value="${notif.dateEnvoi}" pattern="dd/MM/yyyy HH:mm"/>
                        <c:if test="${!notif.lue}">
                            <form action="${pageContext.request.contextPath}/api/marquerNotificationLue" method="post" style="display: inline;">
                                <input type="hidden" name="id" value="${notif.id}">
                                <button type="submit" class="btn-marquer">✓ Marquer comme lue</button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty notifications}">
                <div class="empty-state">
                    <div class="empty-state-icon">🔔</div>
                    <h3>Aucune notification</h3>
                    <p>Vous serez notifié des mises à jour de vos commandes</p>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>