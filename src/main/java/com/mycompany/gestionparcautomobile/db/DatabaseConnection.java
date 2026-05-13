/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.db;

import java.sql.*;

/**
 *
 * @author AFFABLE MUSHOBOZI
 */




/**
 * Classe qui gère la connexion à la base de données MariaDB/MySQL
 */
public class DatabaseConnection {
    
    // Paramètres de connexion
    private static final String URL = "jdbc:mariadb://localhost:3306/GestionParcAuto";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 
    
    /**
     * Établit et retourne une connexion à la base de données
     * @return Connection objet de connexion
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Charger le driver MariaDB (optionnel pour les versions récentes)
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MariaDB non trouvé !", e);
        }
    }
    
    /**
     * Teste la connexion à la base de données
     */
    public static void testConnexion() {
        try (Connection conn = getConnection()) {
            System.out.println(" Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.out.println(" Erreur de connexion : " + e.getMessage());
        }
    }
}