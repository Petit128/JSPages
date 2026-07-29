<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaxiFlow - Gestion des Chauffeurs</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f5f7fa; color: #333; }
        .header {
            background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            position: sticky; top: 0; z-index: 1000;
        }
        .nav-container {
            max-width: 1400px; margin: 0 auto; padding: 1rem 2rem;
            display: flex; justify-content: space-between; align-items: center;
        }
        .logo {
            display: flex; align-items: center; gap: 10px;
            font-size: 1.5rem; font-weight: 800; color: #667eea;
            text-decoration: none;
        }
        .nav-links {
            display: flex; gap: 2rem; list-style: none;
        }
        .nav-links a {
            text-decoration: none; color: #666; font-weight: 500;
            transition: color 0.3s; display: flex; align-items: center; gap: 8px;
        }
        .nav-links a:hover, .nav-links a.active { color: #667eea; }
        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }
        .header-actions {
            display: flex; justify-content: space-between; align-items: center;
            margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;
        }
        .page-title { font-size: 2rem; font-weight: 700; color: #333; }
        .btn-add {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; border: none; padding: 12px 24px; border-radius: 10px;
            cursor: pointer; font-size: 1rem; font-weight: 600;
            display: flex; align-items: center; gap: 8px;
            text-decoration: none; transition: transform 0.3s;
        }
        .btn-add:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(102,126,234,0.4); }
        .filters {
            display: flex; gap: 1rem; flex-wrap: wrap; margin-bottom: 2rem;
        }
        .filter-btn {
            padding: 8px 16px; border: 2px solid #e0e0e0;
            background: white; border-radius: 8px; cursor: pointer;
            transition: all 0.3s; font-weight: 500;
        }
        .filter-btn:hover, .filter-btn.active { border-color: #667eea; color: #667eea; }
        .table-container {
            background: white; border-radius: 15px; overflow: hidden;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05); overflow-x: auto;
        }
        table { width: 100%; border-collapse: collapse; }
        thead { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        th { padding: 1rem; text-align: left; font-weight: 600; }
        td { padding: 1rem; border-bottom: 1px solid #f0f0f0; }
        tr:hover { background: #f9f9f9; }
        .status-badge {
            display: inline-flex; align-items: center; gap: 6px;
            padding: 4px 12px; border-radius: 20px; font-size: 0.85rem; font-weight: 600;
        }
        .status-available { background: #e8f5e9; color: #4caf50; }
        .status-unavailable { background: #ffebee; color: #f44336; }
        .status-driving { background: #fff3e0; color: #ff9800; }
        .action-buttons { display: flex; gap: 0.5rem; }
        .btn-icon {
            padding: 6px 10px; border: none; border-radius: 6px;
            cursor: pointer; text-decoration: none; display: inline-flex;
            align-items: center; gap: 5px; font-size: 0.85rem;
        }
        .btn-edit { background: #2196f3; color: white; }
        .btn-delete { background: #f44336; color: white; }
        .btn-icon:hover { transform: translateY(-2px); filter: brightness(0.9); }
        @media (max-width: 768px) {
            .nav-container { flex-direction: column; gap: 1rem; }
            .nav-links { flex-wrap: wrap; justify-content: center; }
            .header-actions { flex-direction: column; align-items: stretch; }
        }
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
                <li><a href="<%= contextPath %>/chauffeurs" class="active"><i class="fas fa-users"></i> Chauffeurs</a></li>
                <li><a href="<%= contextPath %>/vehicules"><i class="fas fa-car"></i> Véhicules</a></li>
                <li><a href="<%= contextPath %>/courses"><i class="fas fa-map-marker-alt"></i> Courses</a></li>
                <li><a href="<%= contextPath %>/statistiques"><i class="fas fa-chart-line"></i> Statistiques</a></li>
            </ul>
        </div>
    </header>

    <div class="container">
        <div class="header-actions">
            <h1 class="page-title"><i class="fas fa-users" style="color: #667eea;"></i> Gestion des Chauffeurs</h1>
            <a href="<%= contextPath %>/chauffeurs?action=new" class="btn-add"><i class="fas fa-plus"></i> Ajouter un chauffeur</a>
        </div>

        <div class="filters">
            <button class="filter-btn active" onclick="filterDrivers('all')"><i class="fas fa-list"></i> Tous</button>
            <button class="filter-btn" onclick="filterDrivers('disponible')"><i class="fas fa-check-circle"></i> Disponibles</button>
            <button class="filter-btn" onclick="filterDrivers('indisponible')"><i class="fas fa-minus-circle"></i> Indisponibles</button>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr><th>ID</th><th>Chauffeur</th><th>Contact</th><th>Permis</th><th>Statut</th><th>Date d'embauche</th><th>Revenus</th><th>Actions</th></tr>
                </thead>
                <tbody id="driversTableBody">
                    <c:forEach var="chauffeur" items="${chauffeurs}">
                        <tr data-status="${chauffeur.disponible ? 'disponible' : 'indisponible'}">
                            <td>${chauffeur.id}</td>
                            <td><strong>${chauffeur.prenom} ${chauffeur.nom}</strong></td>
                            <td><div><i class="fas fa-phone"></i> ${chauffeur.telephone}</div>
                                <div style="font-size: 0.85rem; color: #999;"><i class="fas fa-envelope"></i> ${chauffeur.email}</div></td>
                            <td>${chauffeur.permis}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${chauffeur.disponible}"><span class="status-badge status-available"><i class="fas fa-circle"></i> Disponible</span></c:when>
                                    <c:otherwise><span class="status-badge status-unavailable"><i class="fas fa-circle"></i> Indisponible</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>${chauffeur.dateEmbauche}</td>
                            <td><strong style="color: #4caf50;"><fmt:formatNumber value="${chauffeur.revenuTotal}" pattern="#,##0"/> Ar</strong></td>
                            <td><div class="action-buttons">
                                <a href="chauffeurs?action=edit&id=${chauffeur.id}" class="btn-icon btn-edit"><i class="fas fa-edit"></i></a>
                                <a href="chauffeurs?action=delete&id=${chauffeur.id}" class="btn-icon btn-delete" onclick="return confirm('Supprimer ?')"><i class="fas fa-trash"></i></a>
                            </div></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        function filterDrivers(status) {
            const rows = document.querySelectorAll('#driversTableBody tr');
            const buttons = document.querySelectorAll('.filter-btn');
            buttons.forEach(btn => btn.classList.remove('active'));
            event.target.closest('.filter-btn').classList.add('active');
            rows.forEach(row => {
                if (status === 'all') row.style.display = '';
                else row.style.display = row.getAttribute('data-status') === status ? '' : 'none';
            });
        }
    </script>
</body>
</html>