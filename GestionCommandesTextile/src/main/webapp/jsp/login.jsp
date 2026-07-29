<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Gestion Textile</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .login-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            width: 100%;
            max-width: 400px;
            padding: 40px;
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .login-header h1 {
            color: #333;
            font-size: 1.8em;
        }
        
        .login-header p {
            color: #666;
            margin-top: 10px;
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
        
        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
            transition: border-color 0.3s;
        }
        
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        
        button {
            width: 100%;
            padding: 12px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }
        
        button:hover {
            background: #5a67d8;
        }
        
        .alert {
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .register-link {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .register-link a {
            color: #667eea;
            text-decoration: none;
        }
        
        .register-link a:hover {
            text-decoration: underline;
        }
        
        .demo-accounts {
            margin-top: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
            font-size: 0.85em;
        }
        
        .demo-accounts h4 {
            margin-bottom: 10px;
            color: #555;
        }
        
        .demo-accounts p {
            margin: 5px 0;
            font-family: monospace;
        }
        
        @media (max-width: 480px) {
            .login-container {
                padding: 25px;
            }
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <h1>🏭 Gestion Textile</h1>
            <p>Connectez-vous à votre espace</p>
        </div>
        
        <c:if test="${not empty erreur}">
            <div class="alert alert-error">
                ❌ ${erreur}
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.succes}">
            <div class="alert alert-success">
                ✅ ${sessionScope.succes}
                <% session.removeAttribute("succes"); %>
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required 
                       placeholder="exemple@email.com" value="${param.email}">
            </div>
            
            <div class="form-group">
                <label for="motDePasse">Mot de passe</label>
                <input type="password" id="motDePasse" name="motDePasse" required 
                       placeholder="Votre mot de passe">
            </div>
            
            <button type="submit">Se connecter</button>
        </form>
        
        <div class="register-link">
            <p>Pas encore de compte ? <a href="${pageContext.request.contextPath}/inscription">S'inscrire</a></p>
        </div>
        
    </div>
</body>
</html>