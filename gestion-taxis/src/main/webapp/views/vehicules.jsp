<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaxiFlow - Gestion des Véhicules</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: #f5f7fa;
            color: #333;
        }

        .header {
            background: white;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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

        .nav-links {
            display: flex;
            gap: 2rem;
            list-style: none;
        }

        .nav-links a {
            text-decoration: none;
            color: #666;
            font-weight: 500;
            transition: color 0.3s;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .nav-links a:hover, .nav-links a.active {
            color: #667eea;
        }

        .container {
            max-width: 1400px;
            margin: 2rem auto;
            padding: 0 2rem;
        }

        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .page-title {
            font-size: 2rem;
            font-weight: 700;
            color: #333;
        }

        .btn-add {
            background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 10px;
            cursor: pointer;
            font-size: 1rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 8px;
            text-decoration: none;
            transition: transform 0.3s;
        }

        .btn-add:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 152, 0, 0.4);
        }

        /* Vehicle Cards Grid */
        .vehicles-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 1.5rem;
        }

        .vehicle-card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .vehicle-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }

        .vehicle-header {
            background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
            color: white;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .vehicle-model {
            font-size: 1.2rem;
            font-weight: 700;
        }

        .vehicle-status {
            padding: 4px 8px;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 600;
            background: rgba(255, 255, 255, 0.2);
        }

        .vehicle-body {
            padding: 1rem;
        }

        .vehicle-info {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .info-item {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 0.9rem;
        }

        .info-item i {
            width: 20px;
            color: #ff9800;
        }

        .kilometer-bar {
            background: #f0f0f0;
            height: 6px;
            border-radius: 3px;
            margin-top: 5px;
            overflow: hidden;
        }

        .kilometer-fill {
            background: linear-gradient(90deg, #ff9800, #f57c00);
            height: 100%;
            border-radius: 3px;
            transition: width 0.3s;
        }

        .vehicle-actions {
            display: flex;
            gap: 0.5rem;
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #f0f0f0;
        }

        .btn-card {
            flex: 1;
            padding: 8px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 0.85rem;
            font-weight: 500;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            text-decoration: none;
            transition: all 0.3s;
        }

        .btn-edit-card {
            background: #2196f3;
            color: white;
        }

        .btn-delete-card {
            background: #f44336;
            color: white;
        }

        .btn-card:hover {
            transform: translateY(-2px);
            filter: brightness(0.9);
        }

        @media (max-width: 768px) {
            .nav-container {
                flex-direction: column;
                gap: 1rem;
            }

            .nav-links {
                flex-wrap: wrap;
                justify-content: center;
            }

            .vehicles-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <%
        String contextPath = request.getContextPath();
    %>

    <header class="header">
        <div class="nav-container">
            <a href="<%= contextPath %>/" class="logo">
                <i class="fas fa-taxi"></i>
                <span>TaxiFlow</span>
            </a>
            <ul class="nav-links">
                <li><a href="<%= contextPath %>/"><i class="fas fa-home"></i> Accueil</a></li>
                <li><a href="<%= contextPath %>/chauffeurs"><i class="fas fa-users"></i> Chauffeurs</a></li>
                <li><a href="<%= contextPath %>/vehicules" class="active"><i class="fas fa-car"></i> Véhicules</a></li>
                <li><a href="<%= contextPath %>/courses"><i class="fas fa-map-marker-alt"></i> Courses</a></li>
                <li><a href="<%= contextPath %>/statistiques"><i class="fas fa-chart-line"></i> Statistiques</a></li>
            </ul>
        </div>
    </header>

    <div class="container">
        <div class="header-actions">
            <h1 class="page-title">
                <i class="fas fa-car" style="color: #ff9800;"></i> 
                Gestion des Véhicules
            </h1>
            <a href="<%= contextPath %>/vehicules?action=new" class="btn-add">
                <i class="fas fa-plus"></i>
                Ajouter un véhicule
            </a>
        </div>

        <div class="vehicles-grid">
            <c:forEach var="vehicule" items="${vehicules}">
                <div class="vehicle-card">
                    <div class="vehicle-header">
                        <span class="vehicle-model">
                            <i class="fas fa-car"></i> ${vehicule.marque} ${vehicule.modele}
                        </span>
                        <span class="vehicle-status">
                            ${vehicule.disponible ? 'Disponible' : 'Indisponible'}
                        </span>
                    </div>
                    <div class="vehicle-body">
                        <div class="vehicle-info">
                            <div class="info-item">
                                <i class="fas fa-id-card"></i>
                                <span>${vehicule.immatriculation}</span>
                            </div>
                            <div class="info-item">
                                <i class="fas fa-palette"></i>
                                <span>${vehicule.couleur}</span>
                            </div>
                            <div class="info-item">
                                <i class="fas fa-calendar"></i>
                                <span>${vehicule.annee}</span>
                            </div>
                            <div class="info-item">
                                <i class="fas fa-users"></i>
                                <span>${vehicule.nombrePlaces} places</span>
                            </div>
                        </div>
                        <div class="info-item">
                            <i class="fas fa-tachometer-alt"></i>
                            <span>${vehicule.kilometrage} km</span>
                        </div>
                        <div class="kilometer-bar">
                            <div class="kilometer-fill" style="width: ${vehicule.kilometrage / 1000}%"></div>
                        </div>
                        <div class="info-item" style="margin-top: 0.5rem;">
                            <i class="fas fa-wrench"></i>
                            <span>État: ${vehicule.etat}</span>
                        </div>
                        <div class="vehicle-actions">
                            <a href="vehicules?action=edit&id=${vehicule.id}" class="btn-card btn-edit-card">
                                <i class="fas fa-edit"></i> Modifier
                            </a>
                            <a href="vehicules?action=delete&id=${vehicule.id}" 
                               class="btn-card btn-delete-card"
                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce véhicule ?')">
                                <i class="fas fa-trash"></i> Supprimer
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>