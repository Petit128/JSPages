<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Résultat équation</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial; margin: 50px; }
        .solution { color: blue; font-weight: bold; }
    </style>
</head>
<body>
    <h2>Résultat de l'équation</h2>
    
    <%
        try {
            double a = Double.parseDouble(request.getParameter("a"));
            double b = Double.parseDouble(request.getParameter("b"));
            double c = Double.parseDouble(request.getParameter("c"));
    %>
    
    <p>Équation : <%= a %>x² + <%= b %>x + <%= c %> = 0</p>
    
    <%
            if (a == 0) {
                if (b == 0) {
                    if (c == 0) {
    %>
    <p class="solution">Tous les réels sont solutions</p>
    <%
                    } else {
    %>
    <p class="solution">Aucune solution</p>
    <%
                    }
                } else {
                    double x = -c / b;
    %>
    <p class="solution">Solution unique : x = <%= x %></p>
    <%
                }
            } else {
                double discriminant = b * b - 4 * a * c;
                
                if (discriminant > 0) {
                    double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);
                    double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);
    %>
    <p class="solution">Deux solutions réelles :</p>
    <p>x₁ = <%= x1 %></p>
    <p>x₂ = <%= x2 %></p>
    <%
                } else if (discriminant == 0) {
                    double x = -b / (2 * a);
    %>
    <p class="solution">Solution double : x = <%= x %></p>
    <%
                } else {
                    double partieReelle = -b / (2 * a);
                    double partieImaginaire = Math.sqrt(-discriminant) / (2 * a);
    %>
    <p class="solution">Deux solutions complexes :</p>
    <p>x₁ = <%= partieReelle %> + <%= partieImaginaire %>i</p>
    <p>x₂ = <%= partieReelle %> - <%= partieImaginaire %>i</p>
    <%
                }
            }
        } catch (NumberFormatException e) {
    %>
    <p style="color: red;">Erreur : Veuillez saisir des nombres valides</p>
    <%
        }
    %>
    
    <br>
    <a href="scdegree.html">Nouvelle équation</a>
    <br>
    <a href="index.html">Retour au menu</a>
</body>
</html>