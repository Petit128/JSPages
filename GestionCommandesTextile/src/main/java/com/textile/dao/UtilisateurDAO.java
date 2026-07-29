package com.textile.dao;

import com.textile.model.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    
    // Méthode d'insertion avec MD5 côté Java
    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe, role, actif) VALUES (?, ?, MD5(?), ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getMotDePasse());  // MD5 sera appliqué par MySQL
            ps.setString(4, utilisateur.getRole());
            ps.setBoolean(5, utilisateur.isActif());
            
            System.out.println("Insertion utilisateur: " + utilisateur.getEmail() + " avec role: " + utilisateur.getRole());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                utilisateur.setId(rs.getInt(1));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    // Vérification de l'authentification avec MD5
    public boolean verifierAuthentification(String email, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = MD5(?) AND actif = TRUE";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            rs = ps.executeQuery();
            
            boolean existe = rs.next();
            System.out.println("Authentification pour " + email + ": " + (existe ? "SUCCÈS" : "ÉCHEC"));
            return existe;
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
    }
    
    // Récupérer utilisateur par email
    public Utilisateur getUtilisateurByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setActif(rs.getBoolean("actif"));
                utilisateur.setCreatedAt(rs.getTimestamp("created_at"));
                utilisateur.setLastLogin(rs.getTimestamp("last_login"));
                return utilisateur;
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public Utilisateur getUtilisateurById(int id) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setActif(rs.getBoolean("actif"));
                utilisateur.setCreatedAt(rs.getTimestamp("created_at"));
                utilisateur.setLastLogin(rs.getTimestamp("last_login"));
                return utilisateur;
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs ORDER BY nom";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateur.setActif(rs.getBoolean("actif"));
                utilisateur.setCreatedAt(rs.getTimestamp("created_at"));
                utilisateur.setLastLogin(rs.getTimestamp("last_login"));
                utilisateurs.add(utilisateur);
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return utilisateurs;
    }
    
    public void updateUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE utilisateurs SET nom=?, email=?, role=?, actif=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getEmail());
            ps.setString(3, utilisateur.getRole());
            ps.setBoolean(4, utilisateur.isActif());
            ps.setInt(5, utilisateur.getId());
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public void updateLastLogin(int id, Timestamp lastLogin) throws SQLException {
        String sql = "UPDATE utilisateurs SET last_login = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, lastLogin);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public void deleteUtilisateur(int id) throws SQLException {
        String sql = "DELETE FROM utilisateurs WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
}