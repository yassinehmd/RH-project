package models;

import java.time.LocalDate;

/**
 * Classe représentant un utilisateur du système.
 */
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String cin;
    private String service;
    private String role; // ENSEIGNANT, PERSONNEL, RH, ADMIN
    private String grade;
    private LocalDate dateNaissance;

    // Constructeur par défaut
    public Utilisateur() {
    }

    // Constructeur complet
    public Utilisateur(int id, String nom, String prenom, String email, String telephone,
                       String cin, String service, String role, String grade, LocalDate dateNaissance) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.cin = cin;
        this.service = service;
        this.role = role;
        this.grade = grade;
        this.dateNaissance = dateNaissance;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return nom + " " + prenom;
    }

    public boolean estEnseignant() {
        return "ENSEIGNANT".equalsIgnoreCase(role);
    }

    public boolean estRH() {
        return "RH".equalsIgnoreCase(role);
    }

    public boolean estAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean estPersonnel() {
        return "PERSONNEL".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", cin='" + cin + '\'' +
                ", service='" + service + '\'' +
                ", role='" + role + '\'' +
                ", grade='" + grade + '\'' +
                ", dateNaissance=" + dateNaissance +
                '}';
    }
}
