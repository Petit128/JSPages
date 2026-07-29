<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Paramètres - Administration</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }
        
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .navbar a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
        }
        
        .container {
            max-width: 800px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .card h2 {
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }
        
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .help-text {
            font-size: 0.8em;
            color: #666;
            margin-top: 5px;
        }
        
        .btn {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1em;
        }
        
        .btn:hover {
            background: #5a67d8;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .info-box {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        
        .info-box p {
            margin: 5px 0;
            color: #1565c0;
        }
        
        .param-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px;
            border-bottom: 1px solid #eee;
        }
        
        .param-key {
            font-weight: bold;
            color: #333;
        }
        
        .param-value {
            color: #666;
        }
        
        .param-default {
            font-size: 0.8em;
            color: #999;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h2>⚙️ Administration - Paramètres</h2>
        <div>
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/utilisateurs">Utilisateurs</a>
            <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
        </div>
    </div>
    
    <div class="container">
        <div class="card">
            <h2>⚙️ Configuration du système</h2>
            
            <c:if test="${not empty sessionScope.succes}">
                <div class="alert alert-success">
                    ✅ ${sessionScope.succes}
                    <% session.removeAttribute("succes"); %>
                </div>
            </c:if>
            
            <c:if test="${not empty erreur}">
                <div class="alert alert-error">
                    ❌ ${erreur}
                </div>
            </c:if>
            
            <div class="info-box">
                <p>📌 Les paramètres définis ici affectent le calcul automatique des délais de production.</p>
                <p>💡 Les modifications sont appliquées immédiatement.</p>
            </div>
            
            <form action="${pageContext.request.contextPath}/admin/parametres/update" method="post">
                <div class="form-group">
                    <label>✂️ Durée étape COUPE (jours)</label>
                    <input type="number" name="duree_coupe_jours" 
                           value="${parametres['duree_coupe_jours']}" 
                           min="1" max="10" required>
                    <div class="help-text">Nombre de jours prévus pour l'étape de coupe</div>
                </div>
                
                <div class="form-group">
                    <label>🪡 Durée étape COUTURE (jours)</label>
                    <input type="number" name="duree_couture_jours" 
                           value="${parametres['duree_couture_jours']}" 
                           min="1" max="10" required>
                    <div class="help-text">Nombre de jours prévus pour l'étape de couture</div>
                </div>
                
                <div class="form-group">
                    <label>✨ Durée étape FINITION (jours)</label>
                    <input type="number" name="duree_finition_jours" 
                           value="${parametres['duree_finition_jours']}" 
                           min="1" max="10" required>
                    <div class="help-text">Nombre de jours prévus pour l'étape de finition</div>
                </div>
                
                <div class="form-group">
                    <label>🚚 Durée étape LIVRAISON (jours)</label>
                    <input type="number" name="duree_livraison_jours" 
                           value="${parametres['duree_livraison_jours']}" 
                           min="1" max="10" required>
                    <div class="help-text">Nombre de jours prévus pour l'étape de livraison</div>
                </div>
                
                <div class="form-group">
                    <label>⚠️ Seuil d'alerte retard (jours)</label>
                    <input type="number" name="seuil_alerte_retard" 
                           value="${parametres['seuil_alerte_retard']}" 
                           min="1" max="30" required>
                    <div class="help-text">Au-delà de ce nombre de jours, une alerte sera déclenchée</div>
                </div>
                
                <div class="form-group">
                    <label>📧 Email notifications</label>
                    <input type="email" name="notification_email" 
                           value="${parametres['notification_email']}" 
                           required>
                    <div class="help-text">Email qui recevra les notifications système</div>
                </div>
                
                <button type="submit" class="btn">💾 Enregistrer les modifications</button>
            </form>
        </div>
        
        <div class="card" style="margin-top: 20px;">
            <h2>📊 Résumé des durées</h2>
            <div class="param-row">
                <span class="param-key">Durée totale par défaut :</span>
                <span class="param-value">
                    <c:set var="total" value="0"/>
                    <c:set var="total" value="${total + parametres['duree_coupe_jours']}"/>
                    <c:set var="total" value="${total + parametres['duree_couture_jours']}"/>
                    <c:set var="total" value="${total + parametres['duree_finition_jours']}"/>
                    <c:set var="total" value="${total + parametres['duree_livraison_jours']}"/>
                    ${total} jours
                </span>
            </div>
            <div class="param-row">
                <span class="param-key">✂️ Coupe :</span>
                <span class="param-value">${parametres['duree_coupe_jours']} jour(s)</span>
            </div>
            <div class="param-row">
                <span class="param-key">🪡 Couture :</span>
                <span class="param-value">${parametres['duree_couture_jours']} jour(s)</span>
            </div>
            <div class="param-row">
                <span class="param-key">✨ Finition :</span>
                <span class="param-value">${parametres['duree_finition_jours']} jour(s)</span>
            </div>
            <div class="param-row">
                <span class="param-key">🚚 Livraison :</span>
                <span class="param-value">${parametres['duree_livraison_jours']} jour(s)</span>
            </div>
        </div>
    </div>
</body>
</html>