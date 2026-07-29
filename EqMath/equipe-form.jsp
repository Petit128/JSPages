<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${empty equipe ? 'Nouvelle' : 'Modifier'} Équipe</title>
    <style>
        * { font-family: Arial, sans-serif; margin: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input { width: 300px; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        .btn { padding: 10px 20px; background-color: #4CAF50; color: white; 
               border: none; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>
    <h1>${empty equipe ? 'Créer une nouvelle équipe' : 'Modifier l\'équipe'}</h1>
    
    <form action="${pageContext.request.contextPath}/equipe/${empty equipe ? 'ajouter' : 'modifier'}" 
          method="post">
        
        <c:if test="${not empty equipe}">
            <input type="hidden" name="id" value="${equipe.id}">
        </c:if>
        
        <div class="form-group">
            <label>Nom de l'équipe:</label>
            <input type="text" name="nom" value="${equipe.nom}" required>
        </div>
        
        <div class="form-group">
            <label>Ville:</label>
            <input type="text" name="ville" value="${equipe.ville}" required>
        </div>
        
        <div class="form-group">
            <label>Date de création:</label>
            <input type="date" name="dateCreation" 
                   value="${equipe.dateCreation}" required>
        </div>
        
        <button type="submit" class="btn">${empty equipe ? 'Créer' : 'Modifier'}</button>
        <a href="${pageContext.request.contextPath}/equipe/">Annuler</a>
    </form>
</body>
</html>