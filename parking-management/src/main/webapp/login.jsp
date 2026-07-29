<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parking Management System - Connexion</title>
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
        }
        .login-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            width: 400px;
            padding: 40px;
        }
        .login-container h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
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
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        button {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: transform 0.2s;
        }
        button:hover {
            transform: translateY(-2px);
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
            margin-top: 15px;
            text-align: center;
        }
        .test-accounts {
            margin-top: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
        }
        .test-accounts h4 {
            margin-bottom: 10px;
            color: #333;
        }
        .test-accounts p {
            font-size: 12px;
            margin: 5px 0;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>🚗 Parking Management System</h2>
        
        <form action="login" method="post">
            <div class="form-group">
                <label>Nom d'utilisateur</label>
                <input type="text" name="username" required autofocus>
            </div>
            <div class="form-group">
                <label>Mot de passe</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit">Se connecter</button>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error"><%= request.getAttribute("error") %></div>
            <% } %>
            <!-- Ajouter ce lien dans login.jsp, après le bouton de connexion -->

			<div class="login-link" style="text-align: center; margin-top: 20px;">
			    Pas encore de compte ? <a href="inscription">Créer un compte</a>
			</div>
        </form>
        
    </div>
</body>
</html>