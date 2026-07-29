// dao/VehiculeDAO.java
package com.taxis.dao;

import com.taxis.model.Vehicule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {
    
    public void ajouter(Vehicule vehicule) throws SQLException {
        String query = "INSERT INTO vehicules (immatriculation, marque, modele, annee, couleur, nombre_places, disponible, kilometrage, etat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, vehicule.getImmatriculation());
            ps.setString(2, vehicule.getMarque());
            ps.setString(3, vehicule.getModele());
            ps.setInt(4, vehicule.getAnnee());
            ps.setString(5, vehicule.getCouleur());
            ps.setInt(6, vehicule.getNombrePlaces());
            ps.setBoolean(7, vehicule.isDisponible());
            ps.setDouble(8, vehicule.getKilometrage());
            ps.setString(9, vehicule.getEtat());
            ps.executeUpdate();
        }
    }
    
    public void modifier(Vehicule vehicule) throws SQLException {
        String query = "UPDATE vehicules SET immatriculation=?, marque=?, modele=?, annee=?, couleur=?, nombre_places=?, disponible=?, kilometrage=?, etat=? WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, vehicule.getImmatriculation());
            ps.setString(2, vehicule.getMarque());
            ps.setString(3, vehicule.getModele());
            ps.setInt(4, vehicule.getAnnee());
            ps.setString(5, vehicule.getCouleur());
            ps.setInt(6, vehicule.getNombrePlaces());
            ps.setBoolean(7, vehicule.isDisponible());
            ps.setDouble(8, vehicule.getKilometrage());
            ps.setString(9, vehicule.getEtat());
            ps.setInt(10, vehicule.getId());
            ps.executeUpdate();
        }
    }
    
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM vehicules WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public Vehicule getById(int id) throws SQLException {
        String query = "SELECT * FROM vehicules WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVehicule(rs);
            }
        }
        return null;
    }
    
    public List<Vehicule> getAll() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicules ORDER BY marque, modele";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        }
        return vehicules;
    }
    
    public List<Vehicule> getDisponibles() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicules WHERE disponible=true AND etat='Bon' ORDER BY marque, modele";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        }
        return vehicules;
    }
    
    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();
        vehicule.setId(rs.getInt("id"));
        vehicule.setImmatriculation(rs.getString("immatriculation"));
        vehicule.setMarque(rs.getString("marque"));
        vehicule.setModele(rs.getString("modele"));
        vehicule.setAnnee(rs.getInt("annee"));
        vehicule.setCouleur(rs.getString("couleur"));
        vehicule.setNombrePlaces(rs.getInt("nombre_places"));
        vehicule.setDisponible(rs.getBoolean("disponible"));
        vehicule.setKilometrage(rs.getDouble("kilometrage"));
        vehicule.setEtat(rs.getString("etat"));
        return vehicule;
    }
}