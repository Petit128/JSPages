<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assigner une course - TaxiFlow</title>
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
        .card { background: white; border-radius: 20px; overflow: hidden; box-shadow: 0 20px 40px rgba(0,0,0,0.1); }
        .card-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 2rem; text-align: center; }
        .card-body { padding: 2rem; }
        .info-section {
            background: #f8f9fa; border-radius: 15px; padding: 1.5rem;
            margin-bottom: 2rem;
        }
        .info-title { font-weight: 700; color: #667eea; margin-bottom: 1rem; font-size: 1.1rem; }
        .info-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem; }
        .info-item { display: flex; align-items: center; gap: 10px; font-size: 0.9rem; }
        .info-item i { width: 20px; color: #667eea; }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; font-weight: 600; color: #333; }
        select {
            width: 100%; padding: 12px; border: 2px solid #e0e0e0;
            border-radius: 10px; font-size: 1rem;
        }
        select:focus { outline: none; border-color: #667eea; }
        .btn-group { display: flex; gap: 1rem; margin-top: 2rem; }
        .btn-primary, .btn-secondary {
            flex: 1; padding: 12px; border: none; border-radius: 10px;
            font-size: 1rem; font-weight: 600; cursor: pointer;
            transition: transform 0.3s;
        }
        .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
        .btn-primary:hover { transform: translateY(-2px); }
        .btn-secondary { background: #f0f0f0; color: #666; text-decoration: none; text-align: center; display: inline-block; }
        .alert { background: #ffebee; color: #f44336; padding: 1rem; border-radius: 10px; margin-bottom: 1rem; text-align: center; }
        .price { font-size: 1.2rem; font-weight: bold; color: #4caf50; margin-top: 0.5rem; }
        .loading { text-align: center; padding: 1rem; color: #666; }
        @media (max-width: 768px) { .info-grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header">
                <i class="fas fa-user-check" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                <h1>Assigner une course</h1>
                <p>Choisissez un chauffeur et un véhicule disponible</p>
            </div>
            <div class="card-body">
                <div class="info-section">
                    <div class="info-title"><i class="fas fa-info-circle"></i> Détails de la course #${course.id}</div>
                    <div class="info-grid">
                        <div class="info-item"><i class="fas fa-user"></i> <span><strong>Client:</strong> ${course.clientNom}</span></div>
                        <div class="info-item"><i class="fas fa-phone"></i> <span><strong>Tél:</strong> ${course.clientTelephone}</span></div>
                        <div class="info-item"><i class="fas fa-map-marker-alt"></i> <span><strong>Départ:</strong> ${course.adresseDepart}</span></div>
                        <div class="info-item"><i class="fas fa-flag-checkered"></i> <span><strong>Arrivée:</strong> ${course.adresseArrivee}</span></div>
                        <div class="info-item"><i class="fas fa-road"></i> <span><strong>Distance:</strong> ${course.distance} km</span></div>
                        <div class="info-item"><i class="fas fa-clock"></i> <span><strong>Date:</strong> ${course.dateHeure}</span></div>
                    </div>
                    <div class="price">💰 Prix: <fmt:formatNumber value="${course.prix}" pattern="#,##0"/> Ar</div>
                </div>

                <c:if test="${empty chauffeurs}">
                    <div class="alert"><i class="fas fa-exclamation-triangle"></i> Aucun chauffeur disponible !</div>
                </c:if>
                <c:if test="${empty vehicules}">
                    <div class="alert"><i class="fas fa-exclamation-triangle"></i> Aucun véhicule disponible !</div>
                </c:if>

                <div class="form-group">
                    <label><i class="fas fa-user"></i> Chauffeur disponible</label>
                    <select id="chauffeurId" required ${empty chauffeurs ? 'disabled' : ''}>
                        <option value="">-- Sélectionnez un chauffeur --</option>
                        <c:forEach var="chauffeur" items="${chauffeurs}">
                            <option value="${chauffeur.id}">${chauffeur.prenom} ${chauffeur.nom} - ${chauffeur.telephone}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label><i class="fas fa-car"></i> Véhicule disponible</label>
                    <select id="vehiculeId" required ${empty vehicules ? 'disabled' : ''}>
                        <option value="">-- Sélectionnez un véhicule --</option>
                        <c:forEach var="vehicule" items="${vehicules}">
                            <option value="${vehicule.id}">${vehicule.marque} ${vehicule.modele} - ${vehicule.immatriculation}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="btn-group">
                    <button type="button" class="btn-primary" onclick="assignerCourse()" ${empty chauffeurs or empty vehicules ? 'disabled' : ''}>
                        <i class="fas fa-check"></i> Assigner la course
                    </button>
                    <a href="courses" class="btn-secondary"><i class="fas fa-times"></i> Annuler</a>
                </div>
                <div id="loadingMsg" class="loading" style="display: none;">Assignation en cours...</div>
            </div>
        </div>
    </div>
    
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        
        function assignerCourse() {
            const courseId = ${course.id};
            const chauffeurId = document.getElementById('chauffeurId').value;
            const vehiculeId = document.getElementById('vehiculeId').value;
            
            if(!chauffeurId || !vehiculeId) {
                alert('Veuillez sélectionner un chauffeur et un véhicule');
                return;
            }
            
            const loadingMsg = document.getElementById('loadingMsg');
            loadingMsg.style.display = 'block';
            
            const data = {
                courseId: parseInt(courseId),
                chauffeurId: parseInt(chauffeurId),
                vehiculeId: parseInt(vehiculeId)
            };
            
            fetch(contextPath + '/api/courses/assigner', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(data => {
                loadingMsg.style.display = 'none';
                if(data.success) {
                    alert('Course assignée avec succès !');
                    window.location.href = contextPath + '/courses';
                } else {
                    alert('Erreur lors de l\'assignation: ' + (data.error || 'Inconnue'));
                }
            })
            .catch(error => {
                loadingMsg.style.display = 'none';
                console.error('Erreur:', error);
                alert('Erreur de connexion. Veuillez réessayer.');
            });
        }
    </script>
</body>
</html>