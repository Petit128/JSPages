<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Commander un taxi - TaxiFlow</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 2rem;
        }
        .container { max-width: 800px; margin: 0 auto; }
        .header {
            background: white; border-radius: 20px; padding: 1rem 2rem;
            display: flex; justify-content: space-between; align-items: center;
            margin-bottom: 2rem;
        }
        .logo { font-size: 1.5rem; font-weight: 800; color: #667eea; }
        .card {
            background: white; border-radius: 30px; overflow: hidden;
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; padding: 2rem; text-align: center;
        }
        .card-header h1 { font-size: 2rem; margin-bottom: 0.5rem; }
        .card-body { padding: 2rem; }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; font-weight: 600; color: #333; }
        input, select {
            width: 100%; padding: 12px; border: 2px solid #e0e0e0;
            border-radius: 10px; font-size: 1rem; transition: all 0.3s;
        }
        input:focus, select:focus { outline: none; border-color: #667eea; }
        .row-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
        .checkbox-group { display: flex; align-items: center; gap: 10px; margin-top: 0.5rem; }
        .checkbox-group input { width: auto; }
        .btn-submit {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; border: none; padding: 14px 28px; border-radius: 10px;
            font-size: 1rem; font-weight: 600; cursor: pointer; width: 100%;
            transition: transform 0.3s; margin-top: 1rem;
        }
        .btn-submit:hover { transform: translateY(-2px); }
        .tracker {
            margin-top: 2rem; padding: 1.5rem; background: #f8f9fa;
            border-radius: 20px; display: none;
        }
        .tracker h3 { margin-bottom: 1rem; color: #667eea; }
        .driver-info {
            display: flex; align-items: center; gap: 1rem; padding: 1rem;
            background: white; border-radius: 15px; margin-top: 1rem;
        }
        .progress-bar {
            width: 100%; height: 8px; background: #e0e0e0;
            border-radius: 4px; margin: 1rem 0;
        }
        .progress-fill {
            height: 100%; background: #4caf50; border-radius: 4px;
            transition: width 0.5s;
        }
        .price-display {
            font-size: 1.2rem; font-weight: bold; color: #4caf50;
            margin-top: 1rem; text-align: center;
        }
        @media (max-width: 768px) { .row-2 { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        com.taxis.model.Client client = (com.taxis.model.Client) session.getAttribute("client");
        boolean isClientLogged = client != null;
    %>
    <div class="container">
        <div class="header">
            <div class="logo"><i class="fas fa-taxi"></i> TaxiFlow</div>
            <div>
                <% if(isClientLogged) { %>
                    <i class="fas fa-user-circle"></i> <%= client.getPrenom() %> <%= client.getNom() %>
                <% } %>
                <a href="<%= contextPath %>/" style="color: #667eea; margin-left: 1rem;">Accueil</a>
            </div>
        </div>
        
        <div class="card">
            <div class="card-header">
                <i class="fas fa-taxi" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                <h1>Commander un taxi</h1>
                <p>Remplissez le formulaire pour réserver votre course</p>
            </div>
            <div class="card-body">
                <form id="courseForm">
                    <div class="row-2">
                        <div class="form-group">
                            <label><i class="fas fa-user"></i> Nom complet</label>
                            <input type="text" id="nom" required placeholder="Jean Dupont" 
                                   value="<%= isClientLogged ? client.getNomComplet() : "" %>">
                        </div>
                        <div class="form-group">
                            <label><i class="fas fa-phone"></i> Téléphone</label>
                            <input type="tel" id="telephone" required placeholder="06 12 34 56 78"
                                   value="<%= isClientLogged ? client.getTelephone() : "" %>">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" id="email" placeholder="exemple@email.com"
                               value="<%= isClientLogged ? client.getEmail() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label><i class="fas fa-map-marker-alt"></i> Adresse de départ</label>
                        <input type="text" id="depart" required placeholder="123 rue de Paris"
                               value="<%= isClientLogged ? client.getAdresse() : "" %>">
                    </div>
                    
                    <div class="form-group">
                        <label><i class="fas fa-flag-checkered"></i> Adresse d'arrivée</label>
                        <input type="text" id="arrivee" required placeholder="45 avenue des Champs">
                    </div>
                    
                    <div class="row-2">
                        <div class="form-group">
                            <label><i class="fas fa-suitcase"></i> Bagages</label>
                            <select id="bagages">
                                <option value="0">0 bagage</option>
                                <option value="1">1 bagage</option>
                                <option value="2">2 bagages</option>
                                <option value="3">3+ bagages</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label><i class="fas fa-paw"></i> Animaux</label>
                            <select id="animaux">
                                <option value="false">Non</option>
                                <option value="true">Oui</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="row-2">
                        <div class="form-group">
                            <label><i class="fas fa-calendar"></i> Date et heure</label>
                            <input type="datetime-local" id="dateHeure">
                        </div>
                        <div class="form-group">
                            <label><i class="fas fa-road"></i> Distance estimée (km)</label>
                            <input type="number" id="distance" step="0.1" min="0" value="5" required>
                        </div>
                    </div>
                    
                    <div class="checkbox-group">
                        <input type="checkbox" id="reservation">
                        <label><i class="fas fa-calendar-check"></i> Course sur réservation (+10 000 Ar)</label>
                    </div>
                    
                    <div class="price-display" id="pricePreview">
                        Prix estimé: <strong id="estimatedPrice">0 Ar</strong>
                    </div>
                    
                    <button type="button" class="btn-submit" onclick="demanderCourse()">
                        <i class="fas fa-search"></i> Trouver un taxi
                    </button>
                </form>
                
                <div id="tracker" class="tracker">
                    <h3><i class="fas fa-spinner fa-spin"></i> Recherche d'un taxi...</h3>
                    <div id="courseStatus"></div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        let intervalId;
        let currentCourseId;
        const contextPath = '${pageContext.request.contextPath}';
        
        // Tarifs en Ariary
        const TARIFS = {
            prixBase: 17500,
            prixKmJour: 6000,
            prixKmNuit: 9000,
            fraisBagages: 10000,
            fraisAnimaux: 15000,
            fraisReservation: 10000
        };
        
        function formatAriary(n) {
            if (n === undefined || n === null) n = 0;
            return n.toLocaleString('fr-FR') + ' Ar';
        }
        
        function estHeureNuit(date) {
            if(!date) return false;
            let heures = date.getHours();
            return (heures >= 22 || heures < 6);
        }
        
        function getPrixKm(date) {
            if(estHeureNuit(date)) return TARIFS.prixKmNuit;
            return TARIFS.prixKmJour;
        }
        
        function calculerPrixEstime() {
            let distance = parseFloat(document.getElementById('distance').value) || 0;
            let nombreBagages = parseInt(document.getElementById('bagages').value) || 0;
            let animauxPresent = document.getElementById('animaux').value === 'true';
            let reservation = document.getElementById('reservation').checked;
            
            let dateStr = document.getElementById('dateHeure').value;
            let date = dateStr ? new Date(dateStr) : new Date();
            
            let prixKm = getPrixKm(date);
            let prixBase = TARIFS.prixBase;
            let prixDistance = distance * prixKm;
            let fraisBagages = nombreBagages * TARIFS.fraisBagages;
            let fraisAnimaux = animauxPresent ? TARIFS.fraisAnimaux : 0;
            let fraisReservation = reservation ? TARIFS.fraisReservation : 0;
            
            let total = prixBase + prixDistance + fraisBagages + fraisAnimaux + fraisReservation;
            
            document.getElementById('estimatedPrice').innerHTML = formatAriary(total);
        }
        
        document.getElementById('distance').addEventListener('input', calculerPrixEstime);
        document.getElementById('bagages').addEventListener('change', calculerPrixEstime);
        document.getElementById('animaux').addEventListener('change', calculerPrixEstime);
        document.getElementById('reservation').addEventListener('change', calculerPrixEstime);
        document.getElementById('dateHeure').addEventListener('change', calculerPrixEstime);
        
        calculerPrixEstime();
        
        function demanderCourse() {
            const nom = document.getElementById('nom').value;
            const telephone = document.getElementById('telephone').value;
            const email = document.getElementById('email').value;
            const depart = document.getElementById('depart').value;
            const arrivee = document.getElementById('arrivee').value;
            const bagages = document.getElementById('bagages').value;
            const animaux = document.getElementById('animaux').value === 'true';
            const distance = parseFloat(document.getElementById('distance').value) || 0;
            const dateHeure = document.getElementById('dateHeure').value;
            const reservation = document.getElementById('reservation').checked;
            
            if(!nom || !telephone || !depart || !arrivee) {
                alert('Veuillez remplir tous les champs obligatoires');
                return;
            }
            
            const data = {
                nom: nom,
                telephone: telephone,
                email: email,
                depart: depart,
                arrivee: arrivee,
                bagages: bagages,
                animaux: animaux,
                distance: distance,
                dateHeure: dateHeure,
                reservation: reservation
            };
            
            document.getElementById('tracker').style.display = 'block';
            document.getElementById('courseStatus').innerHTML = '<p><i class="fas fa-spinner fa-spin"></i> Envoi de la demande...</p>';
            
            fetch(contextPath + '/api/client/request', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(data => {
                if(data.success) {
                    currentCourseId = data.courseId;
                    suivreCourse(currentCourseId);
                } else {
                    document.getElementById('courseStatus').innerHTML = '<p style="color: red;">' + (data.message || 'Aucun taxi disponible. Veuillez réessayer.') + '</p>';
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                document.getElementById('courseStatus').innerHTML = '<p style="color: red;">Erreur de connexion. Veuillez réessayer.</p>';
            });
        }
        
        function suivreCourse(courseId) {
            if(intervalId) clearInterval(intervalId);
            
            intervalId = setInterval(() => {
                fetch(contextPath + '/api/client/course/' + courseId + '/status')
                    .then(response => response.json())
                    .then(data => {
                        const statusDiv = document.getElementById('courseStatus');
                        let chauffeurText = (data.chauffeur && data.chauffeur !== '') ? data.chauffeur : 'En attente';
                        let vehiculeText = (data.vehicule && data.vehicule !== '') ? data.vehicule : '---';
                        let tempsArrivee = (data.tempsArrivee !== undefined && data.tempsArrivee !== null) ? data.tempsArrivee : 5;
                        let prixText = (data.prix !== undefined && data.prix !== null) ? formatAriary(data.prix) : formatAriary(0);
                        let distanceText = (data.distance !== undefined && data.distance !== null) ? data.distance : 0;
                        
                        if(data.statut === 'ASSIGNEE' || data.statut === 'ASSIGNED') {
                            statusDiv.innerHTML = `
                                <div style="color: #4caf50;">
                                    <i class="fas fa-check-circle"></i> <strong>Taxi assigné !</strong>
                                    <div class="driver-info">
                                        <i class="fas fa-user-circle" style="font-size: 3rem;"></i>
                                        <div>
                                            <p><strong>Chauffeur:</strong> ` + chauffeurText + `</p>
                                            <p><strong>Véhicule:</strong> ` + vehiculeText + `</p>
                                            <p><strong>Arrivée estimée:</strong> ` + tempsArrivee + ` min</p>
                                            <p><strong>Prix estimé:</strong> ` + prixText + `</p>
                                        </div>
                                    </div>
                                    <div class="progress-bar"><div class="progress-fill" style="width: 30%"></div></div>
                                </div>
                            `;
                        } else if(data.statut === 'EN_COURS' || data.statut === 'IN_PROGRESS') {
                            statusDiv.innerHTML = `
                                <div style="color: #ff9800;">
                                    <i class="fas fa-car"></i> <strong>Taxi en route...</strong>
                                    <div class="driver-info">
                                        <i class="fas fa-user-circle" style="font-size: 3rem;"></i>
                                        <div>
                                            <p><strong>Chauffeur:</strong> ` + chauffeurText + `</p>
                                            <p><strong>Distance:</strong> ` + distanceText + ` km</p>
                                        </div>
                                    </div>
                                    <div class="progress-bar"><div class="progress-fill" style="width: 60%"></div></div>
                                </div>
                            `;
                        } else if(data.statut === 'TERMINEE' || data.statut === 'COMPLETED') {
                            statusDiv.innerHTML = `
                                <div style="color: #4caf50;">
                                    <i class="fas fa-flag-checkered"></i> <strong>Course terminée !</strong>
                                    <div class="driver-info">
                                        <div>
                                            <p><strong>Distance:</strong> ` + distanceText + ` km</p>
                                            <p><strong>Prix total:</strong> ` + prixText + `</p>
                                            <p><i class="fas fa-credit-card"></i> Paiement accepté</p>
                                        </div>
                                    </div>
                                    <button onclick="location.reload()" style="margin-top: 1rem; padding: 8px 16px; background: #667eea; color: white; border: none; border-radius: 8px;">Nouvelle course</button>
                                </div>
                            `;
                            clearInterval(intervalId);
                        }
                    })
                    .catch(error => console.error('Erreur suivi:', error));
            }, 5000);
        }
    </script>
</body>
</html>