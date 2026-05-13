/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionparcautomobile.models;

/**
 *
 * @author AFFABLE MUSHOBOZI
 */
 public class Chauffeur {
    
    private int idChauffeur;
    private String nom;
    private String prenom;
    private String numPermis;
    private String telephone;
    
    // Constructeur par défaut
    public Chauffeur() {}
    
    // Constructeur sans ID
    public Chauffeur(String nom, String prenom, String numPermis, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.numPermis = numPermis;
        this.telephone = telephone;
    }
    
    // Constructeur complet
    public Chauffeur(int idChauffeur, String nom, String prenom, String numPermis, String telephone) {
        this.idChauffeur = idChauffeur;
        this.nom = nom;
        this.prenom = prenom;
        this.numPermis = numPermis;
        this.telephone = telephone;
    }
    
    //  GETTERS ET SETTERS 
    
    public int getIdChauffeur() {
        return idChauffeur;
    }
    
    public void setIdChauffeur(int idChauffeur) {
        this.idChauffeur = idChauffeur;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getNumPermis() {
        return numPermis;
    }
    
    public void setNumPermis(String numPermis) {
        this.numPermis = numPermis;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    // MÉTHODE toString 
    
    @Override
    public String toString() {
        return idChauffeur + " - " + nom + " " + prenom + " | Permis: " + numPermis 
               + " | Tél: " + telephone;
    }
}
