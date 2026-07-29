<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Espace Chauffeur - TaxiFlow</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f5f7fa; }
        .header {
            background: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 1rem 2rem; display: flex; justify-content: space-between;
            align-items: center; flex-wrap: wrap;
        }
        .logo { font-size: 1.5rem; font-weight: 800; color: #667eea; display: flex; align-items: center; gap: 10px; }
        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }
        .status-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 20px; padding: 2rem; color: white; margin-bottom: 2rem;
            display: flex; justify-content: space-between; align-items: center;
            flex-wrap: wrap; gap: 1rem;
        }
        .status-badge { background: rgba(255,255,255,0.2); padding: 8px 16px; border-radius: 20px; font-size: 1.1rem; }
        .status-select { background: white; color: #667eea; border: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; font-weight: 600; }
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 1.5rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: white; border-radius: 20px; padding: 1.5rem; text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .stat-icon { font-size: 2.5rem; margin-bottom: 0.5rem; }
        .stat-value { font-size: 2rem; font-weight: 800; color: #333; }
        .stat-label { color: #666; font-size: 0.85rem; margin-top: 0.5rem; }
        .courses-section {
            background: white; border-radius: 20px; padding: 1.5rem;
            margin-bottom: 2rem; box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .section-title {
            font-size: 1.3rem; font-weight: 700; margin-bottom: 1.5rem;
            display: flex; align-items: center; gap: 10px;
        }
        .course-item {
            border: 1px solid #e0e0e0; border-radius: 15px; padding: 1.2rem;
            margin-bottom: 1rem;
        }
        .course-header {
            display: flex; justify-content: space-between; align-items: center;
            margin-bottom: 0.8rem;
        }
        .course-id { font-weight: 700; color: #667eea; font-size: 1.1rem; }
        .course-status {
            padding: 4px 12px; border-radius: 20px; font-size: 0.75rem; font-weight: 600;
        }
        .status-assignee { background: #fff3e0; color: #ff9800; }
        .status-encours { background: #fce4ec; color: #f44336; }
        .course-details {
            display: grid; grid-template-columns: repeat(2, 1fr);
            gap: 0.5rem; margin-bottom: 1rem;
        }
        .course-price { font-size: 1.2rem; font-weight: 700; color: #4caf50; margin-top: 0.5rem; }
        .course-actions { display: flex; gap: 0.8rem; margin-top: 1rem; }
        .btn-start, .btn-finish {
            padding: 10px 20px; border: none; border-radius: 10px;
            cursor: pointer; font-weight: 600;
        }
        .btn-start { background: #ff9800; color: white; }
        .btn-finish { background: #2196f3; color: white; }
        .empty-message { text-align: center; padding: 2rem; color: #999; }
        .bilan-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 1rem; margin-top: 1rem;
        }
        .bilan-item {
            background: #f8f9fa; border-radius: 12px; padding: 1rem; text-align: center;
        }
        .bilan-value { font-size: 1.5rem; font-weight: 800; color: #667eea; }
        @media (max-width: 768px) {
            .course-details { grid-template-columns: 1fr; }
            .status-card { flex-direction: column; text-align: center; }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        com.taxis.model.Chauffeur chauffeur = (com.taxis.model.Chauffeur) session.getAttribute("chauffeur");
        if(chauffeur == null) {
            response.sendRedirect(contextPath + "/login");
            return;
        }
    %>
    <header class="header">
        <div class="logo"><i class="fas fa-taxi"></i> TaxiFlow - Espace Chauffeur</div>
        <div>
            <i class="fas fa-user-circle"></i> <%= chauffeur.getPrenom() %> <%= chauffeur.getNom() %>
            <a href="<%= contextPath %>/logout" style="margin-left: 1rem; color: #f44336;">Déconnexion</a>
        </div>
    </header>
    
    <div class="container">
        <div class="status-card">
            <div>
                <h2>Bonjour, <%= chauffeur.getPrenom() %> <%= chauffeur.getNom() %></h2>
                <p>Statut actuel : <span id="currentStatus" class="status-badge"><%= chauffeur.getStatut().getLibelle() %></span></p>
            </div>
            <div>
                <select id="statusSelect" class="status-select" onchange="changerStatut()">
                    <option value="DISPONIBLE" <%= chauffeur.getStatut() == com.taxis.model.StatutChauffeur.DISPONIBLE ? "selected" : "" %>>🟢 Disponible</option>
                    <option value="EN_PAUSE" <%= chauffeur.getStatut() == com.taxis.model.StatutChauffeur.EN_PAUSE ? "selected" : "" %>>⏸️ En pause</option>
                    <option value="INDISPONIBLE" <%= chauffeur.getStatut() == com.taxis.model.StatutChauffeur.INDISPONIBLE ? "selected" : "" %>>🔴 Indisponible</option>
                </select>
            </div>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-chart-line" style="color: #667eea;"></i></div>
                <div class="stat-value" id="totalCourses"><%= chauffeur.getNombreCourses() %></div>
                <div class="stat-label">Courses effectuées</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-euro-sign" style="color: #4caf50;"></i></div>
                <div class="stat-value" id="totalRevenus"><fmt:formatNumber value="<%= chauffeur.getRevenuTotal() %>" pattern="#,##0"/> Ar</div>
                <div class="stat-label">Revenus totaux</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-star" style="color: #ff9800;"></i></div>
                <div class="stat-value" id="evaluation"><%= chauffeur.getEvaluation() %> ★</div>
                <div class="stat-label">Évaluation</div>
            </div>
        </div>
        
        <div class="courses-section">
            <div class="section-title"><i class="fas fa-clock" style="color: #ff9800;"></i> Mes courses</div>
            <div id="coursesList">
                <div class="empty-message">Chargement...</div>
            </div>
        </div>
        
        <div class="courses-section">
            <div class="section-title"><i class="fas fa-chart-bar" style="color: #ff9800;"></i> Bilan du jour</div>
            <div class="bilan-grid">
                <div class="bilan-item"><div class="bilan-value" id="dailyCourses">-</div><div class="bilan-label">Courses</div></div>
                <div class="bilan-item"><div class="bilan-value" id="dailyDistance">- km</div><div class="bilan-label">Distance</div></div>
                <div class="bilan-item"><div class="bilan-value" id="dailyRevenus">- Ar</div><div class="bilan-label">Revenus</div></div>
            </div>
        </div>
    </div>
    
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        const chauffeurId = <%= chauffeur.getId() %>;
        
        function formatAriary(n) {
            if (n === undefined || n === null || isNaN(n)) n = 0;
            return Number(n).toLocaleString('fr-FR') + ' Ar';
        }
        
        function chargerCourses() {
            fetch(contextPath + '/courses?action=list&format=json')
                .then(res => res.json())
                .then(data => {
                    if(Array.isArray(data)) {
                        const mesCourses = data.filter(course => course.chauffeurId == chauffeurId);
                        afficherCourses(mesCourses);
                    } else {
                        document.getElementById('coursesList').innerHTML = '<div class="empty-message">Aucune course assignée</div>';
                    }
                })
                .catch(error => {
                    console.error('Erreur:', error);
                    document.getElementById('coursesList').innerHTML = '<div class="empty-message">Erreur de chargement</div>';
                });
        }
        
        function afficherCourses(courses) {
            const container = document.getElementById('coursesList');
            if(!courses || courses.length === 0) {
                container.innerHTML = '<div class="empty-message">Aucune course assignée</div>';
                return;
            }
            
            let html = '';
            for(let i = 0; i < courses.length; i++) {
                const course = courses[i];
                let statusClass = '';
                let statusText = '';
                let actions = '';
                
                const clientNom = escapeHtml(course.clientNom || '-');
                const clientTelephone = escapeHtml(course.clientTelephone || '-');
                const adresseDepart = escapeHtml(course.adresseDepart || '-');
                const adresseArrivee = escapeHtml(course.adresseArrivee || '-');
                const distance = course.distance || 0;
                const prix = course.prix || 0;
                
                if(course.statut === 'ASSIGNEE') {
                    statusClass = 'status-assignee';
                    statusText = 'À démarrer';
                    actions = `<button class="btn-start" onclick="demarrerCourse(${course.id})">▶️ Démarrer</button>`;
                } else if(course.statut === 'EN_COURS') {
                    statusClass = 'status-encours';
                    statusText = 'En cours';
                    actions = `<button class="btn-finish" onclick="terminerCourse(${course.id})">🏁 Terminer</button>`;
                } else if(course.statut === 'TERMINEE') {
                    statusClass = 'status-terminee';
                    statusText = 'Terminée';
                    actions = '';
                } else {
                    statusClass = 'status-assignee';
                    statusText = course.statut || 'Assignée';
                }
                
                html += '<div class="course-item">' +
                    '<div class="course-header">' +
                        '<span class="course-id">Course #' + course.id + '</span>' +
                        '<span class="course-status ' + statusClass + '">' + statusText + '</span>' +
                    '</div>' +
                    '<div class="course-details">' +
                        '<div><i class="fas fa-user"></i> Client: ' + clientNom + '</div>' +
                        '<div><i class="fas fa-phone"></i> Tél: ' + clientTelephone + '</div>' +
                        '<div><i class="fas fa-map-marker-alt"></i> Départ: ' + adresseDepart + '</div>' +
                        '<div><i class="fas fa-flag-checkered"></i> Arrivée: ' + adresseArrivee + '</div>' +
                        '<div><i class="fas fa-road"></i> Distance: ' + distance + ' km</div>' +
                    '</div>' +
                    '<div class="course-price">💰 <span class="course-prix-val">' + prix + '</span></div>' +
                    '<div class="course-actions">' + actions + '</div>' +
                '</div>';
            }
            container.innerHTML = html;
            
            // Appliquer le formatage après insertion
            document.querySelectorAll('.course-prix-val').forEach(el => {
                const prix = parseInt(el.innerText);
                el.parentElement.innerHTML = '💰 ' + formatAriary(prix);
            });
        }
        
        function escapeHtml(str) {
            if(!str) return '';
            return str.replace(/[&<>]/g, function(m) {
                if(m === '&') return '&amp;';
                if(m === '<') return '&lt;';
                if(m === '>') return '&gt;';
                return m;
            });
        }
        
        function chargerBilan() {
            fetch(contextPath + '/courses?action=list&format=json')
                .then(res => res.json())
                .then(data => {
                    if(Array.isArray(data)) {
                        const mesCourses = data.filter(course => course.chauffeurId == chauffeurId);
                        const coursesTerminees = mesCourses.filter(c => c.statut === 'TERMINEE');
                        let distanceTotale = 0;
                        let revenusTotaux = 0;
                        for(let i = 0; i < coursesTerminees.length; i++) {
                            distanceTotale += coursesTerminees[i].distance || 0;
                            revenusTotaux += coursesTerminees[i].prix || 0;
                        }
                        document.getElementById('dailyCourses').innerText = coursesTerminees.length;
                        document.getElementById('dailyDistance').innerText = distanceTotale + ' km';
                        document.getElementById('dailyRevenus').innerHTML = formatAriary(revenusTotaux);
                    }
                })
                .catch(() => {
                    document.getElementById('dailyCourses').innerText = '0';
                    document.getElementById('dailyDistance').innerText = '0 km';
                    document.getElementById('dailyRevenus').innerHTML = formatAriary(0);
                });
        }
        
        function changerStatut() {
            const newStatus = document.getElementById('statusSelect').value;
            fetch(contextPath + '/api/chauffeur/statut', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: chauffeurId, statut: newStatus })
            })
            .then(res => res.json())
            .then(data => {
                if(data.success) {
                    document.getElementById('currentStatus').innerHTML = data.libelle;
                    alert('Statut mis à jour : ' + data.libelle);
                }
            })
            .catch(() => alert('Statut mis à jour'));
        }
        
        function demarrerCourse(courseId) {
            if(confirm('Démarrer cette course ?')) {
                window.location.href = contextPath + '/courses?action=demarrer&id=' + courseId;
            }
        }
        
        function terminerCourse(courseId) {
            let distance = prompt("Distance réelle parcourue (km):", "0");
            if(distance !== null) {
                distance = parseFloat(distance) || 0;
                window.location.href = contextPath + '/courses?action=terminer&id=' + courseId + '&distanceReelle=' + distance + '&dureeReelle=0';
            }
        }
        
        // Chargement initial
        chargerCourses();
        chargerBilan();
        setInterval(chargerCourses, 30000);
    </script>
</body>
</html>