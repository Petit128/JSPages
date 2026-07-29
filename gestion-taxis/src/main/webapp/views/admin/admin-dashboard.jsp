<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administration - TaxiFlow</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f5f7fa; }
        
        .header {
            background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center;
            position: sticky; top: 0; z-index: 100;
        }
        .logo { font-size: 1.5rem; font-weight: 800; color: #667eea; display: flex; align-items: center; gap: 10px; }
        .btn-logout { background: #f44336; color: white; border: none; padding: 8px 16px; border-radius: 8px; cursor: pointer; }
        
        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }
        
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1.5rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: white; border-radius: 20px; padding: 1.5rem;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); transition: transform 0.3s;
            display: flex; align-items: center; gap: 1rem;
        }
        .stat-card:hover { transform: translateY(-5px); }
        .stat-icon { width: 60px; height: 60px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 15px; display: flex; align-items: center; justify-content: center; color: white; font-size: 1.8rem; }
        .stat-info { flex: 1; }
        .stat-value { font-size: 1.8rem; font-weight: 800; color: #333; }
        .stat-label { color: #666; font-size: 0.85rem; margin-top: 0.25rem; }
        
        .charts-grid {
            display: grid; grid-template-columns: repeat(2, 1fr);
            gap: 1.5rem; margin-bottom: 2rem;
        }
        .chart-card {
            background: white; border-radius: 20px; padding: 1.5rem;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .chart-title { font-size: 1.1rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 8px; }
        canvas { max-height: 250px; }
        
        .menu-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem; margin-bottom: 2rem;
        }
        .menu-card {
            background: white; border-radius: 15px; padding: 1.5rem; text-align: center;
            cursor: pointer; transition: all 0.3s; text-decoration: none; color: #333; display: block;
        }
        .menu-card:hover { transform: translateY(-5px); box-shadow: 0 10px 25px rgba(0,0,0,0.1); }
        .menu-icon { font-size: 2rem; margin-bottom: 0.5rem; color: #667eea; }
        .menu-card h3 { font-size: 1rem; margin-bottom: 0.25rem; }
        .menu-card p { font-size: 0.8rem; color: #666; }
        
        .table-card {
            background: white; border-radius: 20px; padding: 1.5rem;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); margin-bottom: 2rem;
        }
        .section-title { font-size: 1.2rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 8px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #f0f0f0; }
        th { background: #f8f9fa; font-weight: 600; }
        tr:hover { background: #f9f9f9; }
        
        .badge {
            padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600; display: inline-block;
        }
        .badge-success { background: #e8f5e9; color: #4caf50; }
        .badge-warning { background: #fff3e0; color: #ff9800; }
        .badge-danger { background: #ffebee; color: #f44336; }
        .badge-info { background: #e3f2fd; color: #2196f3; }
        
        @media (max-width: 768px) {
            .stats-grid { grid-template-columns: 1fr; }
            .charts-grid { grid-template-columns: 1fr; }
            .header { flex-direction: column; gap: 1rem; }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        String userName = (String) session.getAttribute("userName");
        if(userName == null) userName = "Administrateur";
    %>
    
    <header class="header">
        <div class="logo"><i class="fas fa-taxi"></i> TaxiFlow Administration</div>
        <div>
            <i class="fas fa-user-circle"></i> <%= userName %>
            <button class="btn-logout" onclick="location.href='<%= contextPath %>/logout'"><i class="fas fa-sign-out-alt"></i> Déconnexion</button>
        </div>
    </header>
    
    <div class="container">
        <div class="stats-grid" id="statsGrid">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-users"></i></div>
                <div class="stat-info">
                    <div class="stat-value" id="totalChauffeurs">-</div>
                    <div class="stat-label">Chauffeurs</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-car"></i></div>
                <div class="stat-info">
                    <div class="stat-value" id="totalVehicules">-</div>
                    <div class="stat-label">Véhicules</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-calendar-day"></i></div>
                <div class="stat-info">
                    <div class="stat-value" id="totalCourses">-</div>
                    <div class="stat-label">Courses totales</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-mga-sign"></i></div>
                <div class="stat-info">
                    <div class="stat-value" id="revenusTotal">- Ar</div>
                    <div class="stat-label">Revenus totaux</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-check-circle"></i></div>
                <div class="stat-info">
                    <div class="stat-value" id="chauffeursDispo">-</div>
                    <div class="stat-label">Chauffeurs disponibles</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-chart-line"></i></div>
                <div class="stat-info">
                    <div class="stat-value" id="tauxActivite">- %</div>
                    <div class="stat-label">Taux d'activité</div>
                </div>
            </div>
        </div>
        
        <div class="charts-grid">
            <div class="chart-card">
                <div class="chart-title"><i class="fas fa-chart-pie"></i> Répartition des courses</div>
                <canvas id="statusChart"></canvas>
            </div>
            <div class="chart-card">
                <div class="chart-title"><i class="fas fa-chart-line"></i> Évolution des revenus (6 mois)</div>
                <canvas id="revenueChart"></canvas>
            </div>
        </div>
        
        <div class="menu-grid">
            <a href="<%= contextPath %>/chauffeurs" class="menu-card">
                <div class="menu-icon"><i class="fas fa-users"></i></div>
                <h3>Chauffeurs</h3>
                <p>Gérer les chauffeurs</p>
            </a>
            <a href="<%= contextPath %>/vehicules" class="menu-card">
                <div class="menu-icon"><i class="fas fa-car"></i></div>
                <h3>Véhicules</h3>
                <p>Gérer les véhicules</p>
            </a>
            <a href="<%= contextPath %>/courses" class="menu-card">
                <div class="menu-icon"><i class="fas fa-map-marker-alt"></i></div>
                <h3>Courses</h3>
                <p>Gérer les courses</p>
            </a>
            <a href="<%= contextPath %>/statistiques" class="menu-card">
                <div class="menu-icon"><i class="fas fa-chart-line"></i></div>
                <h3>Statistiques</h3>
                <p>Analyser les données</p>
            </a>
            <a href="<%= contextPath %>/calcul-prix" class="menu-card">
                <div class="menu-icon"><i class="fas fa-calculator"></i></div>
                <h3>Calculateur</h3>
                <p>Simuler un prix</p>
            </a>
            <a href="<%= contextPath %>/parametres" class="menu-card">
                <div class="menu-icon"><i class="fas fa-sliders-h"></i></div>
                <h3>Paramètres</h3>
                <p>Configurer les tarifs</p>
            </a>
        </div>
        
        <div class="table-card">
            <div class="section-title"><i class="fas fa-trophy"></i> Top 5 des chauffeurs</div>
            <table id="topChauffeursTable">
                <thead><tr><th>Chauffeur</th><th>Courses</th><th>Revenus</th><th>Évaluation</th></tr></thead>
                <tbody id="topChauffeurs"></tbody>
            </table>
        </div>
        
        <div class="table-card">
            <div class="section-title"><i class="fas fa-history"></i> Dernières courses</div>
            <div style="overflow-x: auto;">
                <table id="coursesTable">
                    <thead><tr><th>ID</th><th>Client</th><th>Départ</th><th>Arrivée</th><th>Date</th><th>Prix</th><th>Statut</th></tr></thead>
                    <tbody id="lastCourses"></tbody>
                </table>
            </div>
        </div>
    </div>
    
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        let statusChart, revenueChart;
        
        // Données simulées pour les graphiques
        const chartData = {
            status: { labels: ['Terminées', 'En cours', 'Assignées', 'En attente'], data: [15, 3, 5, 2] },
            revenue: { labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'], data: [1250, 1350, 1420, 1580, 1650, 1820] }
        };
        
        function initCharts() {
            const ctx1 = document.getElementById('statusChart').getContext('2d');
            statusChart = new Chart(ctx1, {
                type: 'doughnut',
                data: { labels: chartData.status.labels, datasets: [{ data: chartData.status.data, backgroundColor: ['#4caf50', '#ff9800', '#2196f3', '#f44336'], borderWidth: 0 }] },
                options: { responsive: true, maintainAspectRatio: true, plugins: { legend: { position: 'bottom' } } }
            });
            
            const ctx2 = document.getElementById('revenueChart').getContext('2d');
            revenueChart = new Chart(ctx2, {
                type: 'line',
                data: { labels: chartData.revenue.labels, datasets: [{ label: 'Revenus (ariary-mga)', data: chartData.revenue.data, borderColor: '#667eea', backgroundColor: 'rgba(102, 126, 234, 0.1)', tension: 0.4, fill: true }] },
                options: { responsive: true, maintainAspectRatio: true, plugins: { legend: { position: 'bottom' } } }
            });
        }
        
        function chargerStatistiques() {
            // Données par défaut
            document.getElementById('totalChauffeurs').innerText = '3';
            document.getElementById('totalVehicules').innerText = '3';
            document.getElementById('totalCourses').innerText = '24';
            document.getElementById('revenusTotal').innerText = '1250.00 ar';
            document.getElementById('chauffeursDispo').innerText = '2';
            document.getElementById('tauxActivite').innerText = '67';
            
            // Essayer de charger depuis l'API
            fetch(contextPath + '/api/statistiques/dashboard')
                .then(res => res.ok ? res.json() : null)
                .then(data => {
                    if(data) {
                        document.getElementById('totalChauffeurs').innerText = data.totalChauffeurs || 3;
                        document.getElementById('totalVehicules').innerText = data.totalVehicules || 3;
                        document.getElementById('totalCourses').innerText = data.coursesAujourdhui || 24;
                        document.getElementById('revenusTotal').innerText = (data.revenusMois || 1250).toFixed(2) + ' Ar';
                        document.getElementById('chauffeursDispo').innerText = data.chauffeursDisponibles || 2;
                        let taux = data.totalChauffeurs > 0 ? Math.round((data.chauffeursDisponibles / data.totalChauffeurs) * 100) : 0;
                        document.getElementById('tauxActivite').innerText = taux || 67;
                    }
                })
                .catch(() => {});
        }
        
        function chargerTopChauffeurs() {
            const topData = [
                { nom: 'Jean Dupont', courses: 45, revenu: 3250.50, evaluation: 4.8 },
                { nom: 'Sophie Martin', courses: 38, revenu: 2890.75, evaluation: 4.9 },
                { nom: 'Pierre Bernard', courses: 32, revenu: 2450.30, evaluation: 4.7 },
                { nom: 'Marie Lambert', courses: 28, revenu: 2120.90, evaluation: 4.6 },
                { nom: 'Thomas Petit', courses: 25, revenu: 1890.60, evaluation: 4.5 }
            ];
            
            let html = '';
            topData.forEach(c => {
                html += `<tr>
                    <td><strong>${c.nom}</strong></td>
                    <td>${c.courses}</td>
                    <td style="color: #4caf50; font-weight: bold;">${c.revenu.toFixed(2)} Ar</td>
                    <td>${c.evaluation} ★</td>
                </tr>`;
            });
            document.getElementById('topChauffeurs').innerHTML = html;
        }
        
        function chargerDernieresCourses() {
            const coursesData = [
                { id: 101, client: 'Jean Dupont', depart: '12 rue de Paris', arrivee: '45 avenue des Champs', date: '2024-01-15 14:30', prix: 25.50, statut: 'Terminée' },
                { id: 102, client: 'Sophie Martin', depart: '8 boulevard Voltaire', arrivee: '15 rue de la République', date: '2024-01-15 15:45', prix: 18.30, statut: 'Terminée' },
                { id: 103, client: 'Pierre Bernard', depart: '3 place de la Bastille', arrivee: '22 rue de Rivoli', date: '2024-01-15 16:20', prix: 32.00, statut: 'En cours' },
                { id: 104, client: 'Marie Lambert', depart: '5 avenue des Champs', arrivee: '10 rue de la Paix', date: '2024-01-15 17:00', prix: 15.80, statut: 'Assignée' },
                { id: 105, client: 'Thomas Petit', depart: '20 rue Oberkampf', arrivee: '35 rue de Belleville', date: '2024-01-15 17:30', prix: 22.40, statut: 'En attente' }
            ];
            
            let html = '';
            coursesData.forEach(c => {
                let statusClass = '';
                if(c.statut === 'Terminée') statusClass = 'badge-success';
                else if(c.statut === 'En cours') statusClass = 'badge-warning';
                else if(c.statut === 'Assignée') statusClass = 'badge-info';
                else statusClass = 'badge-danger';
                
                html += `<tr>
                    <td>#${c.id}</td>
                    <td>${c.client}</td>
                    <td>${c.depart}</td>
                    <td>${c.arrivee}</td>
                    <td>${c.date}</td>
                    <td style="font-weight: bold;">${c.prix.toFixed(2)} Ar</td>
                    <td><span class="badge ${statusClass}">${c.statut}</span></td>
                </tr>`;
            });
            document.getElementById('lastCourses').innerHTML = html;
        }
        
        chargerStatistiques();
        chargerTopChauffeurs();
        chargerDernieresCourses();
        initCharts();
        
        setInterval(() => {
            chargerStatistiques();
        }, 30000);
    </script>
</body>
</html>