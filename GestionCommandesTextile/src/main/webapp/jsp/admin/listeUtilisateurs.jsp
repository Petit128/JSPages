<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Utilisateurs - Administration</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
        }
        
        .navbar a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .btn {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        
        table {
            width: 100%;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background: #f7fafc;
        }
        
        .actif {
            color: green;
            font-weight: bold;
        }
        
        .inactif {
            color: red;
        }
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .btn-sm {
            padding: 5px 10px;
            font-size: 0.85em;
        }
        
        .btn-warning {
            background: #ed8936;
        }
        
        .btn-danger {
            background: #f56565;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h2>👥 Gestion des utilisateurs</h2>
        <div>
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
        </div>
    </div>
    
    <div class="container">
        <div class="header">
            <h2>Liste des utilisateurs</h2>
            <a href="${pageContext.request.contextPath}/admin/utilisateur/nouveau" class="btn">➕ Nouvel utilisateur</a>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Rôle</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${utilisateurs}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.nom}</td>
                        <td>${u.email}</td>
                        <td>${u.role}</td>
                        <td class="${u.actif ? 'actif' : 'inactif'}">${u.actif ? 'Actif' : 'Inactif'}</td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/admin/utilisateur/modifier?id=${u.id}" class="btn btn-sm btn-warning">Modifier</a>
                            <a href="${pageContext.request.contextPath}/admin/utilisateur/supprimer?id=${u.id}" class="btn btn-sm btn-danger" onclick="return confirm('Confirmer la suppression ?')">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>