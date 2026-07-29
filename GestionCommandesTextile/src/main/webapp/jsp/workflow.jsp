<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.textile.model.EtapeProduction, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Workflow de production</title>
    <style>
        .container { max-width: 800px; margin: 0 auto; }
        .workflow-step { 
            display: flex; 
            align-items: center; 
            margin: 20px 0;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .step-number { 
            width: 40px; 
            height: 40px; 
            background-color: #007bff; 
            color: white; 
            border-radius: 50%; 
            display: flex; 
            align-items: center; 
            justify-content: center;
            margin-right: 20px;
            font-weight: bold;
        }
        .step-content { flex: 1; }
        .step-status { 
            padding: 5px 10px; 
            border-radius: 3px; 
            font-weight: bold;
        }
        .status-terminee { background-color: #d4edda; color: #155724; }
        .status-en-cours { background-color: #fff3cd; color: #856404; }
        .status-en-attente { background-color: #f8f9fa; color: #6c757d; }
        .btn { 
            padding: 8px 16px; 
            background-color: #007bff; 
            color: white; 
            text-decoration: none; 
            border-radius: 4px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Workflow de production</h1>
        <h2>Commande #${commandeId}</h2>
        
        <% List<EtapeProduction> etapes = (List<EtapeProduction>) request.getAttribute("etapes");
           if (etapes != null && !etapes.isEmpty()) {
               int stepNumber = 1;
               for (EtapeProduction etape : etapes) { 
                   String statusClass = "step-status ";
                   if ("TERMINEE".equals(etape.getStatut())) 
                       statusClass += "status-terminee";
                   else if ("EN_COURS".equals(etape.getStatut())) 
                       statusClass += "status-en-cours";
                   else 
                       statusClass += "status-en-attente";
        %>
                   <div class="workflow-step">
                       <div class="step-number"><%= stepNumber++ %></div>
                       <div class="step-content">
                           <h3><%= etape.getNomEtape() %></h3>
                           <p><strong>Responsable:</strong> 
                              <%= etape.getResponsable() != null ? etape.getResponsable() : "Non assigné" %>
                           </p>
                           <% if (etape.getDateDebut() != null) { %>
                               <p><strong>Début:</strong> <%= etape.getDateDebut() %></p>
                           <% } %>
                           <% if (etape.getDateFin() != null) { %>
                               <p><strong>Fin:</strong> <%= etape.getDateFin() %></p>
                           <% } %>
                       </div>
                       <div class="<%= statusClass %>">
                           <%= etape.getStatut() %>
                       </div>
                   </div>
        <%     }
           } else { %>
               <p>Aucune étape trouvée pour cette commande.</p>
        <% } %>
        
        <br>
        <a href="${pageContext.request.contextPath}/commande/details?id=${commandeId}" 
           class="btn">Retour aux détails</a>
        <a href="${pageContext.request.contextPath}/commande/" class="btn">Liste des commandes</a>
    </div>
</body>
</html>