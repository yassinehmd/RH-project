package views;

import models.DemandeConge;
import models.RepriseTravail;
import services.GestionCongeService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class RepriseTravailForm extends JDialog {
    private DemandeConge demande;
    private GestionCongeService congeService;
    
    private JSpinner spinnerDateReprise;
    private JButton btnValider;
    
    public RepriseTravailForm(JFrame parent, DemandeConge demande) {
        super(parent, "Enregistrement reprise de travail", true);
        this.demande = demande;
        this.congeService = new GestionCongeService();
        
        setSize(400, 200);
        setLocationRelativeTo(parent);
        
        initUI();
    }
    
    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Info demande
        panel.add(new JLabel("Demande de congé:"));
        panel.add(new JLabel(demande.getTypeConge() + " (" + demande.getDateDebut() + " - " + demande.getDateFin() + ")"));
        
        // Date reprise
        panel.add(new JLabel("Date de reprise:"));
        spinnerDateReprise = new JSpinner(new SpinnerDateModel());
        spinnerDateReprise.setEditor(new JSpinner.DateEditor(spinnerDateReprise, "dd/MM/yyyy"));
        panel.add(spinnerDateReprise);
        
        btnValider = new JButton("Enregistrer");
        btnValider.addActionListener(this::enregistrerReprise);
        
        add(panel, BorderLayout.CENTER);
        add(btnValider, BorderLayout.SOUTH);
    }
    
    private void enregistrerReprise(ActionEvent e) {
        LocalDate dateReprise = ((java.util.Date) spinnerDateReprise.getValue()).toInstant()
                          .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        
        if (dateReprise.isBefore(demande.getDateFin())) {
            JOptionPane.showMessageDialog(this, "La date de reprise doit être après la fin du congé");
            return;
        }
        
        RepriseTravail reprise = new RepriseTravail(dateReprise, demande);
        
        if (congeService.enregistrerReprise(reprise)) {
            JOptionPane.showMessageDialog(this, "Reprise enregistrée avec succès");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}