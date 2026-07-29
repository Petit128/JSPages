package com.taxis.dao;

import com.taxis.model.Chauffeur;
import com.taxis.model.StatutChauffeur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurDAO {
    
    public void ajouter(Chauffeur chauffeur) throws SQLException {
        String query = "INSERT INTO chauffeurs (nom, prenom, permis, telephone, email, statut, date_embauche, revenu_total, nombre_courses, evaluation, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, chauffeur.getNom());
            ps.setString(2, chauffeur.getPrenom());
            ps.setString(3, chauffeur.getPermis());
            ps.setString(4, chauffeur.getTelephone());
            ps.setString(5, chauffeur.getEmail());
            ps.setString(6, chauffeur.getStatut().name());
            ps.setDate(7, new java.sql.Date(chauffeur.getDateEmbauche().getTime()));
            ps.setDouble(8, chauffeur.getRevenuTotal());
            ps.setInt(9, chauffeur.getNombreCourses());
            ps.setDouble(10, chauffeur.getEvaluation());
            ps.setDouble(11, chauffeur.getLatitude());
            ps.setDouble(12, chauffeur.getLongitude());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                chauffeur.setId(rs.getInt(1));
            }
        }
    }
    
    public void modifier(Chauffeur chauffeur) throws SQLException {
        String query = "UPDATE chauffeurs SET nom=?, prenom=?, permis=?, telephone=?, email=?, statut=?, revenu_total=?, nombre_courses=?, evaluation=?, latitude=?, longitude=? WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, chauffeur.getNom());
            ps.setString(2, chauffeur.getPrenom());
            ps.setString(3, chauffeur.getPermis());
            ps.setString(4, chauffeur.getTelephone());
            ps.setString(5, chauffeur.getEmail());
            ps.setString(6, chauffeur.getStatut().name());
            ps.setDouble(7, chauffeur.getRevenuTotal());
            ps.setInt(8, chauffeur.getNombreCourses());
            ps.setDouble(9, chauffeur.getEvaluation());
            ps.setDouble(10, chauffeur.getLatitude());
            ps.setDouble(11, chauffeur.getLongitude());
            ps.setInt(12, chauffeur.getId());
            ps.executeUpdate();
        }
    }
    
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM chauffeurs WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public Chauffeur getById(int id) throws SQLException {
        String query = "SELECT * FROM chauffeurs WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToChauffeur(rs);
            }
        }
        return null;
    }
    
    public List<Chauffeur> getAll() throws SQLException {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String query = "SELECT * FROM chauffeurs ORDER BY nom, prenom";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                chauffeurs.add(mapResultSetToChauffeur(rs));
            }
        }
        return chauffeurs;
    }
    
    public List<Chauffeur> getDisponibles() throws SQLException {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String query = "SELECT * FROM chauffeurs WHERE statut = 'DISPONIBLE' ORDER BY nom, prenom";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                chauffeurs.add(mapResultSetToChauffeur(rs));
            }
        }
        return chauffeurs;
    }
    
    public List<Chauffeur> getByStatut(StatutChauffeur statut) throws SQLException {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String query = "SELECT * FROM chauffeurs WHERE statut = ? ORDER BY nom, prenom";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, statut.name());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                chauffeurs.add(mapResultSetToChauffeur(rs));
            }
        }
        return chauffeurs;
    }
    
    private Chauffeur mapResultSetToChauffeur(ResultSet rs) throws SQLException {
        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setId(rs.getInt("id"));
        chauffeur.setNom(rs.getString("nom"));
        chauffeur.setPrenom(rs.getString("prenom"));
        chauffeur.setPermis(rs.getString("permis"));
        chauffeur.setTelephone(rs.getString("telephone"));
        chauffeur.setEmail(rs.getString("email"));
        
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            chauffeur.setStatut(StatutChauffeur.valueOf(statutStr));
        } else {
            // Compatibilité avec ancienne base
            boolean disponible = rs.getBoolean("disponible");
            chauffeur.setStatut(disponible ? StatutChauffeur.DISPONIBLE : StatutChauffeur.INDISPONIBLE);
        }
        
        chauffeur.setDateEmbauche(rs.getDate("date_embauche"));
        chauffeur.setRevenuTotal(rs.getDouble("revenu_total"));
        chauffeur.setNombreCourses(rs.getInt("nombre_courses"));
        chauffeur.setEvaluation(rs.getDouble("evaluation"));
        
        try {
            chauffeur.setLatitude(rs.getDouble("latitude"));
            chauffeur.setLongitude(rs.getDouble("longitude"));
        } catch (SQLException e) {
            chauffeur.setLatitude(48.8566);
            chauffeur.setLongitude(2.3522);
        }
        
        return chauffeur;
    }
    
 // Ajoutez cette méthode dans ChauffeurDAO.java

    public Chauffeur getByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM chauffeurs WHERE user_id = ?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToChauffeur(rs);
            }
        }
        return null;
    }
}