<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle Commande - Gestion Textile</title>
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
            flex-wrap: wrap;
        }
        
        .navbar h1 {
            font-size: 1.5em;
        }
        
        .nav-links a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
            padding: 5px 10px;
            border-radius: 5px;
        }
        
        .nav-links a:hover {
            background: rgba(255,255,255,0.2);
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
            margin-bottom: 5px;
            color: #555;
            font-weight: 500;
        }
        
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        
        input:focus, select:focus {
            outline: none;
            border-color: #667eea;
        }
        
        input:disabled, input[readonly] {
            background-color: #f5f5f5;
            cursor: not-allowed;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            font-size: 1em;
            text-align: center;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
            flex: 1;
        }
        
        .btn-secondary {
            background: #95a5a6;
            color: white;
            flex: 1;
            text-align: center;
        }
        
        .btn-primary:hover {
            background: #5a67d8;
        }
        
        .btn-secondary:hover {
            background: #7f8c8d;
        }
        
        .info-message {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-size: 0.9em;
            color: #1565c0;
        }
        
        @media (max-width: 600px) {
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .navbar {
                flex-direction: column;
                text-align: center;
                gap: 10px;
            }
            
            .nav-links {
                margin-top: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>🏭 Gestion Commandes Textile</h1>
        <div class="nav-links">
            <c:choose>
                <c:when test="${sessionScope.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                </c:when>
                <c:when test="${sessionScope.role == 'CLIENT'}">
                    <a href="${pageContext.request.contextPath}/client/dashboard">Mon espace</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/commande/">Commandes</a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
        </div>
    </div>
    
    <div class="container">
        <div class="card">
            <h2>📝 Nouvelle Commande</h2>
            
            <!-- Message d'information selon le rôle -->
            <c:if test="${sessionScope.role == 'CLIENT'}">
                <div class="info-message">
                    ℹ️ En tant que client, vos commandes vous seront automatiquement associées.
                    Vous pourrez suivre leur avancement dans votre espace client.
                </div>
            </c:if>
            
            <c:if test="${sessionScope.role == 'ADMIN' or sessionScope.role == 'RESPONSABLE_PRODUCTION' or sessionScope.role == 'OPERATEUR'}">
                <div class="info-message">
                    ℹ️ Vous pouvez créer une commande pour le compte d'un client.
                    Le client pourra suivre l'avancement depuis son espace.
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/commande/creer" method="post">
                
                <!-- Champ client - adapté selon le rôle -->
                <c:choose>
                    <c:when test="${sessionScope.role == 'CLIENT'}">
                        <!-- Pour un client, le champ est pré-rempli et désactivé -->
                        <div class="form-group">
                            <label for="client">Nom du client *</label>
                            <input type="text" id="client" name="client" 
                                   value="${sessionScope.nom}" readonly required>
                            <small style="color: #666; display: block; margin-top: 5px;">
                                Le nom est automatiquement celui de votre compte
                            </small>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Pour admin/production, champ libre -->
                        <div class="form-group">
                            <label for="client">Nom du client *</label>
                            <input type="text" id="client" name="client" required 
                                   placeholder="Nom du client ou de l'entreprise">
                        </div>
                    </c:otherwise>
                </c:choose>
                
                <div class="form-group">
                    <label for="typeArticle">Type d'article *</label>
                    <input type="text" id="typeArticle" name="typeArticle" 
                           placeholder="Ex: T-Shirt, Robe, Pantalon..." required>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="quantite">Quantité *</label>
                        <input type="number" id="quantite" name="quantite" 
                               min="1" value="1" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="taille">Taille</label>
                        <select id="taille" name="taille">
                            <option value="">Sélectionner</option>
                            <option value="XS">XS</option>
                            <option value="S">S</option>
                            <option value="M">M</option>
                            <option value="L">L</option>
                            <option value="XL">XL</option>
                            <option value="XXL">XXL</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="couleur">Couleur</label>
                        <input type="text" id="couleur" name="couleur" 
                               placeholder="Ex: Rouge, Bleu, Vert...">
                    </div>
                    
                    <div class="form-group">
                        <label for="dateLivraison">Date de livraison prévue *</label>
                        <input type="date" id="dateLivraison" name="dateLivraison" required>
                        <small style="color: #666; display: block; margin-top: 5px;">
                            Date à laquelle le client souhaite recevoir sa commande
                        </small>
                    </div>
                </div>
                
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">✅ Créer la commande</button>
                    <a href="javascript:history.back()" class="btn btn-secondary">❌ Annuler</a>
                </div>
            </form>
        </div>
        
        <!-- Informations sur les délais -->
        <div class="card" style="margin-top: 20px;">
            <h2>📊 Informations sur les délais</h2>
            <div style="padding: 10px 0;">
                <p><strong>⏱️ Délais de production standards :</strong></p>
                <ul style="margin-left: 20px; margin-top: 10px;">
                    <li>✂️ Coupe : 2 jours</li>
                    <li>🪡 Couture : 3 jours</li>
                    <li>✨ Finition : 2 jours</li>
                    <li>🚚 Livraison : 1 jour</li>
                </ul>
                <p style="margin-top: 15px; color: #666;">
                    <strong>Total :</strong> 8 jours ouvrés minimum
                </p>
            </div>
        </div>
    </div>
    
    <script>
        // Set minimum date to today
        var today = new Date();
        var minDate = new Date(today.setDate(today.getDate() + 8)); // Minimum 8 jours
        document.getElementById('dateLivraison').min = minDate.toISOString().split('T')[0];
        
        // Set default date to 15 days from now
        var defaultDate = new Date();
        defaultDate.setDate(defaultDate.getDate() + 15);
        document.getElementById('dateLivraison').value = defaultDate.toISOString().split('T')[0];
        
        // Validation supplémentaire
        document.querySelector('form').addEventListener('submit', function(e) {
            var quantite = document.getElementById('quantite').value;
            if (quantite < 1) {
                e.preventDefault();
                alert('La quantité doit être au moins 1');
                return false;
            }
            
            var dateLivraison = new Date(document.getElementById('dateLivraison').value);
            var today = new Date();
            var minDate = new Date();
            minDate.setDate(minDate.getDate() + 8);
            
            if (dateLivraison < minDate) {
                e.preventDefault();
                alert('La date de livraison doit être au moins 8 jours à partir d\'aujourd\'hui pour respecter les délais de production.');
                return false;
            }
            
            return true;
        });
    </script>
</body>
</html>