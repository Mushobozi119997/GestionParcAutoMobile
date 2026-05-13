/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.models;

import java.time.LocalDate;

/**
 *
 * @author AFFABLE MUSHOBOZI
 */

public class Maintenance {
    
    private int idMaintenance;
    private int idVehicule;
    private String typeIntervention;
    private LocalDate dateIntervention;
    private double coutTotal;
    
    // Champ additionnel pour affichage
    private String immatriculationVehicule;
    
    // Constructeur par défaut
    public Maintenance() {}
    
    // Constructeur sans ID
    public Maintenance(int idVehicule, String typeIntervention, 
                       LocalDate dateIntervention, double coutTotal) {
        this.idVehicule = idVehicule;
        this.typeIntervention = typeIntervention;
        this.dateIntervention = dateIntervention;
        this.coutTotal = coutTotal;
    }
    
    // Constructeur complet
    public Maintenance(int idMaintenance, int idVehicule, String typeIntervention, 
                       LocalDate dateIntervention, double coutTotal) {
        this.idMaintenance = idMaintenance;
        this.idVehicule = idVehicule;
        this.typeIntervention = typeIntervention;
        this.dateIntervention = dateIntervention;
        this.coutTotal = coutTotal;
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public int getIdMaintenance() {
        return idMaintenance;
    }
    
    public void setIdMaintenance(int idMaintenance) {
        this.idMaintenance = idMaintenance;
    }
    
    public int getIdVehicule() {
        return idVehicule;
    }
    
    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }
    
    public String getTypeIntervention() {
        return typeIntervention;
    }
    
    public void setTypeIntervention(String typeIntervention) {
        this.typeIntervention = typeIntervention;
    }
    
    public LocalDate getDateIntervention() {
        return dateIntervention;
    }
    
    public void setDateIntervention(LocalDate dateIntervention) {
        this.dateIntervention = dateIntervention;
    }
    
    public double getCoutTotal() {
        return coutTotal;
    }
    
    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }
    
    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }
    
    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }
    
    // ========== MÉTHODE toString ==========
    
    @Override
    public String toString() {
        return "Maintenance #" + idMaintenance + " | Véhicule: " + immatriculationVehicule 
               + " | Intervention: " + typeIntervention + " | Date: " + dateIntervention 
               + " | Coût: " + coutTotal + " €";
    }
}
