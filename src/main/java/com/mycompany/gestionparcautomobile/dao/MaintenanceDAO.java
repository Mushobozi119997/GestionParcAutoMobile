/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.dao;


import com.mycompany.gestionparcautomobile.db.DatabaseConnection;
import com.mycompany.gestionparcautomobile.models.Maintenance;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AFFABLE MUSHOBOZI
 */

public class MaintenanceDAO {
    
    // ========== CREATE ==========
    /**
     * Ajouter une nouvelle maintenance dans la base
     */
    public void ajouterMaintenance(Maintenance m) throws SQLException {
        String sql = "INSERT INTO maintenance (id_vehicule, type_intervention, date_intervention, cout_total) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, m.getIdVehicule());
            stmt.setString(2, m.getTypeIntervention());
            stmt.setDate(3, Date.valueOf(m.getDateIntervention()));
            stmt.setDouble(4, m.getCoutTotal());
            
            stmt.executeUpdate();
        }
    }
    
    // ========== READ ==========
    /**
     * Lister toutes les maintenances avec les infos véhicule
     */
    public List<Maintenance> listerMaintenances() throws SQLException {
        List<Maintenance> maintenances = new ArrayList<>();
        String sql = "SELECT m.*, v.immatriculation " +
                     "FROM maintenance m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "ORDER BY m.date_intervention DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Maintenance maint = new Maintenance();
                maint.setIdMaintenance(rs.getInt("id_maintenance"));
                maint.setIdVehicule(rs.getInt("id_vehicule"));
                maint.setTypeIntervention(rs.getString("type_intervention"));
                
                Date date = rs.getDate("date_intervention");
                if (date != null) {
                    maint.setDateIntervention(date.toLocalDate());
                }
                
                maint.setCoutTotal(rs.getDouble("cout_total"));
                maint.setImmatriculationVehicule(rs.getString("immatriculation"));
                
                maintenances.add(maint);
            }
        }
        return maintenances;
    }
    
    /**
     * Trouver une maintenance par son ID
     */
    public Maintenance trouverMaintenanceParId(int id) throws SQLException {
        String sql = "SELECT m.*, v.immatriculation " +
                     "FROM maintenance m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "WHERE m.id_maintenance = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Maintenance maint = new Maintenance();
                maint.setIdMaintenance(rs.getInt("id_maintenance"));
                maint.setIdVehicule(rs.getInt("id_vehicule"));
                maint.setTypeIntervention(rs.getString("type_intervention"));
                
                Date date = rs.getDate("date_intervention");
                if (date != null) {
                    maint.setDateIntervention(date.toLocalDate());
                }
                
                maint.setCoutTotal(rs.getDouble("cout_total"));
                maint.setImmatriculationVehicule(rs.getString("immatriculation"));
                
                return maint;
            }
        }
        return null;
    }
    
    /**
     * Trouver les maintenances d'un véhicule spécifique
     */
    public List<Maintenance> trouverMaintenancesParVehicule(int idVehicule) throws SQLException {
        List<Maintenance> maintenances = new ArrayList<>();
        String sql = "SELECT m.*, v.immatriculation " +
                     "FROM maintenance m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "WHERE m.id_vehicule = ? " +
                     "ORDER BY m.date_intervention DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVehicule);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Maintenance maint = new Maintenance();
                maint.setIdMaintenance(rs.getInt("id_maintenance"));
                maint.setIdVehicule(rs.getInt("id_vehicule"));
                maint.setTypeIntervention(rs.getString("type_intervention"));
                
                Date date = rs.getDate("date_intervention");
                if (date != null) {
                    maint.setDateIntervention(date.toLocalDate());
                }
                
                maint.setCoutTotal(rs.getDouble("cout_total"));
                maint.setImmatriculationVehicule(rs.getString("immatriculation"));
                
                maintenances.add(maint);
            }
        }
        return maintenances;
    }
    
    /**
     * Trouver les maintenances par type d'intervention
     */
    public List<Maintenance> trouverMaintenancesParType(String typeIntervention) throws SQLException {
        List<Maintenance> maintenances = new ArrayList<>();
        String sql = "SELECT m.*, v.immatriculation " +
                     "FROM maintenance m " +
                     "JOIN vehicule v ON m.id_vehicule = v.id_vehicule " +
                     "WHERE m.type_intervention LIKE ? " +
                     "ORDER BY m.date_intervention DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + typeIntervention + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Maintenance maint = new Maintenance();
                maint.setIdMaintenance(rs.getInt("id_maintenance"));
                maint.setIdVehicule(rs.getInt("id_vehicule"));
                maint.setTypeIntervention(rs.getString("type_intervention"));
                
                Date date = rs.getDate("date_intervention");
                if (date != null) {
                    maint.setDateIntervention(date.toLocalDate());
                }
                
                maint.setCoutTotal(rs.getDouble("cout_total"));
                maint.setImmatriculationVehicule(rs.getString("immatriculation"));
                
                maintenances.add(maint);
            }
        }
        return maintenances;
    }
    
    // ========== UPDATE ==========
    /**
     * Modifier une maintenance existante
     */
    public void modifierMaintenance(Maintenance m) throws SQLException {
        String sql = "UPDATE maintenance SET id_vehicule = ?, type_intervention = ?, date_intervention = ?, cout_total = ? WHERE id_maintenance = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, m.getIdVehicule());
            stmt.setString(2, m.getTypeIntervention());
            stmt.setDate(3, Date.valueOf(m.getDateIntervention()));
            stmt.setDouble(4, m.getCoutTotal());
            stmt.setInt(5, m.getIdMaintenance());
            
            stmt.executeUpdate();
        }
    }
    
    // ========== DELETE ==========
    /**
     * Supprimer une maintenance par son ID
     */
    public void supprimerMaintenance(int id) throws SQLException {
        String sql = "DELETE FROM maintenance WHERE id_maintenance = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    // ========== STATISTIQUES ==========
    /**
     * Obtenir le coût total de toutes les maintenances
     */
    public double getTotalCoutMaintenance() throws SQLException {
        String sql = "SELECT SUM(cout_total) as total FROM maintenance";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
    
    /**
     * Obtenir le coût total des maintenances pour un véhicule spécifique
     */
    public double getCoutMaintenanceParVehicule(int idVehicule) throws SQLException {
        String sql = "SELECT SUM(cout_total) as total FROM maintenance WHERE id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVehicule);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
}
