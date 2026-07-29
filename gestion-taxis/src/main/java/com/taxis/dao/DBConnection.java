// dao/DBConnection.java
package com.taxis.dao;

import java.sql.*;

public class DBConnection {
    // Configuration de la base de données
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/gestion_taxis?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = ""; 
    
    // Utilisation de ThreadLocal pour éviter les problèmes de concurrence
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();
    
    static {
        try {
            // Charger le driver MySQL explicitement
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ ERREUR: Driver MySQL non trouvé!");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        Connection conn = connectionHolder.get();
        
        try {
            if (conn == null || conn.isClosed()) {
                // Établir une nouvelle connexion
                conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                connectionHolder.set(conn);
                System.out.println("✓ Nouvelle connexion à la base de données établie");
            }
        } catch (SQLException e) {
            System.err.println("✗ ERREUR de connexion à la base de données:");
            System.err.println("  URL: " + JDBC_URL);
            System.err.println("  User: " + JDBC_USER);
            System.err.println("  Message: " + e.getMessage());
            throw e; // Relancer l'exception pour la gestion dans les DAO
        }
        
        return conn;
    }
    
    public static void closeConnection() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                conn.close();
                connectionHolder.remove();
                System.out.println("✓ Connexion fermée");
            } catch (SQLException e) {
                System.err.println("✗ Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
    
    // Méthode utilitaire pour tester la connexion
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("✓ Test de connexion réussi!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Test de connexion échoué: " + e.getMessage());
        }
        return false;
    }
}