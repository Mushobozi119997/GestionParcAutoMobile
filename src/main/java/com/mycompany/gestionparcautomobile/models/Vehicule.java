/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.models;

/**
 *
 * @author AFFABLE MUSHOBOZI
 */

 



public class Vehicule {
    
    private int idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private String carburant;
    private int kilometrage;
    private String statut;
    
    // Constructeur par défaut
    public Vehicule() {}
    
    // Constructeur avec paramètres (sans ID)
    public Vehicule(String immatriculation, String marque, String modele, 
                    String carburant, int kilometrage, String statut) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.carburant = carburant;
        this.kilometrage = kilometrage;
        this.statut = statut;
    }
    
    // Constructeur complet (avec ID)
    public Vehicule(int idVehicule, String immatriculation, String marque, 
                    String modele, String carburant, int kilometrage, String statut) {
        this.idVehicule = idVehicule;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.carburant = carburant;
        this.kilometrage = kilometrage;
        this.statut = statut;
    }
    
    // l'encapsulation ou les  GETTERS ET SETTERS 
    
    public int getIdVehicule() {
        return idVehicule;
    }
    
    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }
    
    public String getImmatriculation() {
        return immatriculation;
    }
    
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
    
    public String getMarque() {
        return marque;
    }
    
    public void setMarque(String marque) {
        this.marque = marque;
    }
    
    public String getModele() {
        return modele;
    }
    
    public void setModele(String modele) {
        this.modele = modele;
    }
    
    public String getCarburant() {
        return carburant;
    }
    
    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }
    
    public int getKilometrage() {
        return kilometrage;
    }
    
    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    // ETHODE toString
    
    @Override
    public String toString() {
        return idVehicule + " - " + immatriculation + " | " + marque + " " + modele 
               + " | " + carburant + " | " + kilometrage + " km | " + statut;
    }
}