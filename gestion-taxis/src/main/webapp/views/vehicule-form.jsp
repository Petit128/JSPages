<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${vehicule != null ? 'Modifier' : 'Ajouter'} un véhicule</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { max-width: 500px; margin: auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="number"], select { 
            width: 100%; padding: 8px; box-sizing: border-box; 
        }
        input[type="checkbox"] { margin-top: 5px; }
        .btn-submit { background-color: #FF9800; color: white; padding: 10px 15px; 
                     border: none; cursor: pointer; width: 100%; }
        .btn-submit:hover { background-color: #F57C00; }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>${vehicule != null ? 'Modifier' : 'Ajouter'} un véhicule</h1>
        
        <form action="vehicules" method="post">
            <c:if test="${vehicule != null}">
                <input type="hidden" name="id" value="${vehicule.id}">
                <input type="hidden" name="action" value="update">
            </c:if>
            <c:if test="${vehicule == null}">
                <input type="hidden" name="action" value="insert">
            </c:if>
            
            <div class="form-group">
                <label for="immatriculation">Immatriculation:</label>
                <input type="text" id="immatriculation" name="immatriculation" 
                       value="${vehicule.immatriculation}" required>
            </div>
            
            <div class="form-group">
                <label for="marque">Marque:</label>
                <input type="text" id="marque" name="marque" value="${vehicule.marque}" required>
            </div>
            
            <div class="form-group">
                <label for="modele">Modèle:</label>
                <input type="text" id="modele" name="modele" value="${vehicule.modele}" required>
            </div>
            
            <div class="form-group">
                <label for="annee">Année:</label>
                <input type="number" id="annee" name="annee" value="${vehicule.annee}" 
                       min="1900" max="2026" required>
            </div>
            
            <div class="form-group">
                <label for="couleur">Couleur:</label>
                <input type="text" id="couleur" name="couleur" value="${vehicule.couleur}" required>
            </div>
            
            <div class="form-group">
                <label for="nombrePlaces">Nombre de places:</label>
                <input type="number" id="nombrePlaces" name="nombrePlaces" 
                       value="${vehicule.nombrePlaces}" min="2" max="9" required>
            </div>
            
            <div class="form-group">
                <label for="kilometrage">Kilométrage:</label>
                <input type="number" id="kilometrage" name="kilometrage" 
                       value="${vehicule.kilometrage}" step="0.1" min="0" required>
            </div>
            
            <div class="form-group">
                <label for="etat">État:</label>
                <select id="etat" name="etat" required>
                    <option value="Bon" ${vehicule.etat == 'Bon' ? 'selected' : ''}>Bon</option>
                    <option value="Moyen" ${vehicule.etat == 'Moyen' ? 'selected' : ''}>Moyen</option>
                    <option value="Mauvais" ${vehicule.etat == 'Mauvais' ? 'selected' : ''}>Mauvais</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>
                    <input type="checkbox" name="disponible" ${vehicule.disponible ? 'checked' : ''}>
                    Disponible immédiatement
                </label>
            </div>
            
            <button type="submit" class="btn-submit">Enregistrer</button>
        </form>
        
        <p><a href="vehicules">Retour à la liste</a></p>
    </div>
</body>
</html>