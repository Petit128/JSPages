package com.taxis.dao;

import com.taxis.model.Course;
import com.taxis.model.StatutCourse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    
    public void ajouter(Course course) throws SQLException {
        String query = "INSERT INTO courses (chauffeur_id, vehicule_id, client_nom, client_telephone, client_email, adresse_depart, adresse_arrivee, date_heure, distance, duree, temps_attente, nombre_bagages, animaux_present, reservation, prix, commission, revenu_chauffeur, statut, date_debut, date_fin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, course.getChauffeurId());
            ps.setInt(2, course.getVehiculeId());
            ps.setString(3, course.getClientNom());
            ps.setString(4, course.getClientTelephone());
            ps.setString(5, course.getClientEmail());
            ps.setString(6, course.getAdresseDepart());
            ps.setString(7, course.getAdresseArrivee());
            ps.setTimestamp(8, course.getDateHeure() != null ? new Timestamp(course.getDateHeure().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setDouble(9, course.getDistance());
            ps.setDouble(10, course.getDuree());
            ps.setDouble(11, course.getTempsAttente());
            ps.setInt(12, course.getNombreBagages());
            ps.setBoolean(13, course.isAnimauxPresent());
            ps.setBoolean(14, course.isReservation());
            ps.setDouble(15, course.getPrix());
            ps.setDouble(16, course.getCommission());
            ps.setDouble(17, course.getRevenuChauffeur());
            ps.setString(18, course.getStatut().name());
            ps.setTimestamp(19, course.getDateDebut() != null ? new Timestamp(course.getDateDebut().getTime()) : null);
            ps.setTimestamp(20, course.getDateFin() != null ? new Timestamp(course.getDateFin().getTime()) : null);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                course.setId(rs.getInt(1));
            }
        }
    }
    
    public void modifier(Course course) throws SQLException {
        String query = "UPDATE courses SET chauffeur_id=?, vehicule_id=?, client_nom=?, client_telephone=?, client_email=?, adresse_depart=?, adresse_arrivee=?, date_heure=?, distance=?, duree=?, temps_attente=?, nombre_bagages=?, animaux_present=?, reservation=?, prix=?, commission=?, revenu_chauffeur=?, statut=?, date_debut=?, date_fin=? WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, course.getChauffeurId());
            ps.setInt(2, course.getVehiculeId());
            ps.setString(3, course.getClientNom());
            ps.setString(4, course.getClientTelephone());
            ps.setString(5, course.getClientEmail());
            ps.setString(6, course.getAdresseDepart());
            ps.setString(7, course.getAdresseArrivee());
            ps.setTimestamp(8, course.getDateHeure() != null ? new Timestamp(course.getDateHeure().getTime()) : null);
            ps.setDouble(9, course.getDistance());
            ps.setDouble(10, course.getDuree());
            ps.setDouble(11, course.getTempsAttente());
            ps.setInt(12, course.getNombreBagages());
            ps.setBoolean(13, course.isAnimauxPresent());
            ps.setBoolean(14, course.isReservation());
            ps.setDouble(15, course.getPrix());
            ps.setDouble(16, course.getCommission());
            ps.setDouble(17, course.getRevenuChauffeur());
            ps.setString(18, course.getStatut().name());
            ps.setTimestamp(19, course.getDateDebut() != null ? new Timestamp(course.getDateDebut().getTime()) : null);
            ps.setTimestamp(20, course.getDateFin() != null ? new Timestamp(course.getDateFin().getTime()) : null);
            ps.setInt(21, course.getId());
            ps.executeUpdate();
        }
    }
    
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM courses WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public Course getById(int id) throws SQLException {
        String query = "SELECT * FROM courses WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
        }
        return null;
    }
    
    public List<Course> getAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses ORDER BY date_heure DESC";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }
    
  
    
    public List<Course> getByStatut(StatutCourse statut) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE statut=? ORDER BY date_heure";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, statut.name());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }
    
    public int getCoursesDuJourChauffeur(int chauffeurId) throws SQLException {
        String query = "SELECT COUNT(*) FROM courses WHERE chauffeur_id = ? AND DATE(date_heure) = CURDATE() AND statut IN ('TERMINEE', 'EN_COURS')";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, chauffeurId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setChauffeurId(rs.getInt("chauffeur_id"));
        course.setVehiculeId(rs.getInt("vehicule_id"));
        course.setClientNom(rs.getString("client_nom"));
        course.setClientTelephone(rs.getString("client_telephone"));
        
        // Gérer les colonnes qui peuvent ne pas exister
        try { course.setClientEmail(rs.getString("client_email")); } catch (SQLException e) { course.setClientEmail(null); }
        try { course.setTempsAttente(rs.getDouble("temps_attente")); } catch (SQLException e) { course.setTempsAttente(0); }
        try { course.setNombreBagages(rs.getInt("nombre_bagages")); } catch (SQLException e) { course.setNombreBagages(0); }
        try { course.setAnimauxPresent(rs.getBoolean("animaux_present")); } catch (SQLException e) { course.setAnimauxPresent(false); }
        try { course.setReservation(rs.getBoolean("reservation")); } catch (SQLException e) { course.setReservation(false); }
        try { course.setCommission(rs.getDouble("commission")); } catch (SQLException e) { course.setCommission(0); }
        try { course.setRevenuChauffeur(rs.getDouble("revenu_chauffeur")); } catch (SQLException e) { course.setRevenuChauffeur(0); }
        try { course.setDateDebut(rs.getTimestamp("date_debut")); } catch (SQLException e) { course.setDateDebut(null); }
        try { course.setDateFin(rs.getTimestamp("date_fin")); } catch (SQLException e) { course.setDateFin(null); }
        try { course.setLatitudeDepart(rs.getDouble("latitude_depart")); } catch (SQLException e) { course.setLatitudeDepart(0); }
        try { course.setLongitudeDepart(rs.getDouble("longitude_depart")); } catch (SQLException e) { course.setLongitudeDepart(0); }
        try { course.setLatitudeArrivee(rs.getDouble("latitude_arrivee")); } catch (SQLException e) { course.setLatitudeArrivee(0); }
        try { course.setLongitudeArrivee(rs.getDouble("longitude_arrivee")); } catch (SQLException e) { course.setLongitudeArrivee(0); }
        
        course.setAdresseDepart(rs.getString("adresse_depart"));
        course.setAdresseArrivee(rs.getString("adresse_arrivee"));
        course.setDateHeure(rs.getTimestamp("date_heure"));
        course.setDistance(rs.getDouble("distance"));
        course.setDuree(rs.getDouble("duree"));
        course.setPrix(rs.getDouble("prix"));
        
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            try {
                course.setStatut(StatutCourse.valueOf(statutStr));
            } catch (IllegalArgumentException e) {
                switch (statutStr) {
                    case "Planifiée": course.setStatut(StatutCourse.ASSIGNEE); break;
                    case "En cours": course.setStatut(StatutCourse.EN_COURS); break;
                    case "Terminée": course.setStatut(StatutCourse.TERMINEE); break;
                    case "En attente": course.setStatut(StatutCourse.EN_ATTENTE); break;
                    default: course.setStatut(StatutCourse.EN_ATTENTE);
                }
            }
        } else {
            course.setStatut(StatutCourse.EN_ATTENTE);
        }
        
        return course;
    }
 // Ajoutez cette méthode
    public List<Course> getByClientId(int clientId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE client_id = ? ORDER BY date_heure DESC";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }
    
 // Ajoutez cette méthode dans CourseDAO.java
    public List<Course> getByChauffeurId(int chauffeurId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE chauffeur_id = ? ORDER BY date_heure DESC";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, chauffeurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }
}