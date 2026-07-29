package com.taxis.controller.api;

import com.taxis.dao.ChauffeurDAO;
import com.taxis.dao.CourseDAO;
import com.taxis.model.Chauffeur;
import com.taxis.model.Course;
import com.taxis.model.StatutChauffeur;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/chauffeur/*")
public class ChauffeurAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ChauffeurDAO chauffeurDAO;
    private CourseDAO courseDAO;
    private Gson gson;
    
    @Override
    public void init() {
        chauffeurDAO = new ChauffeurDAO();
        courseDAO = new CourseDAO();
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
            if (pathInfo == null || pathInfo.equals("/")) {
                // Liste tous les chauffeurs
                List<Chauffeur> chauffeurs = chauffeurDAO.getAll();
                out.print(gson.toJson(chauffeurs));
            } else if (pathInfo.equals("/disponibles")) {
                // Chauffeurs disponibles
                List<Chauffeur> chauffeurs = chauffeurDAO.getDisponibles();
                out.print(gson.toJson(chauffeurs));
            } else if (pathInfo.matches("/\\d+")) {
                // Chauffeur par ID
                int id = Integer.parseInt(pathInfo.substring(1));
                Chauffeur chauffeur = chauffeurDAO.getById(id);
                out.print(gson.toJson(chauffeur));
            } else if (pathInfo.equals("/stats")) {
                // Statistiques du chauffeur connecté
                Map<String, Object> stats = getChauffeurStats(request);
                out.print(gson.toJson(stats));
            } else if (pathInfo.equals("/bilan")) {
                // Bilan quotidien
                String idParam = request.getParameter("id");
                if (idParam != null) {
                    int id = Integer.parseInt(idParam);
                    Map<String, Object> bilan = getBilanChauffeur(id);
                    out.print(gson.toJson(bilan));
                } else {
                    out.print(gson.toJson(errorResponse("ID manquant")));
                }
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
            if (pathInfo.equals("/statut")) {
                // Changer le statut du chauffeur
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                
                Map<String, Object> data = gson.fromJson(sb.toString(), Map.class);
                String nouveauStatut = (String) data.get("statut");
                Integer chauffeurId = null;
                
                // Récupérer l'ID soit de la session soit du JSON
                if (data.containsKey("id")) {
                    chauffeurId = ((Number) data.get("id")).intValue();
                } else {
                    chauffeurId = (Integer) request.getSession().getAttribute("chauffeurId");
                }
                
                if (chauffeurId == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print(gson.toJson(errorResponse("Non authentifié")));
                    return;
                }
                
                Chauffeur chauffeur = chauffeurDAO.getById(chauffeurId);
                if (chauffeur != null) {
                    switch (nouveauStatut) {
                        case "DISPONIBLE":
                            chauffeur.setStatut(StatutChauffeur.DISPONIBLE);
                            break;
                        case "EN_PAUSE":
                            chauffeur.setStatut(StatutChauffeur.EN_PAUSE);
                            break;
                        case "INDISPONIBLE":
                            chauffeur.setStatut(StatutChauffeur.INDISPONIBLE);
                            break;
                        default:
                            chauffeur.setStatut(StatutChauffeur.valueOf(nouveauStatut));
                    }
                    chauffeurDAO.modifier(chauffeur);
                    
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("statut", chauffeur.getStatut().name());
                    result.put("libelle", chauffeur.getStatut().getLibelle());
                    out.print(gson.toJson(result));
                } else {
                    out.print(gson.toJson(errorResponse("Chauffeur non trouvé")));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(errorResponse("Endpoint non trouvé")));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(errorResponse(e.getMessage())));
        }
    }
    
    private Map<String, Object> getChauffeurStats(HttpServletRequest request) throws SQLException {
        Integer chauffeurId = (Integer) request.getSession().getAttribute("chauffeurId");
        Map<String, Object> stats = new HashMap<>();
        
        if (chauffeurId != null) {
            Chauffeur chauffeur = chauffeurDAO.getById(chauffeurId);
            if (chauffeur != null) {
                stats.put("nombreCourses", chauffeur.getNombreCourses());
                stats.put("revenuTotal", chauffeur.getRevenuTotal());
                stats.put("evaluation", chauffeur.getEvaluation());
                stats.put("statut", chauffeur.getStatut().name());
            }
        }
        return stats;
    }
    
    private Map<String, Object> getBilanChauffeur(int chauffeurId) throws SQLException {
        Map<String, Object> bilan = new HashMap<>();
        List<Course> courses = courseDAO.getByChauffeurId(chauffeurId);
        
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        
        int nbCourses = 0;
        double distanceTotale = 0;
        double revenus = 0;
        double temps = 0;
        
        for (Course course : courses) {
            Calendar courseDate = Calendar.getInstance();
            courseDate.setTime(course.getDateHeure());
            
            if (courseDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                courseDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                if (course.getStatut() == StatutCourse.TERMINEE) {
                    nbCourses++;
                    distanceTotale += course.getDistance();
                    revenus += course.getRevenuChauffeur();
                    temps += course.getDuree();
                }
            }
        }
        
        bilan.put("nombreCourses", nbCourses);
        bilan.put("distanceTotale", distanceTotale);
        bilan.put("revenus", revenus);
        bilan.put("tempsTravaille", Math.round(temps / 60));
        return bilan;
    }
    
    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}