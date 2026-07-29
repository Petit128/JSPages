package com.taxis.controller.api;

import com.taxis.dao.CourseDAO;
import com.taxis.metier.AttributionService;
import com.taxis.metier.TarificationService;
import com.taxis.model.Course;
import com.taxis.model.StatutCourse;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/client/*")
public class ClientAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourseDAO courseDAO;
    private AttributionService attributionService;
    private TarificationService tarificationService;
    private Gson gson;
    
    @Override
    public void init() {
        courseDAO = new CourseDAO();
        attributionService = new AttributionService();
        tarificationService = new TarificationService();
        gson = new Gson();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            if (pathInfo.equals("/request") || pathInfo.equals("/demande")) {
                // Lire les données JSON
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                
                Map<String, Object> data = gson.fromJson(sb.toString(), Map.class);
                
                Course course = new Course();
                course.setClientNom((String) data.get("nom"));
                course.setClientTelephone((String) data.get("telephone"));
                
                // Ajout de l'email
                if (data.containsKey("email")) {
                    course.setClientEmail((String) data.get("email"));
                }
                
                course.setAdresseDepart((String) data.get("depart"));
                course.setAdresseArrivee((String) data.get("arrivee"));
                
                if (data.containsKey("bagages")) {
                    course.setNombreBagages(Integer.parseInt(data.get("bagages").toString()));
                }
                if (data.containsKey("animaux")) {
                    course.setAnimauxPresent(Boolean.parseBoolean(data.get("animaux").toString()));
                }
                if (data.containsKey("distance")) {
                    course.setDistance(Double.parseDouble(data.get("distance").toString()));
                } else {
                    course.setDistance(5.0);
                }
                if (data.containsKey("reservation")) {
                    course.setReservation(Boolean.parseBoolean(data.get("reservation").toString()));
                }
                
                // Gestion de la date
                String dateHeureStr = (String) data.get("dateHeure");
                if (dateHeureStr != null && !dateHeureStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    course.setDateHeure(sdf.parse(dateHeureStr));
                } else {
                    course.setDateHeure(new Date());
                }
                
                course.setStatut(StatutCourse.EN_ATTENTE);
                
                // Calcul du prix estimé
                var details = tarificationService.calculerPrixDetaille(
                    course.getDistance(),
                    course.getTempsAttente(),
                    course.getNombreBagages(),
                    course.isAnimauxPresent(),
                    course.isReservation(),
                    course.getDateHeure()
                );
                course.setPrix(details.getTotalCourse());
                
                // Sauvegarder la course
                courseDAO.ajouter(course);
                
                // Tentative d'attribution automatique
                try {
                    course = attributionService.attribuerCourseAutomatique(course);
                    courseDAO.modifier(course);
                } catch (SQLException e) {
                    System.out.println("Course en attente d'assignation: " + e.getMessage());
                }
                
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("courseId", course.getId());
                result.put("message", "Course créée avec succès");
                out.print(gson.toJson(result));
                
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Endpoint non trouvé");
                out.print(gson.toJson(error));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            out.print(gson.toJson(error));
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            if (pathInfo != null && pathInfo.matches("/course/\\d+/status")) {
                String[] parts = pathInfo.split("/");
                int courseId = Integer.parseInt(parts[2]);
                Course course = courseDAO.getById(courseId);
                
                if (course != null) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("statut", course.getStatut().name());
                    
                    // Récupérer le chauffeur
                    String chauffeurName = "En recherche...";
                    String vehiculeInfo = "---";
                    if (course.getChauffeurId() > 0) {
                        com.taxis.dao.ChauffeurDAO chauffeurDAO = new com.taxis.dao.ChauffeurDAO();
                        com.taxis.model.Chauffeur chauffeur = chauffeurDAO.getById(course.getChauffeurId());
                        if (chauffeur != null) {
                            chauffeurName = chauffeur.getPrenom() + " " + chauffeur.getNom();
                        }
                        
                        // Récupérer le véhicule
                        if (course.getVehiculeId() > 0) {
                            com.taxis.dao.VehiculeDAO vehiculeDAO = new com.taxis.dao.VehiculeDAO();
                            com.taxis.model.Vehicule vehicule = vehiculeDAO.getById(course.getVehiculeId());
                            if (vehicule != null) {
                                vehiculeInfo = vehicule.getMarque() + " " + vehicule.getModele() + " (" + vehicule.getImmatriculation() + ")";
                            }
                        }
                    }
                    
                    result.put("chauffeur", chauffeurName);
                    result.put("vehicule", vehiculeInfo);
                    result.put("distance", course.getDistance());
                    result.put("prix", course.getPrix());
                    result.put("duree", course.getDuree());
                    result.put("tempsEstime", 5);
                    result.put("tempsArrivee", 3);
                    out.print(gson.toJson(result));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Course non trouvée");
                    out.print(gson.toJson(error));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Endpoint non trouvé");
                out.print(gson.toJson(error));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            out.print(gson.toJson(error));
        }
    }
}