<%-- views/chauffeur-form.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${chauffeur != null ? 'Modifier' : 'Ajouter'} un chauffeur</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { max-width: 500px; margin: auto; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="email"], input[type="date"] { 
            width: 100%; padding: 8px; box-sizing: border-box; 
        }
        input[type="checkbox"] { margin-top: 5px; }
        .btn-submit { background-color: #4CAF50; color: white; padding: 10px 15px; 
                     border: none; cursor: pointer; width: 100%; }
        .btn-submit:hover { background-color: #45a049; }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>${chauffeur != null ? 'Modifier' : 'Ajouter'} un chauffeur</h1>
        
        <form action="chauffeurs" method="post">
            <c:if test="${chauffeur != null}">
                <input type="hidden" name="id" value="${chauffeur.id}">
                <input type="hidden" name="action" value="update">
            </c:if>
            <c:if test="${chauffeur == null}">
                <input type="hidden" name="action" value="insert">
            </c:if>
            
            <div class="form-group">
                <label for="nom">Nom:</label>
                <input type="text" id="nom" name="nom" value="${chauffeur.nom}" required>
            </div>
            
            <div class="form-group">
                <label for="prenom">Prénom:</label>
                <input type="text" id="prenom" name="prenom" value="${chauffeur.prenom}" required>
            </div>
            
            <div class="form-group">
                <label for="permis">Numéro de permis:</label>
                <input type="text" id="permis" name="permis" value="${chauffeur.permis}" required>
            </div>
            
            <div class="form-group">
                <label for="telephone">Téléphone:</label>
                <input type="text" id="telephone" name="telephone" value="${chauffeur.telephone}" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${chauffeur.email}" required>
            </div>
            
            <div class="form-group">
                <label for="dateEmbauche">Date d'embauche:</label>
                <input type="date" id="dateEmbauche" name="dateEmbauche" 
                       value="${chauffeur.dateEmbauche}" required>
            </div>
            
            <div class="form-group">
                <label>
                    <input type="checkbox" name="disponible" ${chauffeur.disponible ? 'checked' : ''}>
                    Disponible immédiatement
                </label>
            </div>
            
            <button type="submit" class="btn-submit">Enregistrer</button>
        </form>
        
        <p><a href="chauffeurs">Retour à la liste</a></p>
    </div>
</body>
</html>