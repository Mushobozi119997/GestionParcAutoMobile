/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.dao;

import com.mycompany.gestionparcautomobile.db.DatabaseConnection;
import com.mycompany.gestionparcautomobile.models.Chauffeur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author AFFABLE MUSHOBOZI
 */



/**
 * DAO pour la gestion des chauffeurs (CRUD)
 * @author AFFABLE MUSHOBOZI
 */

public class ChauffeurDAO {
    
    // ========== CREATE ==========
    /**
     * Ajouter un nouveau chauffeur dans la base
     */
    public void ajouterChauffeur(Chauffeur c) throws SQLException {
        String sql = "INSERT INTO chauffeur (nom, prenom, num_permis, telephone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getNumPermis());
            stmt.setString(4, c.getTelephone());
            
            stmt.executeUpdate();
        }
    }
    
    // ========== READ ==========
    /**
     * Lister tous les chauffeurs
     */
    public List<Chauffeur> listerChauffeurs() throws SQLException {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String sql = "SELECT * FROM chauffeur ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Chauffeur c = new Chauffeur();
                c.setIdChauffeur(rs.getInt("id_chauffeur"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setNumPermis(rs.getString("num_permis"));
                c.setTelephone(rs.getString("telephone"));
                chauffeurs.add(c);
            }
        }
        return chauffeurs;
    }
    
    /**
     * Trouver un chauffeur par son ID
     */
    public Chauffeur trouverChauffeurParId(int id) throws SQLException {
        String sql = "SELECT * FROM chauffeur WHERE id_chauffeur = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Chauffeur c = new Chauffeur();
                c.setIdChauffeur(rs.getInt("id_chauffeur"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setNumPermis(rs.getString("num_permis"));
                c.setTelephone(rs.getString("telephone"));
                return c;
            }
        }
        return null;
    }
    
    /**
     * Trouver un chauffeur par son numéro de permis
     */
    public Chauffeur trouverChauffeurParPermis(String numPermis) throws SQLException {
        String sql = "SELECT * FROM chauffeur WHERE num_permis = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numPermis);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Chauffeur c = new Chauffeur();
                c.setIdChauffeur(rs.getInt("id_chauffeur"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setNumPermis(rs.getString("num_permis"));
                c.setTelephone(rs.getString("telephone"));
                return c;
            }
        }
        return null;
    }
    
    // ========== UPDATE ==========
    /**
     * Modifier un chauffeur existant
     */
    public void modifierChauffeur(Chauffeur c) throws SQLException {
        String sql = "UPDATE chauffeur SET nom = ?, prenom = ?, num_permis = ?, telephone = ? WHERE id_chauffeur = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getNumPermis());
            stmt.setString(4, c.getTelephone());
            stmt.setInt(5, c.getIdChauffeur());
            
            stmt.executeUpdate();
        }
    }
    
    // Suppression d'un chauffeur par son Id 
    /**
     * Supprimer un chauffeur par son ID
     */
    
    public void supprimerChauffeur(int id) throws SQLException {
        String sql = "DELETE FROM chauffeur WHERE id_chauffeur = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
