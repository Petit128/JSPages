package com.textile.dao;

import com.textile.model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    
    public void ajouterNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications (commande_id, destinataire, type, message) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, notification.getCommandeId());
            ps.setString(2, notification.getDestinataire());
            ps.setString(3, notification.getType());
            ps.setString(4, notification.getMessage());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                notification.setId(rs.getInt(1));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, null);
        }
    }
    
    public List<Notification> getNotificationsByDestinataire(String destinataire) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE destinataire = ? ORDER BY date_envoi DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, destinataire);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotification(rs));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return notifications;
    }
    
    public List<Notification> getNotificationsNonLues(String destinataire) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE destinataire = ? AND lue = FALSE ORDER BY date_envoi DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, destinataire);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotification(rs));
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return notifications;
    }
    
    public void marquerCommeLue(int id) throws SQLException {
        String sql = "UPDATE notifications SET lue = TRUE WHERE id = ?";
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
    
    public int getNombreNonLues(String destinataire) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE destinataire = ? AND lue = FALSE";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnexionDB.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, destinataire);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            ConnexionDB.closeResources(conn, ps, rs);
        }
        return 0;
    }
    
    private Notification extractNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setCommandeId(rs.getInt("commande_id"));
        notification.setDestinataire(rs.getString("destinataire"));
        notification.setType(rs.getString("type"));
        notification.setMessage(rs.getString("message"));
        notification.setLue(rs.getBoolean("lue"));
        notification.setDateEnvoi(rs.getTimestamp("date_envoi"));
        return notification;
    }
}