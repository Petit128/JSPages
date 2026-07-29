<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails Commande - Gestion Textile</title>
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
        }
        
        .navbar a {
            color: white;
            text-decoration: none;
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .card h3 {
            color: #333;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
        }
        
        .info-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        
        .info-label {
            font-weight: bold;
            color: #555;
        }
        
        .info-value {
            color: #333;
        }
        
        .workflow {
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
            margin: 20px 0;
        }
        
        .etape {
            flex: 1;
            min-width: 200px;
            background: #f8f9fa;
            border-radius: 8px;
            padding: 15px;
            text-align: center;
            position: relative;
        }
        
        .etape.terminee {
            background: #e8f5e9;
            border-left: 4px solid #4caf50;
        }
        
        .etape.en-cours {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
        }
        
        .etape.en-attente {
            background: #fff3e0;
            border-left: 4px solid #ff9800;
        }
        
        .etape h4 {
            margin-bottom: 10px;
            color: #333;
        }
        
        .statut-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: bold;
            margin-top: 10px;
        }
        
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin: 5px;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-success {
            background: #48bb78;
            color: white;
        }
        
        .btn-primary {
            background: #4299e1;
            color: white;
        }
        
        .btn-warning {
            background: #ed8936;
            color: white;
        }
        
        .historique {
            max-height: 300px;
            overflow-y: auto;
        }
        
        .historique-item {
            padding: 10px;
            border-left: 3px solid #667eea;
            margin-bottom: 10px;
            background: #f8f9fa;
        }
        
        .historique-date {
            font-size: 0.85em;
            color: #666;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-warning {
            background: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
        }
        
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            justify-content: center;
            align-items: center;
        }
        
        .modal-content {
            background: white;
            padding: 20px;
            border-radius: 8px;
            width: 400px;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <a href="${pageContext.request.contextPath}/commande/">← Retour aux commandes</a>
    </div>
    
    <div class="container">
        <c:if test="${not empty delais.enRetard && delais.enRetard}">
            <div class="alert alert-warning">
                ⚠️ ATTENTION : Cette commande est en retard de ${delais.retardJours} jour(s) !
            </div>
        </c:if>
        
        <!-- Informations commande -->
        <div class="card">
            <h3>📋 Informations générales</h3>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">N° Commande :</span>
                    <span class="info-value">#${commande.id}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Client :</span>
                    <span class="info-value">${commande.client}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Article :</span>
                    <span class="info-value">${commande.typeArticle}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Quantité :</span>
                    <span class="info-value">${commande.quantite}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Taille/Couleur :</span>
                    <span class="info-value">${commande.taille} / ${commande.couleur}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Date commande :</span>
                    <span class="info-value"><fmt:formatDate value="${commande.dateCommande}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">Livraison prévue :</span>
                    <span class="info-value"><fmt:formatDate value="${commande.dateLivraisonPrevue}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">Livraison estimée :</span>
                    <span class="info-value"><fmt:formatDate value="${delais.dateLivraisonEstimee}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-item">
                    <span class="info-label">Statut :</span>
                    <span class="info-value">${commande.statut}</span>
                </div>
            </div>
        </div>
        
        <!-- Workflow de production -->
        <div class="card">
            <h3>⚙️ Workflow de production</h3>
            <div class="workflow">
                <c:forEach items="${etapes}" var="etape">
                    <div class="etape 
                        <c:choose>
                            <c:when test="${etape.statut == 'TERMINEE'}">terminee</c:when>
                            <c:when test="${etape.statut == 'EN_COURS'}">en-cours</c:when>
                            <c:otherwise>en-attente</c:otherwise>
                        </c:choose>">
                        <h4>
                            <c:choose>
                                <c:when test="${etape.nomEtape == 'COUPE'}">✂️ Coupe</c:when>
                                <c:when test="${etape.nomEtape == 'COUTURE'}">🪡 Couture</c:when>
                                <c:when test="${etape.nomEtape == 'FINITION'}">✨ Finition</c:when>
                                <c:when test="${etape.nomEtape == 'LIVRAISON'}">🚚 Livraison</c:when>
                            </c:choose>
                        </h4>
                        
                        <div class="statut-badge">
                            <c:choose>
                                <c:when test="${etape.statut == 'TERMINEE'}">✅ Terminée</c:when>
                                <c:when test="${etape.statut == 'EN_COURS'}">🔄 En cours</c:when>
                                <c:otherwise>⏸️ En attente</c:otherwise>
                            </c:choose>
                        </div>
                        
                        <c:if test="${etape.dateDebut != null}">
                            <div style="margin-top: 10px; font-size: 0.9em;">
                                Début: <fmt:formatDate value="${etape.dateDebut}" pattern="dd/MM/yyyy"/>
                            </div>
                        </c:if>
                        
                        <c:if test="${etape.dateFin != null}">
                            <div style="font-size: 0.9em;">
                                Fin: <fmt:formatDate value="${etape.dateFin}" pattern="dd/MM/yyyy"/>
                            </div>
                        </c:if>
                        
                        <c:if test="${etape.dateFinPrevue != null}">
                            <div style="font-size: 0.85em; color: #666;">
                                Prévue: <fmt:formatDate value="${etape.dateFinPrevue}" pattern="dd/MM/yyyy"/>
                            </div>
                        </c:if>
                        
                        <c:if test="${etape.responsable != null}">
                            <div style="font-size: 0.85em; margin-top: 5px;">
                                Responsable: ${etape.responsable}
                            </div>
                        </c:if>
                        
                        <c:if test="${etape.statut == 'EN_ATTENTE'}">
                            <form action="${pageContext.request.contextPath}/commande/demarrerEtape" method="post" style="margin-top: 10px;">
                                <input type="hidden" name="commandeId" value="${commande.id}">
                                <input type="hidden" name="nomEtape" value="${etape.nomEtape}">
                                <button type="submit" class="btn btn-success">Démarrer</button>
                            </form>
                        </c:if>
                        
                        <c:if test="${etape.statut == 'EN_COURS'}">
                            <button onclick="ouvrirModal(${commande.id}, '${etape.nomEtape}')" class="btn btn-primary">Terminer</button>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <!-- Historique -->
        <div class="card">
            <h3>📜 Historique des actions</h3>
            <div class="historique">
                <c:forEach items="${historique}" var="action">
                    <div class="historique-item">
                        <div class="historique-date">
                            <fmt:formatDate value="${action.dateAction}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        </div>
                        <div><strong>${action.action}</strong></div>
                        <div style="font-size: 0.9em; color: #666;">${action.details}</div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty historique}">
                    <p>Aucun historique disponible.</p>
                </c:if>
            </div>
        </div>
    </div>
    
    <!-- Modal pour terminer une étape -->
    <div id="modal" class="modal">
        <div class="modal-content">
            <h3>Terminer l'étape</h3>
            <form id="terminerForm" method="post">
                <div class="form-group">
                    <label for="commentaire">Commentaire :</label>
                    <textarea id="commentaire" name="commentaire" rows="3" style="width: 100%; padding: 8px;"></textarea>
                </div>
                <div style="display: flex; gap: 10px; margin-top: 15px;">
                    <button type="submit" class="btn btn-success">Valider</button>
                    <button type="button" onclick="fermerModal()" class="btn btn-warning">Annuler</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        function ouvrirModal(commandeId, nomEtape) {
            var modal = document.getElementById('modal');
            var form = document.getElementById('terminerForm');
            form.action = '${pageContext.request.contextPath}/commande/terminerEtape';
            form.innerHTML += '<input type="hidden" name="commandeId" value="' + commandeId + '">';
            form.innerHTML += '<input type="hidden" name="nomEtape" value="' + nomEtape + '">';
            modal.style.display = 'flex';
        }
        
        function fermerModal() {
            var modal = document.getElementById('modal');
            modal.style.display = 'none';
            document.getElementById('commentaire').value = '';
        }
        
        window.onclick = function(event) {
            var modal = document.getElementById('modal');
            if (event.target == modal) {
                fermerModal();
            }
        }
    </script>
</body>
</html>