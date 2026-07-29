<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${course != null ? 'Modifier' : 'Nouvelle'} course</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { max-width: 500px; margin: auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="datetime-local"], input[type="number"] { 
            width: 100%; padding: 8px; box-sizing: border-box; 
        }
        .btn-submit { background-color: #2196F3; color: white; padding: 10px 15px; 
                     border: none; cursor: pointer; width: 100%; }
        .btn-submit:hover { background-color: #1976D2; }
        .btn-back { background-color: #f0f0f0; color: #333; padding: 10px 15px; 
                    border: none; cursor: pointer; width: 100%; margin-top: 10px; text-decoration: none; display: inline-block; text-align: center; }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>${course != null ? 'Modifier' : 'Nouvelle'} course</h1>
        
        <form action="courses" method="post">
            <c:if test="${course != null}">
                <input type="hidden" name="id" value="${course.id}">
                <input type="hidden" name="action" value="update">
            </c:if>
            <c:if test="${course == null}">
                <input type="hidden" name="action" value="attribuer">
            </c:if>
            
            <div class="form-group">
                <label for="clientNom">Nom du client:</label>
                <input type="text" id="clientNom" name="clientNom" value="${course.clientNom}" required>
            </div>
            
            <div class="form-group">
                <label for="clientTelephone">Téléphone:</label>
                <input type="text" id="clientTelephone" name="clientTelephone" value="${course.clientTelephone}" required>
            </div>
            
            <div class="form-group">
                <label for="adresseDepart">Adresse de départ:</label>
                <input type="text" id="adresseDepart" name="adresseDepart" value="${course.adresseDepart}" required>
            </div>
            
            <div class="form-group">
                <label for="adresseArrivee">Adresse d'arrivée:</label>
                <input type="text" id="adresseArrivee" name="adresseArrivee" value="${course.adresseArrivee}" required>
            </div>
            
            <div class="form-group">
                <label for="dateHeure">Date et heure:</label>
                <input type="datetime-local" id="dateHeure" name="dateHeure" value="${course.dateHeure}">
            </div>
            
            <div class="form-group">
                <label for="distance">Distance (km):</label>
                <input type="number" id="distance" name="distance" step="0.1" min="0" value="${course.distance}" required>
            </div>
            
            <c:if test="${course == null}">
                <div class="form-group">
                    <label>
                        <input type="checkbox" name="assignAuto" value="true" checked>
                        Assigner automatiquement à un chauffeur disponible
                    </label>
                    <small style="color: #666; display: block; margin-top: 5px;">
                        Si décoché, la course sera créée sans chauffeur et pourra être assignée manuellement plus tard.
                    </small>
                </div>
            </c:if>
            
            <button type="submit" class="btn-submit">${course != null ? 'Modifier' : 'Créer'} la course</button>
        </form>
        
        <a href="courses" class="btn-back">Retour à la liste</a>
    </div>
</body>
</html>