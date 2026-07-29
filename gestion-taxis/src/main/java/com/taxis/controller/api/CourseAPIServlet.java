package com.taxis.controller.api;

import com.taxis.dao.ChauffeurDAO;
import com.taxis.dao.CourseDAO;
import com.taxis.dao.VehiculeDAO;
import com.taxis.metier.AttributionService;
import com.taxis.metier.TarificationService;
import com.taxis.model.Chauffeur;
import com.taxis.model.Course;
import com.taxis.model.StatutChauffeur;
import com.taxis.model.StatutCourse;
import com.taxis.model.Vehicule;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/courses/*")
public class CourseAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourseDAO courseDAO;
    private ChauffeurDAO chauffeurDAO;
    private VehiculeDAO vehiculeDAO;
    private AttributionService attributionService;
    private TarificationService tarificationService;
    private Gson gson;
    
    @Override
    public void init() {
        courseDAO = new CourseDAO();
        chauffeurDAO = new ChauffeurDAO();
        vehiculeDAO = new VehiculeDAO();
        attributionService = new AttributionService();
        tarificationService = new TarificationService();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String chauffeurIdParam = request.getParameter("chauffeurId");
            
            if (chauffeurIdParam != null) {
                List<Course> courses = courseDAO.getByChauffeurId(Integer.parseInt(chauffeurIdParam));
                out.print(gson.toJson(courses));
            } else if (pathInfo == null || pathInfo.equals("/")) {
                List<Course> courses = courseDAO.getAll();
                out.print(gson.toJson(courses));
            } else if (pathInfo.equals("/en-attente")) {
                List<Course> courses = courseDAO.getByStatut(StatutCourse.EN_ATTENTE);
                out.print(gson.toJson(courses));
            } else if (pathInfo.matches("/\\d+")) {
                int id = Integer.parseInt(pathInfo.substring(1));
                Course course = courseDAO.getById(id);
                out.print(gson.toJson(course));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(errorResponse("Endpoint non trouvé")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(errorResponse(e.getMessage())));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            Map<String, Object> data = body.isEmpty() ? new HashMap<>() : gson.fromJson(body, Map.class);
            
            // ========== ACCEPTER UNE COURSE (Chauffeur) ==========
            if (pathInfo.equals("/accepter")) {
                int courseId = ((Number) data.get("courseId")).intValue();
                int chauffeurId = ((Number) data.get("chauffeurId")).intValue();
                
                Course course = courseDAO.getById(courseId);
                Chauffeur chauffeur = chauffeurDAO.getById(chauffeurId);
                
                if (course != null && chauffeur != null) {
                    course.setChauffeurId(chauffeurId);
                    course.setStatut(StatutCourse.EN_COURS);
                    chauffeur.setStatut(StatutChauffeur.EN_COURSE);
                    
                    courseDAO.modifier(course);
                    chauffeurDAO.modifier(chauffeur);
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    out.print(gson.toJson(result));
                }
            }
            // ========== REFUSER UNE COURSE ==========
            else if (pathInfo.equals("/refuser")) {
                int courseId = ((Number) data.get("courseId")).intValue();
                Course nouvelleCourse = attributionService.reassignerCourse(courseId);
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("newCourseId", nouvelleCourse.getId());
                out.print(gson.toJson(result));
            }
            // ========== DÉMARRER UNE COURSE ==========
            else if (pathInfo.equals("/demarrer")) {
                int courseId = ((Number) data.get("courseId")).intValue();
                Course course = courseDAO.getById(courseId);
                if (course != null) {
                    course.setStatut(StatutCourse.EN_COURS);
                    course.setDateDebut(new Date());
                    courseDAO.modifier(course);
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    out.print(gson.toJson(result));
                }
            }
            // ========== TERMINER UNE COURSE ==========
            else if (pathInfo.equals("/terminer")) {
                int courseId = ((Number) data.get("courseId")).intValue();
                double distance = data.containsKey("distance") ? ((Number) data.get("distance")).doubleValue() : 0;
                
                Course course = courseDAO.getById(courseId);
                if (course != null) {
                    course.setStatut(StatutCourse.TERMINEE);
                    course.setDistance(distance);
                    course.setDateFin(new Date());
                    
                    var details = tarificationService.calculerPrixDetaille(
                        distance, course.getDuree(), course.getNombreBagages(),
                        course.isAnimauxPresent(), course.isReservation(), course.getDateHeure()
                    );
                    course.setPrix(details.getTotalCourse());
                    course.setRevenuChauffeur(details.getRevenuChauffeur());
                    courseDAO.modifier(course);
                    
                    if (course.getChauffeurId() > 0) {
                        Chauffeur chauffeur = chauffeurDAO.getById(course.getChauffeurId());
                        if (chauffeur != null) {
                            chauffeur.setStatut(StatutChauffeur.DISPONIBLE);
                            chauffeur.setRevenuTotal(chauffeur.getRevenuTotal() + details.getRevenuChauffeur());
                            chauffeur.setNombreCourses(chauffeur.getNombreCourses() + 1);
                            chauffeurDAO.modifier(chauffeur);
                        }
                    }
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("prix", details.getTotalCourse());
                    out.print(gson.toJson(result));
                }
            }
            // ========== ASSIGNER UNE COURSE (Admin/Opérateur) ==========
            else if (pathInfo.equals("/assigner")) {
                int courseId = ((Number) data.get("courseId")).intValue();
                int chauffeurId = ((Number) data.get("chauffeurId")).intValue();
                int vehiculeId = ((Number) data.get("vehiculeId")).intValue();
                
                Course course = courseDAO.getById(courseId);
                Chauffeur chauffeur = chauffeurDAO.getById(chauffeurId);
                Vehicule vehicule = vehiculeDAO.getById(vehiculeId);
                
                if (course != null && chauffeur != null && vehicule != null) {
                    course.setChauffeurId(chauffeurId);
                    course.setVehiculeId(vehiculeId);
                    course.setStatut(StatutCourse.ASSIGNEE);
                    chauffeur.setStatut(StatutChauffeur.EN_COURSE);
                    vehicule.setDisponible(false);
                    
                    courseDAO.modifier(course);
                    chauffeurDAO.modifier(chauffeur);
                    vehiculeDAO.modifier(vehicule);
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("message", "Course assignée avec succès");
                    out.print(gson.toJson(result));
                } else {
                    out.print(gson.toJson(errorResponse("Données invalides")));
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(errorResponse("Endpoint non trouvé: " + pathInfo)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(errorResponse(e.getMessage())));
        }
    }
    
    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}