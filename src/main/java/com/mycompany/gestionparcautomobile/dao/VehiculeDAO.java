/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.dao;

import com.mycompany.gestionparcautomobile.db.DatabaseConnection;
import com.mycompany.gestionparcautomobile.models.Vehicule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author AFFABLE MUSHOBOZI
 */

/**
 * DAO pour la gestion des véhicules (CRUD)
 * @author AFFABLE MUSHOBOZI
 */
public class VehiculeDAO {
    
    // ========== CREATE ==========
    /**
     * Ajouter un nouveau véhicule dans la base
     */
    public void ajouterVehicule(Vehicule v) throws SQLException {
        String sql = "INSERT INTO vehicule (immatriculation, marque, modele, carburant, kilometrage, statut) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, v.getImmatriculation());
            stmt.setString(2, v.getMarque());
            stmt.setString(3, v.getModele());
            stmt.setString(4, v.getCarburant());
            stmt.setInt(5, v.getKilometrage());
            stmt.setString(6, v.getStatut());
            
            stmt.executeUpdate();
        }
    }
    
    // ========== READ ==========
    /**
     * Lister tous les véhicules
     */
    public List<Vehicule> listerVehicules() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM vehicule ORDER BY id_vehicule";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vehicule v = new Vehicule();
                v.setIdVehicule(rs.getInt("id_vehicule"));
                v.setImmatriculation(rs.getString("immatriculation"));
                v.setMarque(rs.getString("marque"));
                v.setModele(rs.getString("modele"));
                v.setCarburant(rs.getString("carburant"));
                v.setKilometrage(rs.getInt("kilometrage"));
                v.setStatut(rs.getString("statut"));
                vehicules.add(v);
            }
        }
        return vehicules;
    }
    
    /**
     * Trouver un véhicule par son ID
     */
    public Vehicule trouverVehiculeParId(int id) throws SQLException {
        String sql = "SELECT * FROM vehicule WHERE id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Vehicule v = new Vehicule();
                v.setIdVehicule(rs.getInt("id_vehicule"));
                v.setImmatriculation(rs.getString("immatriculation"));
                v.setMarque(rs.getString("marque"));
                v.setModele(rs.getString("modele"));
                v.setCarburant(rs.getString("carburant"));
                v.setKilometrage(rs.getInt("kilometrage"));
                v.setStatut(rs.getString("statut"));
                return v;
            }
        }
        return null;
    }
    
    /**
     * Trouver les véhicules disponibles (pas en mission, pas en maintenance)
     */
    public List<Vehicule> trouverVehiculesDisponibles() throws SQLException {
        List<Vehicule> vehicules = new ArrayList<>();
        String sql = "SELECT * FROM vehicule WHERE statut = 'Disponible' ORDER BY marque";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vehicule v = new Vehicule();
                v.setIdVehicule(rs.getInt("id_vehicule"));
                v.setImmatriculation(rs.getString("immatriculation"));
                v.setMarque(rs.getString("marque"));
                v.setModele(rs.getString("modele"));
                v.setCarburant(rs.getString("carburant"));
                v.setKilometrage(rs.getInt("kilometrage"));
                v.setStatut(rs.getString("statut"));
                vehicules.add(v);
            }
        }
        return vehicules;
    }
    
    // ========== UPDATE ==========
    /**
     * Modifier un véhicule existant
     */
    public void modifierVehicule(Vehicule v) throws SQLException {
        String sql = "UPDATE vehicule SET immatriculation = ?, marque = ?, modele = ?, carburant = ?, kilometrage = ?, statut = ? WHERE id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, v.getImmatriculation());
            stmt.setString(2, v.getMarque());
            stmt.setString(3, v.getModele());
            stmt.setString(4, v.getCarburant());
            stmt.setInt(5, v.getKilometrage());
            stmt.setString(6, v.getStatut());
            stmt.setInt(7, v.getIdVehicule());
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Modifier uniquement le statut d'un véhicule
     */
    public void modifierStatutVehicule(int idVehicule, String nouveauStatut) throws SQLException {
        String sql = "UPDATE vehicule SET statut = ? WHERE id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nouveauStatut);
            stmt.setInt(2, idVehicule);
            stmt.executeUpdate();
        }
    }
    
    
    // Supprimer un véhicule par son ID
     
    public void supprimerVehicule(int id) throws SQLException {
        String sql = "DELETE FROM vehicule WHERE id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
