<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Parking Management System</title>
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
        .register-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            width: 500px;
            max-width: 100%;
            padding: 40px;
        }
        .register-container h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: 500;
            font-size: 14px;
        }
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        input:focus {
            outline: none;
            border-color: #667eea;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
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
            margin-top: 10px;
        }
        button:hover {
            transform: translateY(-2px);
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .login-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }
        .login-link a {
            color: #667eea;
            text-decoration: none;
        }
        .login-link a:hover {
            text-decoration: underline;
        }
        .required {
            color: red;
        }
    </style>
</head>
<body>
    <div class="register-container">
        <h2>📝 Créer un compte</h2>
        
        <form action="inscription" method="post">
            <div class="form-row">
                <div class="form-group">
                    <label>Nom d'utilisateur <span class="required">*</span></label>
                    <input type="text" name="username" required autofocus>
                </div>
                <div class="form-group">
                    <label>Nom complet <span class="required">*</span></label>
                    <input type="text" name="fullName" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>Email <span class="required">*</span></label>
                    <input type="email" name="email" required>
                </div>
                <div class="form-group">
                    <label>Téléphone</label>
                    <input type="tel" name="phone">
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>Mot de passe <span class="required">*</span></label>
                    <input type="password" name="password" required>
                </div>
                <div class="form-group">
                    <label>Confirmer mot de passe <span class="required">*</span></label>
                    <input type="password" name="confirmPassword" required>
                </div>
            </div>
            
            <div class="form-group">
                <label>Plaque d'immatriculation</label>
                <input type="text" name="vehiclePlate" placeholder="ex: ABC-123">
            </div>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <button type="submit">✅ S'inscrire</button>
        </form>
        
        <div class="login-link">
            Déjà un compte ? <a href="login">Se connecter</a>
        </div>
    </div>
</body>
</html>