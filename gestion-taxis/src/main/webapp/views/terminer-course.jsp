<%-- views/terminer-course.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Terminer une course</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { max-width: 500px; margin: auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="number"] { 
            width: 100%; padding: 8px; box-sizing: border-box; 
        }
        .btn-submit { background-color: #4CAF50; color: white; padding: 10px 15px; 
                     border: none; cursor: pointer; width: 100%; }
        .btn-submit:hover { background-color: #45a049; }
        .info-box {
            background-color: #e3f2fd;
            border-left: 4px solid #2196F3;
            padding: 10px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>Terminer la course #${course.id}</h1>
        
        <div class="info-box">
            <p><strong>Client:</strong> ${course.clientNom}</p>
            <p><strong>Départ:</strong> ${course.adresseDepart}</p>
            <p><strong>Arrivée:</strong> ${course.adresseArrivee}</p>
            <p><strong>Distance estimée:</strong> ${course.distance} km</p>
        </div>
        
        <form action="courses" method="post">
            <input type="hidden" name="action" value="terminer">
            <input type="hidden" name="id" value="${course.id}">
            
            <div class="form-group">
                <label for="distanceReelle">Distance réelle (km):</label>
                <input type="number" id="distanceReelle" name="distanceReelle" 
                       step="0.1" min="0" value="${course.distance}" required>
            </div>
            
            <div class="form-group">
                <label for="dureeReelle">Durée réelle (minutes):</label>
                <input type="number" id="dureeReelle" name="dureeReelle" 
                       step="1" min="0" required>
            </div>
            
            <button type="submit" class="btn-submit">Terminer la course</button>
        </form>
        
        <p><a href="courses">Retour à la liste</a></p>
    </div>
</body>
</html>