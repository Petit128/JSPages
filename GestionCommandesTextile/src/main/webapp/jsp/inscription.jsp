<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription - Gestion Textile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 10px;
            padding: 40px;
            width: 100%;
            max-width: 450px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
        }
        button {
            width: 100%;
            padding: 12px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
        }
        button:hover {
            background: #5a67d8;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .login-link {
            text-align: center;
            margin-top: 20px;
        }
        .login-link a {
            color: #667eea;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏭 Inscription</h1>
        
        <c:if test="${not empty erreur}">
            <div class="error">❌ ${erreur}</div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/inscription" method="post">
            <div class="form-group">
                <label>Nom complet</label>
                <input type="text" name="nom" required value="${param.nom}">
            </div>
            
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required value="${param.email}">
            </div>
            
            <div class="form-group">
                <label>Mot de passe</label>
                <input type="password" name="motDePasse" required>
            </div>
            
            <div class="form-group">
                <label>Confirmer mot de passe</label>
                <input type="password" name="confirmMotDePasse" required>
            </div>
            
            <button type="submit">S'inscrire</button>
        </form>
        
        <div class="login-link">
            <p>Déjà inscrit ? <a href="${pageContext.request.contextPath}/login">Se connecter</a></p>
        </div>
    </div>
</body>
</html>