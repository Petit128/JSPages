<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaxiFlow - Gestion de flotte de taxis</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        
        /* Header */
        .header {
            background: rgba(255,255,255,0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
            position: sticky;
            top: 0;
            z-index: 1000;
        }
        .nav-container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 1rem 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .logo {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 1.5rem;
            font-weight: 800;
            color: #667eea;
            text-decoration: none;
        }
        .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .user-avatar {
            width: 40px;
            height: 40px;
            background: #667eea;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        }
        .btn-logout {
            background: #f44336;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 8px;
            cursor: pointer;
        }
        
        /* Hero */
        .hero {
            text-align: center;
            padding: 4rem 2rem;
            color: white;
        }
        .hero h1 { font-size: 3rem; margin-bottom: 1rem; }
        
        /* Menu Grid */
        .menu-section {
            max-width: 1400px;
            margin: 2rem auto;
            padding: 0 2rem;
        }
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 2rem;
        }
        .menu-card {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            color: inherit;
            display: block;
        }
        .menu-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
        }
        .menu-icon { font-size: 3rem; margin-bottom: 1rem; }
        .menu-card h3 { font-size: 1.3rem; margin-bottom: 0.5rem; color: #667eea; }
        
        /* Login Modal */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 2000;
            justify-content: center;
            align-items: center;
        }
        .modal-content {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            width: 400px;
            max-width: 90%;
        }
        .form-group { margin-bottom: 1rem; }
        input {
            width: 100%;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
        }
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
        }
        
        @media (max-width: 768px) {
            .nav-container { flex-direction: column; gap: 1rem; }
            .menu-grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
        String userRole = (String) session.getAttribute("userRole");
        String userName = (String) session.getAttribute("userName");
        boolean isLoggedIn = userRole != null;
    %>
    
    <header class="header">
        <div class="nav-container">
            <a href="<%= contextPath %>/" class="logo">
                <i class="fas fa-taxi"></i>
                <span>TaxiFlow</span>
            </a>
            <div class="user-info">
                <% if(isLoggedIn) { %>
                    <div class="user-avatar"><i class="fas fa-user"></i></div>
                    <span><%= userName %> (<%= userRole %>)</span>
                    <a href="<%= contextPath %>/logout" class="btn-logout"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
                <% } else { %>
                    <button onclick="showLoginModal()" class="btn-login" style="width: auto;">Connexion</button>
                <% } %>
            </div>
        </div>
    </header>
    
    <div class="hero">
        <h1>Gestion de Flotte de Taxis</h1>
        <p>Optimisez votre flotte, maximisez vos revenus</p>
    </div>
    
    <div class="menu-section">
        <div class="menu-grid">
            
            <!-- ESPACE CLIENT -->
            <a href="${pageContext.request.contextPath}/client/dashboard" class="menu-card">
                <div class="menu-icon"><i class="fas fa-user-circle"></i></div>
                <h3>Espace Client</h3>
                <p>Gérez vos courses et votre compte</p>
            </a>
            
            <!-- INSCRIPTION CLIENT -->
            <a href="${pageContext.request.contextPath}/register" class="menu-card">
                <div class="menu-icon"><i class="fas fa-user-plus"></i></div>
                <h3>S'inscrire</h3>
                <p>Créez votre compte client</p>
            </a>
            
            <!-- GESTION CHAUFFEURS (Admin/Opérateur uniquement) -->
            <% if(!isLoggedIn || "ADMIN".equals(userRole) || "OPERATEUR".equals(userRole)) { %>
                <a href="<%= contextPath %>/chauffeurs" class="menu-card">
                    <div class="menu-icon"><i class="fas fa-users"></i></div>
                    <h3>Chauffeurs</h3>
                    <p>Gérer les chauffeurs et leurs disponibilités</p>
                </a>
            <% } %>
            
            <!-- GESTION VEHICULES (Admin/Opérateur uniquement) -->
            <% if(!isLoggedIn || "ADMIN".equals(userRole) || "OPERATEUR".equals(userRole)) { %>
                <a href="<%= contextPath %>/vehicules" class="menu-card">
                    <div class="menu-icon"><i class="fas fa-car"></i></div>
                    <h3>Véhicules</h3>
                    <p>Gérer le parc automobile</p>
                </a>
            <% } %>
            
            <!-- GESTION COURSES (Admin/Opérateur uniquement) -->
            <% if(!isLoggedIn || "ADMIN".equals(userRole) || "OPERATEUR".equals(userRole)) { %>
                <a href="<%= contextPath %>/courses" class="menu-card">
                    <div class="menu-icon"><i class="fas fa-map-marker-alt"></i></div>
                    <h3>Courses</h3>
                    <p>Gérer et attribuer les courses</p>
                </a>
            <% } %>
            
            <!-- PARAMETRES (Admin uniquement) -->
            <% if(!isLoggedIn || "ADMIN".equals(userRole)) { %>
                <a href="<%= contextPath %>/parametres" class="menu-card">
                    <div class="menu-icon"><i class="fas fa-sliders-h"></i></div>
                    <h3>Paramètres</h3>
                    <p>Configurer les tarifs et commissions</p>
                </a>
            <% } %>
            
            <!-- STATISTIQUES -->
            <a href="<%= contextPath %>/statistiques" class="menu-card">
                <div class="menu-icon"><i class="fas fa-chart-line"></i></div>
                <h3>Statistiques</h3>
                <p>Analyser les performances</p>
            </a>
            
            <!-- CALCULATEUR DE PRIX -->
            <a href="<%= contextPath %>/calcul-prix" class="menu-card">
                <div class="menu-icon"><i class="fas fa-calculator"></i></div>
                <h3>Calculateur</h3>
                <p>Simuler le prix d'une course</p>
            </a>
            
            <!-- COMMANDER UN TAXI (Client uniquement) -->
            <% if(!isLoggedIn || "CLIENT".equals(userRole)) { %>
                <a href="<%= contextPath %>/client/request" class="menu-card">
                    <div class="menu-icon"><i class="fas fa-taxi"></i></div>
                    <h3>Commander un taxi</h3>
                    <p>Réservez votre course</p>
                </a>
            <% } %>
            
        </div>
    </div>
    
    <!-- Login Modal -->
    <div id="loginModal" class="modal">
        <div class="modal-content">
            <h2>Connexion</h2>
            <form action="<%= contextPath %>/login" method="post">
                <div class="form-group">
                    <input type="text" name="username" placeholder="Nom d'utilisateur" required>
                </div>
                <div class="form-group">
                    <input type="password" name="password" placeholder="Mot de passe" required>
                </div>
                <button type="submit" class="btn-login">Se connecter</button>
            </form>
            <hr style="margin: 1rem 0;">
            <p style="text-align: center;">
                <a href="${pageContext.request.contextPath}/register" style="color: #667eea; text-decoration: none;">Pas encore de compte ? S'inscrire</a>
            </p>
        </div>
    </div>
    
    <script>
        function showLoginModal() {
            document.getElementById('loginModal').style.display = 'flex';
        }
        window.onclick = function(event) {
            if (event.target == document.getElementById('loginModal')) {
                document.getElementById('loginModal').style.display = 'none';
            }
        }
    </script>
</body>
</html>