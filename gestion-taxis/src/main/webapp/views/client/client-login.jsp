<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Client - TaxiFlow</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 2rem;
        }
        .container { max-width: 450px; margin: 0 auto; }
        .login-card {
            background: white;
            border-radius: 30px;
            overflow: hidden;
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            text-align: center;
        }
        .card-header h1 { font-size: 1.8rem; margin-bottom: 0.5rem; }
        .card-body { padding: 2rem; }
        .form-group { margin-bottom: 1.5rem; }
        label {
            display: block; margin-bottom: 0.5rem; font-weight: 600; color: #333;
        }
        input {
            width: 100%; padding: 12px; border: 2px solid #e0e0e0;
            border-radius: 10px; font-size: 1rem;
        }
        input:focus { outline: none; border-color: #667eea; }
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; border: none; padding: 14px; border-radius: 10px;
            font-size: 1rem; font-weight: 600; cursor: pointer; width: 100%;
            transition: transform 0.3s; margin-top: 1rem;
        }
        .btn-login:hover { transform: translateY(-2px); }
        .register-link {
            text-align: center; margin-top: 1.5rem; color: #666;
        }
        .register-link a { color: #667eea; text-decoration: none; font-weight: 600; }
        .error-message {
            background: #ffebee; color: #f44336; padding: 12px;
            border-radius: 10px; margin-bottom: 1rem; text-align: center;
        }
        .btn-back {
            display: inline-block; margin-top: 1rem; text-align: center;
            color: #667eea; text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-card">
            <div class="card-header">
                <i class="fas fa-taxi" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                <h1>Espace Client</h1>
                <p>Connectez-vous à votre compte</p>
            </div>
            <div class="card-body">
                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message"><i class="fas fa-exclamation-triangle"></i> <%= request.getAttribute("error") %></div>
                <% } %>
                
                <form action="${pageContext.request.contextPath}/client/login" method="post">
                    <div class="form-group">
                        <label><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" name="email" required placeholder="exemple@email.com">
                    </div>
                    <div class="form-group">
                        <label><i class="fas fa-lock"></i> Mot de passe</label>
                        <input type="password" name="password" required>
                    </div>
                    <button type="submit" class="btn-login">
                        <i class="fas fa-sign-in-alt"></i> Se connecter
                    </button>
                </form>
                
                <div class="register-link">
                    Pas encore de compte ? <a href="${pageContext.request.contextPath}/register">Créer un compte</a>
                </div>
                <div style="text-align: center; margin-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/" class="btn-back"><i class="fas fa-arrow-left"></i> Retour à l'accueil</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>