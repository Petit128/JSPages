package com.textile.dao;

import com.textile.model.Commande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    
    public void ajouterCommande(Commande commande) throws SQLException {
        String sql = "INSERT INTO commandes (utilisateur_id, client, type_article, quantite, taille, couleur, date_commande, date_livraison_prevue, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, commande.getUtilisateurId());
            ps.setString(2, commande.getClient());
            ps.setString(3, commande.getTypeArticle());
            ps.setInt(4, commande.getQuantite());
            ps.setString(5, commande.getTaille());
            ps.setString(6, commande.getCouleur());
            ps.setDate(7, new java.sql.Date(commande.getDateCommande().getTime()));
            ps.setDate(8, new java.sql.Date(commande.getDateLivraisonPrevue().getTime()));
            ps.setString(9, commande.getStatut());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                commande.setId(rs.getInt(1));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    // Pour ADMIN et PRODUCTION - voir TOUTES les commandes
    public List<Commande> getAllCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes ORDER BY date_commande DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                commandes.add(extractCommande(rs));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return commandes;
    }
    
    // Pour CLIENT - voir seulement SES commandes
    public List<Commande> getCommandesByUtilisateurId(int utilisateurId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE utilisateur_id = ? ORDER BY date_commande DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, utilisateurId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                commandes.add(extractCommande(rs));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return commandes;
    }
    
    // NOUVEAU : Récupérer les commandes par statut
    public List<Commande> getCommandesByStatut(String statut) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE statut = ? ORDER BY date_commande DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                commandes.add(extractCommande(rs));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return commandes;
    }
    
    // NOUVEAU : Récupérer les commandes par statut pour un utilisateur spécifique
    public List<Commande> getCommandesByStatutAndUtilisateur(String statut, int utilisateurId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes WHERE statut = ? AND utilisateur_id = ? ORDER BY date_commande DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, statut);
            ps.setInt(2, utilisateurId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                commandes.add(extractCommande(rs));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return commandes;
    }
    
    public Commande getCommandeById(int id) throws SQLException {
        String sql = "SELECT * FROM commandes WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractCommande(rs);
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return null;
    }
    
    // Vérifier si une commande appartient à un utilisateur
    public boolean isCommandeAccessible(int commandeId, int utilisateurId, String role) throws SQLException {
        if ("ADMIN".equals(role)) {
            return true; // Admin voit tout
        }
        
        String sql = "SELECT id FROM commandes WHERE id = ? AND utilisateur_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, commandeId);
            ps.setInt(2, utilisateurId);
            rs = ps.executeQuery();
            
            return rs.next();
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
    }
    
    public void updateCommande(Commande commande) throws SQLException {
        String sql = "UPDATE commandes SET client=?, type_article=?, quantite=?, taille=?, couleur=?, date_livraison_prevue=?, statut=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, commande.getClient());
            ps.setString(2, commande.getTypeArticle());
            ps.setInt(3, commande.getQuantite());
            ps.setString(4, commande.getTaille());
            ps.setString(5, commande.getCouleur());
            ps.setDate(6, new java.sql.Date(commande.getDateLivraisonPrevue().getTime()));
            ps.setString(7, commande.getStatut());
            ps.setInt(8, commande.getId());
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public void deleteCommande(int id) throws SQLException {
        String sql = "DELETE FROM commandes WHERE id=?";
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
    
    public void mettreAJourStatut(int id, String nouveauStatut) throws SQLException {
        String sql = "UPDATE commandes SET statut = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nouveauStatut);
            ps.setInt(2, id);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    private Commande extractCommande(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id"));
        
        // Gérer le cas où utilisateur_id peut être NULL
        try {
            commande.setUtilisateurId(rs.getInt("utilisateur_id"));
        } catch (SQLException e) {
            commande.setUtilisateurId(0);
        }
        
        commande.setClient(rs.getString("client"));
        
        // Gérer les nouvelles colonnes
        try {
            commande.setTypeArticle(rs.getString("type_article"));
        } catch (SQLException e) {
            commande.setTypeArticle("");
        }
        
        try {
            commande.setQuantite(rs.getInt("quantite"));
        } catch (SQLException e) {
            commande.setQuantite(1);
        }
        
        try {
            commande.setTaille(rs.getString("taille"));
        } catch (SQLException e) {
            commande.setTaille("");
        }
        
        try {
            commande.setCouleur(rs.getString("couleur"));
        } catch (SQLException e) {
            commande.setCouleur("");
        }
        
        commande.setDateCommande(rs.getDate("date_commande"));
        commande.setDateLivraisonPrevue(rs.getDate("date_livraison_prevue"));
        commande.setStatut(rs.getString("statut"));
        return commande;
    }
}