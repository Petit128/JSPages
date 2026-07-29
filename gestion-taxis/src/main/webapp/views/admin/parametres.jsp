<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Paramètres - TaxiFlow</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: #f5f7fa;
        }
        .header {
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 1rem 2rem;
        }
        .container { max-width: 1200px; margin: 2rem auto; padding: 0 2rem; }
        .settings-card {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .settings-title {
            font-size: 1.3rem;
            margin-bottom: 1rem;
            color: #667eea;
        }
        .settings-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 1.5rem;
        }
        .setting-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0.75rem;
            border-bottom: 1px solid #f0f0f0;
        }
        .setting-item input {
            width: 150px;
            padding: 8px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
        }
        .btn-save {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 10px;
            cursor: pointer;
            margin-top: 1rem;
        }
        .info-text { font-size: 0.85rem; color: #666; margin-top: 0.25rem; }
        .currency { color: #4caf50; font-weight: bold; }
    </style>
</head>
<body>
    <div class="header">
        <div class="logo" style="font-size: 1.5rem; font-weight: 800; color: #667eea;">
            <i class="fas fa-taxi"></i> TaxiFlow - Paramètres (Ariary)
        </div>
    </div>
    
    <div class="container">
        <div class="settings-card">
            <h2 class="settings-title"><i class="fas fa-euro-sign"></i> Tarifs (Ariary - Ar)</h2>
            <form id="tarifsForm">
                <div class="settings-grid">
                    <div class="setting-item">
                        <label>Prix prise en charge (Ar)</label>
                        <input type="number" id="prixBase" value="17500" step="500">
                    </div>
                    <div class="setting-item">
                        <label>Prix au km (jour) (Ar/km)</label>
                        <input type="number" id="prixKmJour" value="6000" step="500">
                    </div>
                    <div class="setting-item">
                        <label>Prix au km (nuit) (Ar/km)</label>
                        <input type="number" id="prixKmNuit" value="9000" step="500">
                    </div>
                    <div class="setting-item">
                        <label>Prix attente (Ar/min)</label>
                        <input type="number" id="prixAttente" value="2500" step="100">
                    </div>
                    <div class="setting-item">
                        <label>Frais bagages (Ar)</label>
                        <input type="number" id="fraisBagages" value="10000" step="1000">
                    </div>
                    <div class="setting-item">
                        <label>Frais animaux (Ar)</label>
                        <input type="number" id="fraisAnimaux" value="15000" step="1000">
                    </div>
                </div>
            </form>
        </div>
        
        <div class="settings-card">
            <h2 class="settings-title"><i class="fas fa-percent"></i> Commissions</h2>
            <div class="settings-grid">
                <div class="setting-item">
                    <label>Commission entreprise (%)</label>
                    <input type="number" id="commission" value="20" step="1">
                </div>
                <div class="info-text">Le chauffeur reçoit le reste après commission</div>
            </div>
        </div>
        
        <div class="settings-card">
            <h2 class="settings-title"><i class="fas fa-moon"></i> Majorations</h2>
            <div class="settings-grid">
                <div class="setting-item">
                    <label>Majoration nuit (%)</label>
                    <input type="number" id="majorationNuit" value="50" step="5">
                </div>
                <div class="setting-item">
                    <label>Majoration weekend (%)</label>
                    <input type="number" id="majorationWeekend" value="20" step="5">
                </div>
                <div class="setting-item">
                    <label>Frais réservation (Ar)</label>
                    <input type="number" id="fraisReservation" value="10000" step="500">
                </div>
            </div>
        </div>
        
        <button class="btn-save" onclick="saveSettings()"><i class="fas fa-save"></i> Enregistrer tous les paramètres</button>
    </div>
    
    <script>
        function saveSettings() {
            const settings = {
                prixBase: document.getElementById('prixBase').value,
                prixKmJour: document.getElementById('prixKmJour').value,
                prixKmNuit: document.getElementById('prixKmNuit').value,
                prixAttente: document.getElementById('prixAttente').value,
                fraisBagages: document.getElementById('fraisBagages').value,
                fraisAnimaux: document.getElementById('fraisAnimaux').value,
                commission: document.getElementById('commission').value,
                majorationNuit: document.getElementById('majorationNuit').value,
                majorationWeekend: document.getElementById('majorationWeekend').value,
                fraisReservation: document.getElementById('fraisReservation').value
            };
            
            fetch('api/parametres', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(settings)
            })
            .then(response => response.json())
            .then(data => {
                if(data.success) {
                    alert('Paramètres enregistrés avec succès !');
                } else {
                    alert('Erreur lors de l\'enregistrement');
                }
            });
        }
        
        fetch('api/parametres')
            .then(response => response.json())
            .then(data => {
                if(data) {
                    document.getElementById('prixBase').value = data.prixPriseEnCharge || 17500;
                    document.getElementById('prixKmJour').value = data.prixKmJour || 6000;
                    document.getElementById('prixKmNuit').value = data.prixKmNuit || 9000;
                    document.getElementById('prixAttente').value = data.prixAttenteMin || 2500;
                    document.getElementById('fraisBagages').value = data.fraisBagages || 10000;
                    document.getElementById('fraisAnimaux').value = data.fraisAnimaux || 15000;
                    document.getElementById('commission').value = data.commissionEntreprise || 20;
                    document.getElementById('majorationNuit').value = data.majorationNuit || 50;
                    document.getElementById('majorationWeekend').value = data.majorationWeekend || 20;
                    document.getElementById('fraisReservation').value = data.fraisReservation || 10000;
                }
            });
    </script>
</body>
</html>