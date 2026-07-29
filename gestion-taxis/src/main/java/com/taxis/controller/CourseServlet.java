package com.taxis.controller;

import com.google.gson.Gson;
import com.taxis.dao.ChauffeurDAO;
import com.taxis.dao.CourseDAO;
import com.taxis.dao.VehiculeDAO;
import com.taxis.metier.CourseMetier;
import com.taxis.model.Chauffeur;
import com.taxis.model.Course;
import com.taxis.model.StatutChauffeur;
import com.taxis.model.StatutCourse;
import com.taxis.model.Vehicule;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/courses")
public class CourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CourseDAO courseDAO;
    private ChauffeurDAO chauffeurDAO;
    private VehiculeDAO vehiculeDAO;
    private CourseMetier courseMetier;
    
    @Override
    public void init() {
        courseDAO = new CourseDAO();
        chauffeurDAO = new ChauffeurDAO();
        vehiculeDAO = new VehiculeDAO();
        courseMetier = new CourseMetier();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        // Support JSON pour l'API
        if ("list".equals(action) && "json".equals(request.getParameter("format"))) {
            try {
				listCoursesJson(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return;
        }
        
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCourse(request, response);
                    break;
                case "terminer":
                    showTerminerForm(request, response);
                    break;
                case "assign":
                    showAssignForm(request, response);
                    break;
                case "demarrer":
                    demarrerCourse(request, response);
                    break;
                default:
                    listCourses(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("update".equals(action)) {
                updateCourse(request, response);
            } else if ("attribuer".equals(action)) {
                attribuerCourse(request, response);
            } else if ("terminer".equals(action)) {
                terminerCourse(request, response);
            } else if ("assignerManuelle".equals(action)) {
                assignerCourseManuelle(request, response);
            } else {
                response.sendRedirect("courses");
            }
        } catch (SQLException | ParseException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listCourses(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Course> courses = courseDAO.getAll();
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/views/courses.jsp").forward(request, response);
    }
    
    private void listCoursesJson(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        List<Course> courses = courseDAO.getAll();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(courses));
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/course-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            Course course = courseDAO.getById(id);
            request.setAttribute("course", course);
            request.getRequestDispatcher("/views/course-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("courses");
        }
    }
    
    private void showTerminerForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            Course course = courseDAO.getById(id);
            request.setAttribute("course", course);
            request.getRequestDispatcher("/views/terminer-course.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("courses");
        }
    }
    
    private void showAssignForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            Course course = courseDAO.getById(id);
            List<Chauffeur> chauffeursDisponibles = chauffeurDAO.getDisponibles();
            List<Vehicule> vehiculesDisponibles = vehiculeDAO.getDisponibles();
            
            request.setAttribute("course", course);
            request.setAttribute("chauffeurs", chauffeursDisponibles);
            request.setAttribute("vehicules", vehiculesDisponibles);
            request.getRequestDispatcher("/views/assigner-course.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("courses");
        }
    }
    
    private void attribuerCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        Course course = new Course();
        
        course.setClientNom(request.getParameter("clientNom"));
        course.setClientTelephone(request.getParameter("clientTelephone"));
        course.setAdresseDepart(request.getParameter("adresseDepart"));
        course.setAdresseArrivee(request.getParameter("adresseArrivee"));
        
        String dateStr = request.getParameter("dateHeure");
        if (dateStr != null && !dateStr.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            course.setDateHeure(sdf.parse(dateStr));
        } else {
            course.setDateHeure(new java.util.Date());
        }
        
        double distance = Double.parseDouble(request.getParameter("distance"));
        course.setDistance(distance);
        course.setStatut(StatutCourse.EN_ATTENTE);
        
        courseDAO.ajouter(course);
        
        String assignAuto = request.getParameter("assignAuto");
        if ("true".equals(assignAuto)) {
            try {
                course = courseMetier.assignerAutomatique(course);
                courseDAO.modifier(course);
            } catch (SQLException e) {
                System.out.println("Aucun chauffeur disponible pour assignation auto");
            }
        }
        
        response.sendRedirect("courses");
    }
    
    private void assignerCourseManuelle(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String courseIdParam = request.getParameter("courseId");
        String chauffeurIdParam = request.getParameter("chauffeurId");
        String vehiculeIdParam = request.getParameter("vehiculeId");
        
        if (courseIdParam == null || chauffeurIdParam == null || vehiculeIdParam == null) {
            response.sendRedirect("courses");
            return;
        }
        
        try {
            int courseId = Integer.parseInt(courseIdParam);
            int chauffeurId = Integer.parseInt(chauffeurIdParam);
            int vehiculeId = Integer.parseInt(vehiculeIdParam);
            
            Course course = courseDAO.getById(courseId);
            Chauffeur chauffeur = chauffeurDAO.getById(chauffeurId);
            Vehicule vehicule = vehiculeDAO.getById(vehiculeId);
            
            if (course != null && chauffeur != null && vehicule != null) {
                course.setChauffeurId(chauffeurId);
                course.setVehiculeId(vehiculeId);
                course.setStatut(StatutCourse.ASSIGNEE);
                chauffeur.setStatut(StatutChauffeur.EN_COURSE);
                vehicule.setDisponible(false);
                
                chauffeurDAO.modifier(chauffeur);
                vehiculeDAO.modifier(vehicule);
                courseDAO.modifier(course);
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur de parsing: " + e.getMessage());
        }
        
        response.sendRedirect("courses");
    }
    
    private void demarrerCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Course course = courseDAO.getById(id);
            
            if (course != null) {
                course.setStatut(StatutCourse.EN_COURS);
                course.setDateDebut(new java.util.Date());
                courseDAO.modifier(course);
            }
        } catch (NumberFormatException e) {
            System.err.println("ID invalide pour demarrerCourse: " + idParam);
        }
        
        response.sendRedirect("courses");
    }
    
    private void terminerCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idParam = request.getParameter("id");
        String distanceParam = request.getParameter("distanceReelle");
        String dureeParam = request.getParameter("dureeReelle");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            double distanceReelle = 0;
            double dureeReelle = 0;
            
            if (distanceParam != null && !distanceParam.isEmpty()) {
                distanceReelle = Double.parseDouble(distanceParam);
            }
            if (dureeParam != null && !dureeParam.isEmpty()) {
                dureeReelle = Double.parseDouble(dureeParam);
            }
            
            courseMetier.terminerCourse(id, distanceReelle, dureeReelle, 0, false);
        } catch (NumberFormatException e) {
            System.err.println("Paramètres invalides pour terminerCourse: " + e.getMessage());
        }
        
        response.sendRedirect("courses");
    }
    
    private void updateCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Course course = courseDAO.getById(id);
            if (course != null) {
                course.setClientNom(request.getParameter("clientNom"));
                course.setClientTelephone(request.getParameter("clientTelephone"));
                course.setAdresseDepart(request.getParameter("adresseDepart"));
                course.setAdresseArrivee(request.getParameter("adresseArrivee"));
                
                String dateStr = request.getParameter("dateHeure");
                if (dateStr != null && !dateStr.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    course.setDateHeure(sdf.parse(dateStr));
                }
                
                course.setDistance(Double.parseDouble(request.getParameter("distance")));
                courseDAO.modifier(course);
            }
        } catch (NumberFormatException e) {
            System.err.println("ID invalide pour updateCourse: " + idParam);
        }
        
        response.sendRedirect("courses");
    }
    
    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("courses");
            return;
        }
        
        try {
            int id = Integer.parseInt(idParam);
            Course course = courseDAO.getById(id);
            if (course != null && course.getChauffeurId() > 0) {
                Chauffeur chauffeur = chauffeurDAO.getById(course.getChauffeurId());
                if (chauffeur != null) {
                    chauffeur.setStatut(StatutChauffeur.DISPONIBLE);
                    chauffeurDAO.modifier(chauffeur);
                }
            }
            if (course != null && course.getVehiculeId() > 0) {
                Vehicule vehicule = vehiculeDAO.getById(course.getVehiculeId());
                if (vehicule != null) {
                    vehicule.setDisponible(true);
                    vehiculeDAO.modifier(vehicule);
                }
            }
            
            courseDAO.supprimer(id);
        } catch (NumberFormatException e) {
            System.err.println("ID invalide pour deleteCourse: " + idParam);
        }
        
        response.sendRedirect("courses");
    }
}