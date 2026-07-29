<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String role = (String) session.getAttribute("role");
    String nom = (String) session.getAttribute("nom");
%>

<style>
    .navbar {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 15px 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    
    .navbar-brand {
        font-size: 1.3em;
        font-weight: bold;
    }
    
    .navbar-brand a {
        color: white;
        text-decoration: none;
    }
    
    .nav-links {
        display: flex;
        align-items: center;
        gap: 15px;
        flex-wrap: wrap;
    }
    
    .nav-links a {
        color: white;
        text-decoration: none;
        padding: 8px 15px;
        border-radius: 5px;
        transition: background 0.3s;
    }
    
    .nav-links a:hover {
        background: rgba(255,255,255,0.2);
    }
    
    .user-info {
        display: flex;
        align-items: center;
        gap: 10px;
        background: rgba(255,255,255,0.15);
        padding: 5px 15px;
        border-radius: 20px;
    }
    
    .logout-btn {
        background: #f56565;
    }
    
    .logout-btn:hover {
        background: #e53e3e;
    }
    
    @media (max-width: 768px) {
        .navbar {
            flex-direction: column;
            text-align: center;
            gap: 10px;
        }
        
        .nav-links {
            justify-content: center;
        }
    }
</style>

<div class="navbar">
    <div class="navbar-brand">
        <a href="${pageContext.request.contextPath}/">🏭 Gestion Textile</a>
    </div>
    
    <div class="nav-links">
        <c:choose>
            <c:when test="${role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/utilisateurs">Utilisateurs</a>
                <a href="${pageContext.request.contextPath}/admin/parametres">Paramètres</a>
                <a href="${pageContext.request.contextPath}/commande/">Commandes</a>
            </c:when>
            <c:when test="${role == 'RESPONSABLE_PRODUCTION' or role == 'OPERATEUR'}">
                <a href="${pageContext.request.contextPath}/commande/">Commandes</a>
                <a href="${pageContext.request.contextPath}/production/dashboard">Production</a>
            </c:when>
            <c:when test="${role == 'CLIENT'}">
                <a href="${pageContext.request.contextPath}/client/dashboard">Accueil</a>
                <a href="${pageContext.request.contextPath}/client/commandes">Mes commandes</a>
                <a href="${pageContext.request.contextPath}/client/notifications">Notifications</a>
            </c:when>
        </c:choose>
        
        <div class="user-info">
            <span>👤 ${nom}</span>
            <span class="badge">${role}</span>
        </div>
        
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">🚪 Déconnexion</a>
    </div>
</div>