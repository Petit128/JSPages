package com.textile.dao;

import com.textile.model.EtapeProduction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtapeProductionDAO {
    
    public void ajouterEtape(EtapeProduction etape) throws SQLException {
        String sql = "INSERT INTO etapes_production (commande_id, nom_etape, statut, responsable, duree_prevue_jours) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, etape.getCommandeId());
            ps.setString(2, etape.getNomEtape());
            ps.setString(3, etape.getStatut());
            ps.setString(4, etape.getResponsable());
            ps.setInt(5, etape.getDureePrevueJours());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                etape.setId(rs.getInt(1));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public List<EtapeProduction> getEtapesByCommandeId(int commandeId) throws SQLException {
        List<EtapeProduction> etapes = new ArrayList<>();
        String sql = "SELECT * FROM etapes_production WHERE commande_id = ? ORDER BY id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, commandeId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                EtapeProduction etape = new EtapeProduction();
                etape.setId(rs.getInt("id"));
                etape.setCommandeId(rs.getInt("commande_id"));
                etape.setNomEtape(rs.getString("nom_etape"));
                etape.setDateDebut(rs.getDate("date_debut"));
                etape.setDateFin(rs.getDate("date_fin"));
                etape.setStatut(rs.getString("statut"));
                etape.setResponsable(rs.getString("responsable"));
                etape.setCommentaire(rs.getString("commentaire"));
                etape.setDureePrevueJours(rs.getInt("duree_prevue_jours"));
                etape.setDateFinPrevue(rs.getDate("date_fin_prevue"));
                etapes.add(etape);
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return etapes;
    }
    
    public EtapeProduction getEtapeById(int etapeId) throws SQLException {
        String sql = "SELECT * FROM etapes_production WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, etapeId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                EtapeProduction etape = new EtapeProduction();
                etape.setId(rs.getInt("id"));
                etape.setCommandeId(rs.getInt("commande_id"));
                etape.setNomEtape(rs.getString("nom_etape"));
                etape.setDateDebut(rs.getDate("date_debut"));
                etape.setDateFin(rs.getDate("date_fin"));
                etape.setStatut(rs.getString("statut"));
                etape.setResponsable(rs.getString("responsable"));
                etape.setCommentaire(rs.getString("commentaire"));
                etape.setDureePrevueJours(rs.getInt("duree_prevue_jours"));
                etape.setDateFinPrevue(rs.getDate("date_fin_prevue"));
                return etape;
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public void demarrerEtape(int etapeId) throws SQLException {
        String sql = "UPDATE etapes_production SET statut = 'EN_COURS', date_debut = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            ps.setInt(2, etapeId);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public void terminerEtape(int etapeId, String commentaire) throws SQLException {
        String sql = "UPDATE etapes_production SET statut = 'TERMINEE', date_fin = ?, commentaire = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
            ps.setString(2, commentaire);
            ps.setInt(3, etapeId);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public void updateDateFinPrevue(int etapeId, Date dateFinPrevue) throws SQLException {
        String sql = "UPDATE etapes_production SET date_fin_prevue = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dateFinPrevue.getTime()));
            ps.setInt(2, etapeId);
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public EtapeProduction getEtapePrecedente(int commandeId, String nomEtape) throws SQLException {
        String ordre = "SELECT id FROM etapes_production WHERE commande_id = ? AND nom_etape = ?";
        String[] etapes = {"COUPE", "COUTURE", "FINITION", "LIVRAISON"};
        int index = -1;
        
        for (int i = 0; i < etapes.length; i++) {
            if (etapes[i].equals(nomEtape)) {
                index = i;
                break;
            }
        }
        
        if (index > 0) {
            String etapePrecedente = etapes[index - 1];
            String sql = "SELECT * FROM etapes_production WHERE commande_id = ? AND nom_etape = ?";
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            try {
                conn = ConnexionDB.getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, commandeId);
                ps.setString(2, etapePrecedente);
                rs = ps.executeQuery();
                
                if (rs.next()) {
                    EtapeProduction etape = new EtapeProduction();
                    etape.setId(rs.getInt("id"));
                    etape.setStatut(rs.getString("statut"));
                    return etape;
                }
            } finally {
                ConnexionDB.closeResources(conn, ps, rs);
            }
        }
        return null;
    }
}