package com.taxis.dao;

import com.taxis.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    public void ajouter(Client client) throws SQLException {
        String query = "INSERT INTO clients (nom, prenom, email, telephone, adresse, password, date_inscription, actif, total_depenses, nombre_courses) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelephone());
            ps.setString(5, client.getAdresse());
            ps.setString(6, client.getPassword());
            ps.setTimestamp(7, new Timestamp(client.getDateInscription().getTime()));
            ps.setBoolean(8, client.isActif());
            ps.setDouble(9, client.getTotalDepenses());
            ps.setInt(10, client.getNombreCourses());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }
        }
    }
    
    public Client getById(int id) throws SQLException {
        String query = "SELECT * FROM clients WHERE id = ?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        }
        return null;
    }
    
    public Client getByEmail(String email) throws SQLException {
        String query = "SELECT * FROM clients WHERE email = ?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        }
        return null;
    }
    
    public Client authentifier(String email, String password) throws SQLException {
        String query = "SELECT * FROM clients WHERE email = ? AND password = ? AND actif = true";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        }
        return null;
    }
    
    public void modifier(Client client) throws SQLException {
        String query = "UPDATE clients SET nom=?, prenom=?, email=?, telephone=?, adresse=?, password=?, actif=?, total_depenses=?, nombre_courses=? WHERE id=?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelephone());
            ps.setString(5, client.getAdresse());
            ps.setString(6, client.getPassword());
            ps.setBoolean(7, client.isActif());
            ps.setDouble(8, client.getTotalDepenses());
            ps.setInt(9, client.getNombreCourses());
            ps.setInt(10, client.getId());
            ps.executeUpdate();
        }
    }
    
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients ORDER BY date_inscription DESC";
        
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }
    
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        client.setTelephone(rs.getString("telephone"));
        client.setAdresse(rs.getString("adresse"));
        client.setPassword(rs.getString("password"));
        client.setDateInscription(rs.getTimestamp("date_inscription"));
        client.setActif(rs.getBoolean("actif"));
        client.setTotalDepenses(rs.getDouble("total_depenses"));
        client.setNombreCourses(rs.getInt("nombre_courses"));
        return client;
    }
}