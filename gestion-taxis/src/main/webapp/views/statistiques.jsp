<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaxiFlow - Statistiques</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        .page-title { font-size: 2rem; font-weight: 700; color: #333; margin-bottom: 2rem; }
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: white; border-radius: 15px; padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05); transition: transform 0.3s;
        }
        .stat-card:hover { transform: translateY(-5px); }
        .stat-icon { font-size: 2rem; margin-bottom: 1rem; }
        .stat-label { font-size: 0.85rem; color: #999; margin-bottom: 0.5rem; }
        .stat-value { font-size: 2rem; font-weight: 800; color: #333; }
        .stat-sub { font-size: 0.85rem; color: #666; margin-top: 0.5rem; }
        .charts-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
            gap: 2rem; margin-bottom: 2rem;
        }
        .chart-card {
            background: white; border-radius: 15px; padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
        }
        .chart-title { font-size: 1.2rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 10px; }
        canvas { max-height: 300px; }
        .top-performers {
            background: white; border-radius: 15px; padding: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
        }
        .section-title { font-size: 1.2rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 10px; }
        .performer-list { list-style: none; }
        .performer-item {
            padding: 1rem; border-bottom: 1px solid #f0f0f0;
            display: flex; justify-content: space-between; align-items: center;
        }
        .performer-name { font-weight: 500; }
        .performer-value { font-weight: 700; color: #4caf50; }
        @media (max-width: 768px) {
            .nav-container { flex-direction: column; gap: 1rem; }
            .nav-links { flex-wrap: wrap; justify-content: center; }
            .charts-grid { grid-template-columns: 1fr; }
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
                <li><a href="<%= contextPath %>/chauffeurs"><i class="fas fa-users"></i> Chauffeurs</a></li>
                <li><a href="<%= contextPath %>/vehicules"><i class="fas fa-car"></i> Véhicules</a></li>
                <li><a href="<%= contextPath %>/courses"><i class="fas fa-map-marker-alt"></i> Courses</a></li>
                <li><a href="<%= contextPath %>/statistiques" class="active"><i class="fas fa-chart-line"></i> Statistiques</a></li>
            </ul>
        </div>
    </header>

    <div class="container">
        <h1 class="page-title"><i class="fas fa-chart-line" style="color: #667eea;"></i> Tableau de Bord - Statistiques</h1>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-chart-bar" style="color: #667eea;"></i></div>
                <div class="stat-label">Total des courses</div>
                <div class="stat-value">${stats.totalCourses}</div>
                <div class="stat-sub"><i class="fas fa-arrow-up" style="color: #4caf50;"></i> +15% ce mois</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-euro-sign" style="color: #4caf50;"></i></div>
                <div class="stat-label">Revenu total</div>
                <div class="stat-value"><fmt:formatNumber value="${stats.revenuTotal}" pattern="#,##0"/> Ar</div>
                <div class="stat-sub"><i class="fas fa-arrow-up" style="color: #4caf50;"></i> +8% vs mois dernier</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-users" style="color: #ff9800;"></i></div>
                <div class="stat-label">Chauffeurs disponibles</div>
                <div class="stat-value">${stats.chauffeursDisponibles}/${stats.chauffeursTotal}</div>
                <div class="stat-sub">Taux: <fmt:formatNumber value="${stats.tauxDisponibiliteChauffeurs}" pattern="#.#"/>%</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-car" style="color: #2196f3;"></i></div>
                <div class="stat-label">Véhicules disponibles</div>
                <div class="stat-value">${stats.vehiculesDisponibles}/${stats.vehiculesTotal}</div>
                <div class="stat-sub">Taux: <fmt:formatNumber value="${stats.tauxDisponibiliteVehicules}" pattern="#.#"/>%</div>
            </div>
        </div>

        <div class="charts-grid">
            <div class="chart-card">
                <div class="chart-title"><i class="fas fa-chart-pie"></i> Répartition des courses par statut</div>
                <canvas id="statusChart"></canvas>
            </div>
            <div class="chart-card">
                <div class="chart-title"><i class="fas fa-chart-line"></i> Évolution mensuelle des revenus (Ar)</div>
                <canvas id="revenueChart"></canvas>
            </div>
        </div>

        <div class="top-performers">
            <div class="section-title"><i class="fas fa-trophy" style="color: #ff9800;"></i> Top 5 des chauffeurs (revenus)</div>
            <ul class="performer-list" id="topDriversList"></ul>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const ctx1 = document.getElementById('statusChart').getContext('2d');
            new Chart(ctx1, {
                type: 'doughnut',
                data: { labels: ['En cours', 'Terminées', 'Planifiées'],
                        datasets: [{ data: [${stats.coursesEnCours}, ${stats.coursesTerminees}, ${stats.coursesPlanifiees}],
                                    backgroundColor: ['#ff9800', '#4caf50', '#2196f3'], borderWidth: 0 }] },
                options: { responsive: true, maintainAspectRatio: true, plugins: { legend: { position: 'bottom' } } }
            });

            const ctx2 = document.getElementById('revenueChart').getContext('2d');
            new Chart(ctx2, {
                type: 'line',
                data: { labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'],
                        datasets: [{ label: 'Revenus (Ar)', data: [6250000, 6750000, 7100000, 7900000, 8250000, 9100000],
                                    borderColor: '#667eea', backgroundColor: 'rgba(102,126,234,0.1)', tension: 0.4, fill: true }] },
                options: { responsive: true, maintainAspectRatio: true, plugins: { legend: { position: 'bottom' } } }
            });

            const topDrivers = [
                { name: 'Jean Dupont', revenue: 16250000 },
                { name: 'Sophie Martin', revenue: 14450000 },
                { name: 'Pierre Bernard', revenue: 12250000 },
                { name: 'Marie Lambert', revenue: 10600000 },
                { name: 'Thomas Petit', revenue: 9450000 }
            ];
            const listContainer = document.getElementById('topDriversList');
            topDrivers.forEach(driver => {
                const li = document.createElement('li');
                li.className = 'performer-item';
                li.innerHTML = `<span class="performer-name"><i class="fas fa-user"></i> ${driver.name}</span><span class="performer-value">${driver.revenue.toLocaleString('fr-FR')} Ar</span>`;
                listContainer.appendChild(li);
            });
        });
    </script>
</body>
</html>