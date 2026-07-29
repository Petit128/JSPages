package com.parking.dao;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/parking_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL Driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }
    
    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection test: SUCCESS");
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED - " + e.getMessage());
        }
    }
}