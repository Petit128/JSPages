package com.textile.dao;

import com.textile.model.HistoriqueAction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueDAO {
    
    public void ajouterAction(HistoriqueAction action) throws SQLException {
        String sql = "INSERT INTO historique_actions (commande_id, action, utilisateur, details) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, action.getCommandeId());
            ps.setString(2, action.getAction());
            ps.setString(3, action.getUtilisateur());
            ps.setString(4, action.getDetails());
            ps.executeUpdate();
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public List<HistoriqueAction> getHistoriqueByCommandeId(int commandeId) throws SQLException {
        List<HistoriqueAction> historique = new ArrayList<>();
        String sql = "SELECT * FROM historique_actions WHERE commande_id = ? ORDER BY date_action DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, commandeId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                HistoriqueAction action = new HistoriqueAction();
                action.setId(rs.getInt("id"));
                action.setCommandeId(rs.getInt("commande_id"));
                action.setAction(rs.getString("action"));
                action.setUtilisateur(rs.getString("utilisateur"));
                action.setDateAction(rs.getTimestamp("date_action"));
                action.setDetails(rs.getString("details"));
                historique.add(action);
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return historique;
    }
}