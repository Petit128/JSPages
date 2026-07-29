<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaxiFlow - Gestion des Courses</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f5f7fa; }
        .header { background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1); position: sticky; top: 0; z-index: 1000; }
        .nav-container { max-width: 1400px; margin: 0 auto; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; }
        .logo { display: flex; align-items: center; gap: 10px; font-size: 1.5rem; font-weight: 800; color: #667eea; text-decoration: none; }
        .nav-links { display: flex; gap: 2rem; list-style: none; }
        .nav-links a { text-decoration: none; color: #666; font-weight: 500; transition: color 0.3s; display: flex; align-items: center; gap: 8px; }
        .nav-links a:hover, .nav-links a.active { color: #667eea; }
        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem; }
        .page-title { font-size: 2rem; font-weight: 700; color: #333; }
        .btn-add { background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%); color: white; border: none; padding: 12px 24px; border-radius: 10px; font-size: 1rem; font-weight: 600; display: flex; align-items: center; gap: 8px; text-decoration: none; transition: transform 0.3s; }
        .btn-add:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(33,150,243,0.4); }
        .table-container { background: white; border-radius: 15px; overflow-x: auto; box-shadow: 0 5px 20px rgba(0,0,0,0.05); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #f0f0f0; }
        th { background: #f8f9fa; font-weight: 600; }
        tr:hover { background: #f9f9f9; }
        .badge { padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600; display: inline-block; }
        .badge-en-attente { background: #e3f2fd; color: #1976d2; }
        .badge-assignee { background: #fff3e0; color: #ff9800; }
        .badge-encours { background: #fce4ec; color: #f44336; }
        .badge-terminee { background: #e8f5e9; color: #4caf50; }
        .btn-action { padding: 6px 12px; border: none; border-radius: 6px; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; gap: 5px; font-size: 0.8rem; margin: 2px; transition: all 0.3s; }
        .btn-assign { background: #ff9800; color: white; }
        .btn-edit { background: #2196f3; color: white; }
        .btn-start { background: #2196f3; color: white; }
        .btn-terminer { background: #4caf50; color: white; }
        .btn-delete { background: #f44336; color: white; }
        .btn-action:hover { transform: translateY(-2px); filter: brightness(0.9); }
        .action-buttons { display: flex; gap: 5px; flex-wrap: wrap; }
        @media (max-width: 768px) { .nav-container { flex-direction: column; gap: 1rem; } .nav-links { flex-wrap: wrap; justify-content: center; } }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
    %>
    <header class="header">
        <div class="nav-container">
            <a href="<%= contextPath %>/" class="logo"><i class="fas fa-taxi"></i><span>TaxiFlow</span></a>
            <ul class="nav-links">
                <li><a href="<%= contextPath %>/"><i class="fas fa-home"></i> Accueil</a></li>
                <li><a href="<%= contextPath %>/chauffeurs"><i class="fas fa-users"></i> Chauffeurs</a></li>
                <li><a href="<%= contextPath %>/vehicules"><i class="fas fa-car"></i> Véhicules</a></li>
                <li><a href="<%= contextPath %>/courses" class="active"><i class="fas fa-map-marker-alt"></i> Courses</a></li>
                <li><a href="<%= contextPath %>/statistiques"><i class="fas fa-chart-line"></i> Statistiques</a></li>
            </ul>
        </div>
    </header>

    <div class="container">
        <div class="header-actions">
            <h1 class="page-title"><i class="fas fa-map-marker-alt" style="color: #2196f3;"></i> Gestion des Courses</h1>
            <a href="<%= contextPath %>/courses?action=new" class="btn-add"><i class="fas fa-plus"></i> Nouvelle course</a>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th><th>Client</th><th>Départ</th><th>Arrivée</th>
                        <th>Date</th><th>Distance</th><th>Prix</th><th>Statut</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="course" items="${courses}">
                        <tr>
                            <td>${course.id}</td>
                            <td><strong>${course.clientNom}</strong><br><small>${course.clientTelephone}</small></td>
                            <td>${course.adresseDepart}</td>
                            <td>${course.adresseArrivee}</td>
                            <td>${course.dateHeure}</td>
                            <td>${course.distance} km</br><small>Chauf: ${course.chauffeurId}</small></td>
                            <td class="course-price"><fmt:formatNumber value="${course.prix}" pattern="#,##0"/> Ar</td>
                            <td>
                                <c:choose>
                                    <c:when test="${course.statut == 'EN_ATTENTE'}"><span class="badge badge-en-attente">En attente</span></c:when>
                                    <c:when test="${course.statut == 'ASSIGNEE'}"><span class="badge badge-assignee">Assignée</span></c:when>
                                    <c:when test="${course.statut == 'EN_COURS'}"><span class="badge badge-encours">En cours</span></c:when>
                                    <c:when test="${course.statut == 'TERMINEE'}"><span class="badge badge-terminee">Terminée</span></c:when>
                                    <c:otherwise><span class="badge">${course.statut}</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="action-buttons">
                                <!-- Assigner (toujours visible pour toutes les courses) -->
                                <a href="<%= contextPath %>/courses?action=assign&id=${course.id}" class="btn-action btn-assign">
                                    <i class="fas fa-user-check"></i> Assigner
                                </a>
                                
                                <!-- Modifier -->
                                <a href="<%= contextPath %>/courses?action=edit&id=${course.id}" class="btn-action btn-edit">
                                    <i class="fas fa-edit"></i> Modifier
                                </a>
                                
                                <!-- Démarrer -->
                                <a href="<%= contextPath %>/courses?action=demarrer&id=${course.id}" class="btn-action btn-start" onclick="return confirm('Démarrer cette course ?')">
                                    <i class="fas fa-play"></i> Démarrer
                                </a>
                                
                                <!-- Terminer -->
                                <a href="<%= contextPath %>/courses?action=terminer&id=${course.id}" class="btn-action btn-terminer" onclick="return confirm('Terminer cette course ?')">
                                    <i class="fas fa-flag-checkered"></i> Terminer
                                </a>
                                
                                <!-- Supprimer -->
                                <a href="<%= contextPath %>/courses?action=delete&id=${course.id}" class="btn-action btn-delete" onclick="return confirm('Supprimer cette course ?')">
                                    <i class="fas fa-trash"></i> Supprimer
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty courses}">
                        <tr><td colspan="9" style="text-align: center; padding: 3rem;">Aucune course enregistrée</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>