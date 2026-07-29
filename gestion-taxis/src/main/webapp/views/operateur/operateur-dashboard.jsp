<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Opérateur - TaxiFlow</title>
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
        .container { max-width: 1400px; margin: 2rem auto; padding: 0 2rem; }
        
        .stats-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: white; border-radius: 15px; padding: 1.5rem; text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); transition: transform 0.3s;
        }
        .stat-card:hover { transform: translateY(-5px); }
        .stat-value { font-size: 2rem; font-weight: 800; color: #667eea; }
        .stat-label { color: #666; font-size: 0.85rem; margin-top: 0.5rem; }
        
        .section-title {
            font-size: 1.2rem; font-weight: 600; margin-bottom: 1rem;
            display: flex; align-items: center; gap: 8px;
        }
        
        .drivers-grid {
            display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1rem; margin-bottom: 2rem;
        }
        .driver-card {
            background: white; border-radius: 15px; padding: 1rem;
            display: flex; justify-content: space-between; align-items: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            transition: transform 0.3s;
        }
        .driver-card:hover { transform: translateY(-3px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        .driver-info h4 { margin-bottom: 5px; }
        .driver-info p { font-size: 0.85rem; color: #666; }
        .status-dot { width: 12px; height: 12px; border-radius: 50%; display: inline-block; margin-right: 8px; }
        .status-disponible { background: #4caf50; box-shadow: 0 0 5px #4caf50; }
        .status-en-course { background: #ff9800; box-shadow: 0 0 5px #ff9800; }
        .status-en-pause { background: #2196f3; box-shadow: 0 0 5px #2196f3; }
        .status-indisponible { background: #f44336; box-shadow: 0 0 5px #f44336; }
        
        .courses-grid {
            display: grid; grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 1rem; margin-bottom: 2rem;
        }
        .course-card {
            background: white; border-radius: 15px; padding: 1rem;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            border-left: 4px solid #2196f3;
            transition: transform 0.3s;
        }
        .course-card:hover { transform: translateY(-3px); }
        .course-header {
            display: flex; justify-content: space-between; margin-bottom: 0.5rem;
        }
        .course-id { font-weight: 600; color: #667eea; }
        .course-status {
            padding: 2px 8px; border-radius: 20px; font-size: 0.7rem; font-weight: 600;
        }
        .status-attente { background: #e3f2fd; color: #1976d2; }
        .status-assignee { background: #fff3e0; color: #ff9800; }
        .status-terminee { background: #e8f5e9; color: #4caf50; }
        .btn-assign {
            background: #ff9800; color: white; border: none; padding: 8px 12px;
            border-radius: 8px; cursor: pointer; margin-top: 0.5rem; width: 100%;
            transition: all 0.3s;
        }
        .btn-assign:hover { background: #f57c00; transform: translateY(-2px); }
        
        .btn-add {
            background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%);
            color: white; border: none; padding: 12px 24px; border-radius: 10px;
            cursor: pointer; text-decoration: none; display: inline-block;
            transition: transform 0.3s;
        }
        .btn-add:hover { transform: translateY(-2px); }
        .btn-logout {
            background: #f44336; color: white; border: none; padding: 8px 16px;
            border-radius: 8px; cursor: pointer; margin-left: 1rem;
        }
        
        .modal {
            display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0,0,0,0.5); z-index: 1000; justify-content: center; align-items: center;
        }
        .modal-content {
            background: white; border-radius: 20px; padding: 2rem; width: 500px; max-width: 90%;
        }
        .form-input {
            width: 100%; padding: 10px; margin: 8px 0; border: 2px solid #e0e0e0;
            border-radius: 8px; font-size: 1rem;
        }
        
        @media (max-width: 768px) {
            .drivers-grid, .courses-grid { grid-template-columns: 1fr; }
            .header { flex-direction: column; gap: 1rem; }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        String userName = (String) session.getAttribute("userName");
        if(userName == null) userName = "Opérateur";
    %>
    
    <header class="header">
        <div class="logo"><i class="fas fa-taxi"></i> TaxiFlow - Centre de Dispatch</div>
        <div>
            <i class="fas fa-user-circle"></i> <%= userName %>
            <button class="btn-logout" onclick="location.href='<%= contextPath %>/logout'">Déconnexion</button>
        </div>
    </header>
    
    <div class="container">
        <!-- Statistiques en temps réel -->
        <div class="stats-grid" id="liveStats">
            <div class="stat-card">
                <div class="stat-value" id="chauffeursDispo">0</div>
                <div class="stat-label">Chauffeurs disponibles</div>
            </div>
            <div class="stat-card">
                <div class="stat-value" id="vehiculesDispo">0</div>
                <div class="stat-label">Véhicules disponibles</div>
            </div>
            <div class="stat-card">
                <div class="stat-value" id="coursesEnAttente">0</div>
                <div class="stat-label">Courses en attente</div>
            </div>
            <div class="stat-card">
                <div class="stat-value" id="coursesAujourdhui">0</div>
                <div class="stat-label">Courses aujourd'hui</div>
            </div>
        </div>
        
        <!-- Actions rapides -->
        <div style="display: flex; gap: 1rem; margin-bottom: 2rem; flex-wrap: wrap;">
            <button class="btn-add" onclick="openNewCourseModal()"><i class="fas fa-plus"></i> Nouvelle course</button>
            <button class="btn-add" onclick="location.reload()"><i class="fas fa-sync-alt"></i> Rafraîchir</button>
            <button class="btn-add" onclick="window.location.href='<%= contextPath %>/courses'"><i class="fas fa-list"></i> Toutes les courses</button>
        </div>
        
        <!-- Chauffeurs disponibles -->
        <div class="section-title"><i class="fas fa-users"></i> Chauffeurs disponibles</div>
        <div class="drivers-grid" id="driversList">
            <div class="driver-card">Chargement...</div>
        </div>
        
        <!-- Courses en attente d'assignation -->
        <div class="section-title"><i class="fas fa-clock"></i> Courses en attente d'assignation</div>
        <div class="courses-grid" id="pendingCoursesList">
            <div class="course-card">Chargement...</div>
        </div>
    </div>
    
    <!-- Modal Nouvelle Course -->
    <div id="courseModal" class="modal">
        <div class="modal-content">
            <h3>Nouvelle course</h3>
            <form id="courseForm">
                <input type="text" name="clientNom" placeholder="Nom du client" class="form-input" required>
                <input type="text" name="clientTelephone" placeholder="Téléphone" class="form-input" required>
                <input type="text" name="adresseDepart" placeholder="Adresse de départ" class="form-input" required>
                <input type="text" name="adresseArrivee" placeholder="Adresse d'arrivée" class="form-input" required>
                <input type="datetime-local" name="dateHeure" class="form-input">
                <input type="number" name="distance" placeholder="Distance (km)" step="0.1" class="form-input" required>
                <select name="nombreBagages" class="form-input">
                    <option value="0">0 bagage</option>
                    <option value="1">1 bagage</option>
                    <option value="2">2 bagages</option>
                    <option value="3">3+ bagages</option>
                </select>
                <label><input type="checkbox" name="assignAuto" checked> Assigner automatiquement</label>
                <button type="submit" class="btn-add" style="width: 100%; margin-top: 1rem;">Créer la course</button>
            </form>
            <button onclick="closeModal('courseModal')" style="margin-top: 1rem; width: 100%; padding: 8px;">Fermer</button>
        </div>
    </div>
    
    <!-- Modal Assignation -->
    <div id="assignModal" class="modal">
        <div class="modal-content">
            <h3>Assigner une course</h3>
            <input type="hidden" id="assignCourseId">
            <select id="assignChauffeurId" class="form-input">
                <option value="">Chargement...</option>
            </select>
            <select id="assignVehiculeId" class="form-input">
                <option value="">Chargement...</option>
            </select>
            <button onclick="assignerCourse()" class="btn-add" style="width: 100%; margin-top: 1rem;">Assigner</button>
            <button onclick="closeModal('assignModal')" style="margin-top: 1rem; width: 100%; padding: 8px;">Annuler</button>
        </div>
    </div>
    
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        function formatAriary(n) {
            if (n === undefined || n === null) n = 0;
            return n.toLocaleString('fr-FR') + ' Ar';
        }
        
        function chargerDonnees() {
            // Charger les chauffeurs disponibles
            fetch(contextPath + '/api/chauffeur/disponibles')
                .then(res => res.ok ? res.json() : [])
                .then(data => {
                    let disponibles = Array.isArray(data) ? data : [];
                    document.getElementById('chauffeursDispo').innerText = disponibles.length;
                    
                    let html = '';
                    if(disponibles.length > 0) {
                        for(let i = 0; i < disponibles.length; i++) {
                            let c = disponibles[i];
                            let nom = (c.prenom ? c.prenom : '') + ' ' + (c.nom ? c.nom : '');
                            let telephone = c.telephone ? c.telephone : '';
                            let evaluation = c.evaluation ? c.evaluation : 5.0;
                            let nombreCourses = c.nombreCourses ? c.nombreCourses : 0;
                            
                            html += '<div class="driver-card">' +
                                '<div class="driver-info">' +
                                    '<h4><span class="status-dot status-disponible"></span> ' + nom + '</h4>' +
                                    '<p><i class="fas fa-phone"></i> ' + telephone + '</p>' +
                                    '<p><i class="fas fa-star"></i> ' + evaluation + ' ★ | ' + nombreCourses + ' courses</p>' +
                                '</div>' +
                                '<div><i class="fas fa-car" style="color: #4caf50;"></i> Disponible</div>' +
                            '</div>';
                        }
                    } else {
                        html = '<div class="driver-card">Aucun chauffeur disponible</div>';
                    }
                    document.getElementById('driversList').innerHTML = html;
                })
                .catch(() => {
                    document.getElementById('driversList').innerHTML = '<div class="driver-card">Erreur de chargement</div>';
                });
            
            // Charger les véhicules disponibles
            fetch(contextPath + '/api/vehicules/disponibles')
                .then(res => res.ok ? res.json() : [])
                .then(data => {
                    let disponibles = Array.isArray(data) ? data.filter(v => v.disponible === true) : [];
                    document.getElementById('vehiculesDispo').innerText = disponibles.length;
                })
                .catch(() => {
                    document.getElementById('vehiculesDispo').innerText = '0';
                });
            
            // Charger les courses en attente
            fetch(contextPath + '/api/courses/en-attente')
                .then(res => res.ok ? res.json() : [])
                .then(data => {
                    let enAttente = Array.isArray(data) ? data : [];
                    document.getElementById('coursesEnAttente').innerText = enAttente.length;
                    document.getElementById('coursesAujourdhui').innerText = enAttente.length;
                    
                    let html = '';
                    if(enAttente.length > 0) {
                        for(let i = 0; i < enAttente.length; i++) {
                            let c = enAttente[i];
                            let clientNom = c.clientNom ? c.clientNom : '';
                            let clientTelephone = c.clientTelephone ? c.clientTelephone : '';
                            let adresseDepart = c.adresseDepart ? c.adresseDepart : '';
                            let adresseArrivee = c.adresseArrivee ? c.adresseArrivee : '';
                            let distance = c.distance ? c.distance : 0;
                            let prix = c.prix ? c.prix : 0;
                            
                            html += '<div class="course-card">' +
                                '<div class="course-header">' +
                                    '<span class="course-id">#' + c.id + '</span>' +
                                    '<span class="course-status status-attente">En attente</span>' +
                                '</div>' +
                                '<p><strong>' + clientNom + '</strong> - ' + clientTelephone + '</p>' +
                                '<p><i class="fas fa-map-marker-alt"></i> ' + adresseDepart + '</p>' +
                                '<p><i class="fas fa-flag-checkered"></i> ' + adresseArrivee + '</p>' +
                                '<p><i class="fas fa-road"></i> ' + distance + ' km | ' + formatAriary(prix) + '</p>' +
                                '<button class="btn-assign" onclick="openAssignModal(' + c.id + ')"><i class="fas fa-user-check"></i> Assigner</button>' +
                            '</div>';
                        }
                    } else {
                        html = '<div class="course-card">Aucune course en attente</div>';
                    }
                    document.getElementById('pendingCoursesList').innerHTML = html;
                })
                .catch(() => {
                    document.getElementById('pendingCoursesList').innerHTML = '<div class="course-card">Erreur de chargement</div>';
                });
        }
        
        function openNewCourseModal() {
            document.getElementById('courseModal').style.display = 'flex';
        }
        
        function openAssignModal(courseId) {
            document.getElementById('assignCourseId').value = courseId;
            
            // Charger les chauffeurs disponibles
            fetch(contextPath + '/api/chauffeur/disponibles')
                .then(res => res.ok ? res.json() : [])
                .then(data => {
                    let html = '<option value="">Sélectionner un chauffeur</option>';
                    if(Array.isArray(data)) {
                        for(let i = 0; i < data.length; i++) {
                            let c = data[i];
                            let nom = (c.prenom ? c.prenom : '') + ' ' + (c.nom ? c.nom : '');
                            let telephone = c.telephone ? c.telephone : '';
                            html += '<option value="' + c.id + '">' + nom + ' - ' + telephone + '</option>';
                        }
                    }
                    document.getElementById('assignChauffeurId').innerHTML = html;
                });
            
            // Charger les véhicules disponibles
            fetch(contextPath + '/api/vehicules/disponibles')
                .then(res => res.ok ? res.json() : [])
                .then(data => {
                    let html = '<option value="">Sélectionner un véhicule</option>';
                    if(Array.isArray(data)) {
                        let disponibles = data.filter(v => v.disponible === true);
                        for(let i = 0; i < disponibles.length; i++) {
                            let v = disponibles[i];
                            let marque = v.marque ? v.marque : '';
                            let modele = v.modele ? v.modele : '';
                            let immatriculation = v.immatriculation ? v.immatriculation : '';
                            html += '<option value="' + v.id + '">' + marque + ' ' + modele + ' - ' + immatriculation + '</option>';
                        }
                    }
                    document.getElementById('assignVehiculeId').innerHTML = html;
                });
            
            document.getElementById('assignModal').style.display = 'flex';
        }
        
        function assignerCourse() {
            const courseId = document.getElementById('assignCourseId').value;
            const chauffeurId = document.getElementById('assignChauffeurId').value;
            const vehiculeId = document.getElementById('assignVehiculeId').value;
            
            if(!chauffeurId || !vehiculeId) {
                alert('Veuillez sélectionner un chauffeur et un véhicule');
                return;
            }
            
            // Assignation via le servlet existant
            window.location.href = contextPath + '/courses?action=assignerManuelle&courseId=' + courseId + '&chauffeurId=' + chauffeurId + '&vehiculeId=' + vehiculeId;
        }
        
        document.getElementById('courseForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            const params = new URLSearchParams();
            for(let [key, value] of formData) { params.append(key, value); }
            params.append('action', 'attribuer');
            window.location.href = contextPath + '/courses?' + params.toString();
        });
        
        function closeModal(id) {
            document.getElementById(id).style.display = 'none';
        }
        
        window.onclick = function(event) {
            if(event.target.classList.contains('modal')) event.target.style.display = 'none';
        }
        
        // Initialisation
        chargerDonnees();
        setInterval(chargerDonnees, 15000);
    </script>
</body>
</html>