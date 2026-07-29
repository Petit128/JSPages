<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon espace - TaxiFlow</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f5f7fa; }
        
        .header {
            background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center;
            position: sticky; top: 0; z-index: 100;
        }
        .logo { font-size: 1.5rem; font-weight: 800; color: #667eea; display: flex; align-items: center; gap: 10px; }
        .container { max-width: 1200px; margin: 2rem auto; padding: 0 2rem; }
        
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: white; border-radius: 20px; padding: 1.5rem;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); display: flex; align-items: center; gap: 1rem;
        }
        .stat-icon {
            width: 60px; height: 60px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 15px; display: flex; align-items: center; justify-content: center; color: white; font-size: 1.8rem;
        }
        .stat-info { flex: 1; }
        .stat-value { font-size: 1.8rem; font-weight: 800; color: #333; }
        .stat-label { color: #666; font-size: 0.85rem; }
        
        .section-title {
            font-size: 1.3rem; font-weight: 700; margin-bottom: 1rem;
            display: flex; align-items: center; gap: 10px;
        }
        .table-card {
            background: white; border-radius: 20px; padding: 1.5rem;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 2rem;
            overflow-x: auto;
        }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #f0f0f0; }
        th { background: #f8f9fa; font-weight: 600; }
        
        .btn-request {
            background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%);
            color: white; border: none; padding: 12px 24px; border-radius: 10px;
            font-size: 1rem; font-weight: 600; cursor: pointer; text-decoration: none;
            display: inline-block; transition: transform 0.3s;
        }
        .btn-request:hover { transform: translateY(-2px); }
        .btn-logout {
            background: #f44336; color: white; border: none; padding: 8px 16px;
            border-radius: 8px; cursor: pointer; margin-left: 1rem;
        }
        .badge {
            padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600;
        }
        .badge-success { background: #e8f5e9; color: #4caf50; }
        .badge-warning { background: #fff3e0; color: #ff9800; }
        .badge-info { background: #e3f2fd; color: #2196f3; }
        
        @media (max-width: 768px) {
            .header { flex-direction: column; gap: 1rem; }
            .stats-grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        com.taxis.model.Client client = (com.taxis.model.Client) session.getAttribute("client");
        if(client == null) {
            response.sendRedirect(contextPath + "/client/login");
            return;
        }
    %>
    
    <header class="header">
        <div class="logo"><i class="fas fa-taxi"></i> TaxiFlow - Espace Client</div>
        <div>
            <i class="fas fa-user-circle"></i> <%= client.getPrenom() %> <%= client.getNom() %>
            <button class="btn-logout" onclick="location.href='<%= contextPath %>/logout'"><i class="fas fa-sign-out-alt"></i> Déconnexion</button>
        </div>
    </header>
    
    <div class="container">
        <!-- Statistiques -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-chart-line"></i></div>
                <div class="stat-info">
                    <div class="stat-value"><%= client.getNombreCourses() %></div>
                    <div class="stat-label">Courses effectuées</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-euro-sign"></i></div>
                <div class="stat-info">
                    <div class="stat-value"><fmt:formatNumber value="<%= client.getTotalDepenses() %>" pattern="#,##0"/> Ar</div>
                    <div class="stat-label">Total dépensé</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-calendar-day"></i></div>
                <div class="stat-info">
                    <div class="stat-value"><%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(client.getDateInscription()) %></div>
                    <div class="stat-label">Membre depuis</div>
                </div>
            </div>
        </div>
        
        <!-- Actions -->
        <div style="margin-bottom: 2rem;">
            <a href="${pageContext.request.contextPath}/client/request" class="btn-request">
                <i class="fas fa-taxi"></i> Commander un taxi
            </a>
        </div>
        
        <!-- Historique des courses -->
        <div class="table-card">
            <div class="section-title">
                <i class="fas fa-history"></i> Historique de mes courses
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Date</th>
                        <th>Départ</th>
                        <th>Arrivée</th>
                        <th>Distance</th>
                        <th>Prix</th>
                        <th>Statut</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td>#${course.id}</td>
                            <td>${course.dateHeure}</td>
                            <td>${course.adresseDepart}</td>
                            <td>${course.adresseArrivee}</td>
                            <td>${course.distance} km</td>
                            <td><fmt:formatNumber value="${course.prix}" pattern="#,##0"/> Ar</td>
                            <td>
                                <c:choose>
                                    <c:when test="${course.statut == 'TERMINEE'}"><span class="badge badge-success">Terminée</span></c:when>
                                    <c:when test="${course.statut == 'EN_COURS'}"><span class="badge badge-warning">En cours</span></c:when>
                                    <c:otherwise><span class="badge badge-info">En attente</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty courses}">
                        <td><td colspan="7" style="text-align: center;">Aucune course pour le moment</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>