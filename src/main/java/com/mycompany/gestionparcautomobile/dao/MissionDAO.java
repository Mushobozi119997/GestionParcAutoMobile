/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.dao;

import com.mycompany.gestionparcautomobile.db.DatabaseConnection;
import com.mycompany.gestionparcautomobile.models.Mission;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author AFFABLE MUSHOBOZI
 */

/**
 * DAO pour la gestion des missions (CRUD)
 * @author AFFABLE MUSHOBOZI
 */

public class MissionDAO {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // ========== CREATE ==========
    /**
     * Ajouter une nouvelle mission dans la base
     */
    public void ajouterMission(Mission m) throws SQLException {
        String sql = "INSERT INTO mission (id_vehicule, id_chauffeur, date_depart, date_retour_prevu, destination) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, m.getIdVehicule());
            stmt.setInt(2, m.getIdChauffeur());
            stmt.setString(3, m.getDateDepart().format(FORMATTER));
            stmt.setString(4, m.getDateRetourPrevu().format(FORMATTER));
            stmt.setString(5, m.getDestination());
            
            stmt.executeUpdate();
        }
    }
    
    // ========== READ ==========
    /**
     * Lister toutes les missions avec les infos véhicule et chauffeur
     */
    public List<Mission> listerMissions() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT m.*, v.immatriculation, c.nom, c.prenom " +
                     "FROM mission m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "JOIN chauffeur c ON m.id_chauffeur = c.id_chauffeur " +
                     "ORDER BY m.date_depart DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Mission m = new Mission();
                m.setIdMission(rs.getInt("id_mission"));
                m.setIdVehicule(rs.getInt("id_vehicule"));
                m.setIdChauffeur(rs.getInt("id_chauffeur"));
                
                String dateDepartStr = rs.getString("date_depart");
                if (dateDepartStr != null) {
                    m.setDateDepart(LocalDateTime.parse(dateDepartStr, FORMATTER));
                }
                
                String dateRetourStr = rs.getString("date_retour_prevu");
                if (dateRetourStr != null) {
                    m.setDateRetourPrevu(LocalDateTime.parse(dateRetourStr, FORMATTER));
                }
                
                m.setDestination(rs.getString("destination"));
                m.setImmatriculationVehicule(rs.getString("immatriculation"));
                m.setNomChauffeur(rs.getString("nom") + " " + rs.getString("prenom"));
                
                missions.add(m);
            }
        }
        return missions;
    }
    
    /**
     * Trouver une mission par son ID
     */
    public Mission trouverMissionParId(int id) throws SQLException {
        String sql = "SELECT m.*, v.immatriculation, c.nom, c.prenom " +
                     "FROM mission m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "JOIN chauffeur c ON m.id_chauffeur = c.id_chauffeur " +
                     "WHERE m.id_mission = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Mission m = new Mission();
                m.setIdMission(rs.getInt("id_mission"));
                m.setIdVehicule(rs.getInt("id_vehicule"));
                m.setIdChauffeur(rs.getInt("id_chauffeur"));
                
                String dateDepartStr = rs.getString("date_depart");
                if (dateDepartStr != null) {
                    m.setDateDepart(LocalDateTime.parse(dateDepartStr, FORMATTER));
                }
                
                String dateRetourStr = rs.getString("date_retour_prevu");
                if (dateRetourStr != null) {
                    m.setDateRetourPrevu(LocalDateTime.parse(dateRetourStr, FORMATTER));
                }
                
                m.setDestination(rs.getString("destination"));
                m.setImmatriculationVehicule(rs.getString("immatriculation"));
                m.setNomChauffeur(rs.getString("nom") + " " + rs.getString("prenom"));
                
                return m;
            }
        }
        return null;
    }
    
    /**
     * Trouver les missions d'un véhicule spécifique
     */
    public List<Mission> trouverMissionsParVehicule(int idVehicule) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT m.*, v.immatriculation, c.nom, c.prenom " +
                     "FROM mission m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "JOIN chauffeur c ON m.id_chauffeur = c.id_chauffeur " +
                     "WHERE m.id_vehicule = ? " +
                     "ORDER BY m.date_depart DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVehicule);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Mission m = new Mission();
                m.setIdMission(rs.getInt("id_mission"));
                m.setIdVehicule(rs.getInt("id_vehicule"));
                m.setIdChauffeur(rs.getInt("id_chauffeur"));
                
                String dateDepartStr = rs.getString("date_depart");
                if (dateDepartStr != null) {
                    m.setDateDepart(LocalDateTime.parse(dateDepartStr, FORMATTER));
                }
                
                String dateRetourStr = rs.getString("date_retour_prevu");
                if (dateRetourStr != null) {
                    m.setDateRetourPrevu(LocalDateTime.parse(dateRetourStr, FORMATTER));
                }
                
                m.setDestination(rs.getString("destination"));
                m.setImmatriculationVehicule(rs.getString("immatriculation"));
                m.setNomChauffeur(rs.getString("nom") + " " + rs.getString("prenom"));
                
                missions.add(m);
            }
        }
        return missions;
    }
    
    /**
     * Trouver les missions d'un chauffeur spécifique
     */
    public List<Mission> trouverMissionsParChauffeur(int idChauffeur) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT m.*, v.immatriculation, c.nom, c.prenom " +
                     "FROM mission m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "JOIN chauffeur c ON m.id_chauffeur = c.id_chauffeur " +
                     "WHERE m.id_chauffeur = ? " +
                     "ORDER BY m.date_depart DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idChauffeur);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Mission m = new Mission();
                m.setIdMission(rs.getInt("id_mission"));
                m.setIdVehicule(rs.getInt("id_vehicule"));
                m.setIdChauffeur(rs.getInt("id_chauffeur"));
                
                String dateDepartStr = rs.getString("date_depart");
                if (dateDepartStr != null) {
                    m.setDateDepart(LocalDateTime.parse(dateDepartStr, FORMATTER));
                }
                
                String dateRetourStr = rs.getString("date_retour_prevu");
                if (dateRetourStr != null) {
                    m.setDateRetourPrevu(LocalDateTime.parse(dateRetourStr, FORMATTER));
                }
                
                m.setDestination(rs.getString("destination"));
                m.setImmatriculationVehicule(rs.getString("immatriculation"));
                m.setNomChauffeur(rs.getString("nom") + " " + rs.getString("prenom"));
                
                missions.add(m);
            }
        }
        return missions;
    }
    
    // ========== UPDATE ==========
    /**
     * Modifier une mission existante
     */
    public void modifierMission(Mission m) throws SQLException {
        String sql = "UPDATE mission SET id_vehicule = ?, id_chauffeur = ?, date_depart = ?, date_retour_prevu = ?, destination = ? WHERE id_mission = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, m.getIdVehicule());
            stmt.setInt(2, m.getIdChauffeur());
            stmt.setString(3, m.getDateDepart().format(FORMATTER));
            stmt.setString(4, m.getDateRetourPrevu().format(FORMATTER));
            stmt.setString(5, m.getDestination());
            stmt.setInt(6, m.getIdMission());
            
            stmt.executeUpdate();
        }
    }
    
    // ========== DELETE ==========
    /**
     * Supprimer une mission par son ID
     */
    public void supprimerMission(int id) throws SQLException {
        String sql = "DELETE FROM mission WHERE id_mission = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}