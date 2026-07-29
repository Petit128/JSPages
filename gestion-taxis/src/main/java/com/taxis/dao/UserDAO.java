package com.taxis.dao;

import com.taxis.model.User;
import java.sql.*;

public class UserDAO {
    
    public User authentifier(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                return user;
            }
        }
        return null;
    }
    
    public User getById(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                return user;
            }
        }
        return null;
    }
    
    public void ajouter(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, nom, prenom, email, role) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNom());
            ps.setString(4, user.getPrenom());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getRole().name());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        }
    }
}