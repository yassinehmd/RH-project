package models;

import java.time.LocalDate;

public class RepriseTravail {
    private int id;
    private LocalDate dateReprise;
    private DemandeConge demandeConge;
    
    // Constructeurs
    public RepriseTravail() {}
    
    public RepriseTravail(LocalDate dateReprise, DemandeConge demandeConge) {
        this.dateReprise = dateReprise;
        this.demandeConge = demandeConge;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDate getDateReprise() { return dateReprise; }
    public void setDateReprise(LocalDate dateReprise) { 
        this.dateReprise = dateReprise; 
    }
    
    public DemandeConge getDemandeConge() { return demandeConge; }
    public void setDemandeConge(DemandeConge demandeConge) { 
        this.demandeConge = demandeConge; 
    }
}