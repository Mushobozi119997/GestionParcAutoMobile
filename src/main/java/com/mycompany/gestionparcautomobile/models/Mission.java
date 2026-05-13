/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.models;

import java.time.LocalDateTime;

/**
 *
 * @author AFFABLE MUSHOBOZI
 */

public class Mission {
    
    private int idMission;
    private int idVehicule;
    private int idChauffeur;
    private LocalDateTime dateDepart;
    private LocalDateTime dateRetourPrevu;
    private String destination;
    
    // Champs additionnels pour affichage (jointures)
    private String immatriculationVehicule;
    private String nomChauffeur;
    
    // Constructeur par défaut
    public Mission() {}
    
    // Constructeur sans ID
    public Mission(int idVehicule, int idChauffeur, LocalDateTime dateDepart, 
                   LocalDateTime dateRetourPrevu, String destination) {
        this.idVehicule = idVehicule;
        this.idChauffeur = idChauffeur;
        this.dateDepart = dateDepart;
        this.dateRetourPrevu = dateRetourPrevu;
        this.destination = destination;
    }
    
    // Constructeur complet
    public Mission(int idMission, int idVehicule, int idChauffeur, LocalDateTime dateDepart, 
                   LocalDateTime dateRetourPrevu, String destination) {
        this.idMission = idMission;
        this.idVehicule = idVehicule;
        this.idChauffeur = idChauffeur;
        this.dateDepart = dateDepart;
        this.dateRetourPrevu = dateRetourPrevu;
        this.destination = destination;
    }
    
    // GETTERS ET SETTers
    
    public int getIdMission() {
        return idMission;
    }
    
    public void setIdMission(int idMission) {
        this.idMission = idMission;
    }
    
    public int getIdVehicule() {
        return idVehicule;
    }
    
    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }
    
    public int getIdChauffeur() {
        return idChauffeur;
    }
    
    public void setIdChauffeur(int idChauffeur) {
        this.idChauffeur = idChauffeur;
    }
    
    public LocalDateTime getDateDepart() {
        return dateDepart;
    }
    
    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }
    
    public LocalDateTime getDateRetourPrevu() {
        return dateRetourPrevu;
    }
    
    public void setDateRetourPrevu(LocalDateTime dateRetourPrevu) {
        this.dateRetourPrevu = dateRetourPrevu;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    // Getters et Setters pour les champs additionnels
    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }
    
    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }
    
    public String getNomChauffeur() {
        return nomChauffeur;
    }
    
    public void setNomChauffeur(String nomChauffeur) {
        this.nomChauffeur = nomChauffeur;
    }
    
    //MÉTHODE toString 
    
    @Override
    public String toString() {
        return "Mission " + idMission + " | Véhicule: " + immatriculationVehicule 
               + " | Chauffeur: " + nomChauffeur + " | Destination: " + destination;
    }
}
