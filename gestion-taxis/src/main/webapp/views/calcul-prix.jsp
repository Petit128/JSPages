<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculateur de prix - TaxiFlow (Ariary)</title>
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
        .container { max-width: 1200px; margin: 0 auto; }
        .header {
            background: white; border-radius: 20px; padding: 1rem 2rem;
            display: flex; justify-content: space-between; align-items: center;
            margin-bottom: 2rem; box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        .logo { font-size: 1.5rem; font-weight: 800; color: #667eea; display: flex; align-items: center; gap: 10px; }
        .btn-back { background: #667eea; color: white; border: none; padding: 8px 16px; border-radius: 8px; cursor: pointer; text-decoration: none; display: inline-block; }
        .calculator-card {
            background: white; border-radius: 30px; overflow: hidden;
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; padding: 2rem; text-align: center;
        }
        .card-header h1 { font-size: 2rem; margin-bottom: 0.5rem; }
        .card-body { padding: 2rem; }
        .two-columns { display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; }
        .form-group { margin-bottom: 1.5rem; }
        label { display: block; margin-bottom: 0.5rem; font-weight: 600; color: #333; }
        input, select {
            width: 100%; padding: 12px; border: 2px solid #e0e0e0;
            border-radius: 10px; font-size: 1rem; transition: all 0.3s;
        }
        .checkbox-group { display: flex; align-items: center; gap: 10px; margin-top: 0.5rem; }
        .checkbox-group input { width: auto; }
        .row-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
        .btn-calculer {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; border: none; padding: 14px 28px; border-radius: 10px;
            font-size: 1rem; font-weight: 600; cursor: pointer; width: 100%;
            transition: transform 0.3s; margin-top: 1rem;
        }
        .btn-calculer:hover { transform: translateY(-2px); }
        .result-section {
            background: #f8f9fa; border-radius: 20px; padding: 1.5rem;
        }
        .result-title {
            font-size: 1.2rem; font-weight: 700; margin-bottom: 1rem;
            color: #667eea; display: flex; align-items: center; gap: 8px;
        }
        .price-breakdown { background: white; border-radius: 15px; padding: 1rem; }
        .breakdown-item {
            display: flex; justify-content: space-between; padding: 0.75rem 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .breakdown-total {
            font-weight: 800; font-size: 1.1rem; color: #4caf50;
            border-top: 2px solid #e0e0e0; margin-top: 0.5rem; padding-top: 0.75rem;
        }
        .total-price {
            text-align: center; margin-top: 1rem; padding: 1rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 15px; color: white;
        }
        .total-price .label { font-size: 0.9rem; opacity: 0.9; }
        .total-price .value { font-size: 2rem; font-weight: 800; }
        .tariff-info {
            background: #e3f2fd; border-radius: 15px; padding: 1rem;
            margin-bottom: 1.5rem; font-size: 0.85rem;
        }
        .tariff-info h4 { margin-bottom: 0.5rem; color: #1976d2; }
        .tariff-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 0.5rem; }
        @media (max-width: 768px) {
            body { padding: 1rem; }
            .two-columns { grid-template-columns: 1fr; }
            .row-2 { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        String userName = (String) session.getAttribute("userName");
    %>
    
    <div class="container">
        <div class="header">
            <div class="logo"><i class="fas fa-taxi"></i> TaxiFlow - Calculateur (Ariary)</div>
            <div>
                <% if(userName != null) { %>
                    <i class="fas fa-user-circle"></i> <%= userName %>
                <% } %>
                <a href="<%= contextPath %>/" class="btn-back"><i class="fas fa-home"></i> Accueil</a>
            </div>
        </div>
        
        <div class="calculator-card">
            <div class="card-header">
                <i class="fas fa-calculator" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                <h1>Calculateur de prix (Ariary)</h1>
                <p>Simulez le prix de votre course en Ariary (MGA)</p>
            </div>
            <div class="card-body">
                <div class="two-columns">
                    <div>
                        <div class="tariff-info">
                            <h4><i class="fas fa-info-circle"></i> Tarifs en vigueur (Ariary)</h4>
                            <div class="tariff-grid">
                                <span>📌 Prise en charge: <strong id="infoPrixBase">17 500 Ar</strong></span>
                                <span>🚗 Jour: <strong id="infoPrixKmJour">6 000 Ar/km</strong></span>
                                <span>🌙 Nuit: <strong id="infoPrixKmNuit">9 000 Ar/km</strong></span>
                                <span>⏱️ Attente: <strong id="infoPrixAttente">2 500 Ar/min</strong></span>
                                <span>🧳 Bagages: <strong id="infoFraisBagages">10 000 Ar</strong></span>
                                <span>🐕 Animaux: <strong id="infoFraisAnimaux">15 000 Ar</strong></span>
                                <span>🏢 Commission: <strong id="infoCommission">20%</strong></span>
                            </div>
                        </div>
                        
                        <form id="calculForm">
                            <div class="form-group">
                                <label><i class="fas fa-road"></i> Distance (km)</label>
                                <input type="number" id="distance" step="0.1" min="0" value="10" required>
                            </div>
                            
                            <div class="form-group">
                                <label><i class="fas fa-hourglass-half"></i> Temps d'attente (minutes)</label>
                                <input type="number" id="tempsAttente" step="1" min="0" value="0">
                            </div>
                            
                            <div class="row-2">
                                <div class="form-group">
                                    <label><i class="fas fa-suitcase"></i> Bagages</label>
                                    <select id="nombreBagages">
                                        <option value="0">0 bagage</option>
                                        <option value="1">1 bagage</option>
                                        <option value="2">2 bagages</option>
                                        <option value="3">3+ bagages</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label><i class="fas fa-calendar"></i> Date et heure</label>
                                    <input type="datetime-local" id="dateHeure">
                                </div>
                            </div>
                            
                            <div class="checkbox-group">
                                <input type="checkbox" id="animauxPresent">
                                <label><i class="fas fa-paw"></i> Présence d'animaux</label>
                            </div>
                            <div class="checkbox-group">
                                <input type="checkbox" id="reservation">
                                <label><i class="fas fa-calendar-check"></i> Sur réservation</label>
                            </div>
                            
                            <button type="button" class="btn-calculer" onclick="calculerPrix()">
                                <i class="fas fa-calculator"></i> Calculer le prix
                            </button>
                        </form>
                    </div>
                    
                    <div class="result-section">
                        <div class="result-title">
                            <i class="fas fa-chart-line"></i> Détail du calcul (Ariary)
                        </div>
                        <div id="resultContainer">
                            <div class="price-breakdown">
                                <div class="breakdown-item">
                                    <span>Prise en charge:</span>
                                    <span id="prixBase">0 Ar</span>
                                </div>
                                <div class="breakdown-item">
                                    <span>Distance parcourue:</span>
                                    <span id="prixDistance">0 Ar</span>
                                </div>
                                <div class="breakdown-item" id="attenteRow" style="display: none;">
                                    <span>Temps d'attente:</span>
                                    <span id="prixAttente">0 Ar</span>
                                </div>
                                <div class="breakdown-item" id="bagagesRow" style="display: none;">
                                    <span>Frais bagages:</span>
                                    <span id="prixBagages">0 Ar</span>
                                </div>
                                <div class="breakdown-item" id="animauxRow" style="display: none;">
                                    <span>Frais animaux:</span>
                                    <span id="prixAnimaux">0 Ar</span>
                                </div>
                                <div class="breakdown-item" id="majorationRow" style="display: none;">
                                    <span>Majorations:</span>
                                    <span id="prixMajorations">0 Ar</span>
                                </div>
                                <div class="breakdown-item breakdown-total">
                                    <span><strong>Total course:</strong></span>
                                    <span><strong id="totalCourse">0 Ar</strong></span>
                                </div>
                                <div class="breakdown-item">
                                    <span>Commission entreprise:</span>
                                    <span id="commission">0 Ar</span>
                                </div>
                                <div class="breakdown-item">
                                    <span><i class="fas fa-user"></i> Revenu chauffeur:</span>
                                    <span style="color: #4caf50; font-weight: bold;" id="revenuChauffeur">0 Ar</span>
                                </div>
                            </div>
                            <div class="total-price">
                                <div class="label">Prix total de la course</div>
                                <div class="value" id="grandTotal">0 Ar</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        let TARIFS = {
            prixBase: 17500,
            prixKmJour: 6000,
            prixKmNuit: 9000,
            prixAttenteMin: 2500,
            fraisBagages: 10000,
            fraisAnimaux: 15000,
            commission: 0.20,
            majorationNuit: 0.50,
            majorationWeekend: 0.20,
            fraisReservation: 10000
        };
        
        function formatNumber(n) {
            return n.toLocaleString('fr-FR');
        }
        
        function chargerTarifs() {
            fetch('${pageContext.request.contextPath}/api/parametres')
                .then(res => res.ok ? res.json() : {})
                .then(data => {
                    if(data) {
                        TARIFS.prixBase = data.prixPriseEnCharge || 17500;
                        TARIFS.prixKmJour = data.prixKmJour || 6000;
                        TARIFS.prixKmNuit = data.prixKmNuit || 9000;
                        TARIFS.prixAttenteMin = data.prixAttenteMin || 2500;
                        TARIFS.fraisBagages = data.fraisBagages || 10000;
                        TARIFS.fraisAnimaux = data.fraisAnimaux || 15000;
                        TARIFS.commission = (data.commissionEntreprise || 20) / 100;
                        TARIFS.majorationNuit = (data.majorationNuit || 50) / 100;
                        TARIFS.majorationWeekend = (data.majorationWeekend || 20) / 100;
                        TARIFS.fraisReservation = data.fraisReservation || 10000;
                        
                        document.getElementById('infoPrixBase').innerHTML = formatNumber(TARIFS.prixBase) + ' Ar';
                        document.getElementById('infoPrixKmJour').innerHTML = formatNumber(TARIFS.prixKmJour) + ' Ar/km';
                        document.getElementById('infoPrixKmNuit').innerHTML = formatNumber(TARIFS.prixKmNuit) + ' Ar/km';
                        document.getElementById('infoPrixAttente').innerHTML = formatNumber(TARIFS.prixAttenteMin) + ' Ar/min';
                        document.getElementById('infoFraisBagages').innerHTML = formatNumber(TARIFS.fraisBagages) + ' Ar';
                        document.getElementById('infoFraisAnimaux').innerHTML = formatNumber(TARIFS.fraisAnimaux) + ' Ar';
                        document.getElementById('infoCommission').innerHTML = (TARIFS.commission * 100) + '%';
                    }
                });
        }
        
        function estHeureNuit(date) {
            if(!date) return false;
            let heures = date.getHours();
            return (heures >= 22 || heures < 6);
        }
        
        function estWeekend(date) {
            if(!date) return false;
            let jour = date.getDay();
            return (jour === 0 || jour === 6);
        }
        
        function getPrixKm(date) {
            if(estHeureNuit(date)) return TARIFS.prixKmNuit;
            return TARIFS.prixKmJour;
        }
        
        function calculerMajorations(date, reservation) {
            let majorations = 0;
            if(estHeureNuit(date)) majorations += TARIFS.majorationNuit;
            if(estWeekend(date)) majorations += TARIFS.majorationWeekend;
            if(reservation) majorations += TARIFS.fraisReservation;
            return majorations;
        }
        
        function calculerPrix() {
            let distance = parseFloat(document.getElementById('distance').value) || 0;
            let tempsAttente = parseFloat(document.getElementById('tempsAttente').value) || 0;
            let nombreBagages = parseInt(document.getElementById('nombreBagages').value) || 0;
            let animauxPresent = document.getElementById('animauxPresent').checked;
            let reservation = document.getElementById('reservation').checked;
            
            let dateStr = document.getElementById('dateHeure').value;
            let date = dateStr ? new Date(dateStr) : new Date();
            
            let prixKm = getPrixKm(date);
            let majorations = calculerMajorations(date, reservation);
            
            let prixBase = TARIFS.prixBase;
            let prixDistance = distance * prixKm;
            let prixAttente = tempsAttente * TARIFS.prixAttenteMin;
            let fraisBagages = nombreBagages * TARIFS.fraisBagages;
            let fraisAnimaux = animauxPresent ? TARIFS.fraisAnimaux : 0;
            
            let sousTotal = prixBase + prixDistance + prixAttente + fraisBagages + fraisAnimaux;
            let totalCourse = sousTotal + majorations;
            let commission = totalCourse * TARIFS.commission;
            let revenuChauffeur = totalCourse - commission;
            
            document.getElementById('prixBase').innerHTML = formatNumber(prixBase) + ' Ar';
            document.getElementById('prixDistance').innerHTML = formatNumber(prixDistance) + ' Ar (à ' + formatNumber(prixKm) + ' Ar/km)';
            document.getElementById('prixAttente').innerHTML = formatNumber(prixAttente) + ' Ar';
            document.getElementById('prixBagages').innerHTML = formatNumber(fraisBagages) + ' Ar';
            document.getElementById('prixAnimaux').innerHTML = formatNumber(fraisAnimaux) + ' Ar';
            document.getElementById('prixMajorations').innerHTML = formatNumber(majorations) + ' Ar';
            document.getElementById('totalCourse').innerHTML = formatNumber(totalCourse) + ' Ar';
            document.getElementById('commission').innerHTML = formatNumber(commission) + ' Ar';
            document.getElementById('revenuChauffeur').innerHTML = formatNumber(revenuChauffeur) + ' Ar';
            document.getElementById('grandTotal').innerHTML = formatNumber(totalCourse) + ' Ar';
            
            document.getElementById('attenteRow').style.display = tempsAttente > 0 ? 'flex' : 'none';
            document.getElementById('bagagesRow').style.display = nombreBagages > 0 ? 'flex' : 'none';
            document.getElementById('animauxRow').style.display = animauxPresent ? 'flex' : 'none';
            document.getElementById('majorationRow').style.display = majorations > 0 ? 'flex' : 'none';
            
            let periodeNote = '';
            if(estHeureNuit(date)) periodeNote += ' 🌙 Tarif de nuit appliqué';
            if(estWeekend(date)) periodeNote += ' 📅 Majoration weekend';
            if(reservation) periodeNote += ' 📞 Frais de réservation';
            
            let noteElement = document.getElementById('periodeNote');
            if(!noteElement) {
                let div = document.createElement('div');
                div.id = 'periodeNote';
                div.style.cssText = 'margin-top: 10px; font-size: 0.8rem; color: #666; text-align: center;';
                document.querySelector('.price-breakdown').appendChild(div);
                noteElement = div;
            }
            noteElement.innerHTML = periodeNote;
        }
        
        chargerTarifs();
        setTimeout(() => { calculerPrix(); }, 500);
    </script>
</body>
</html>