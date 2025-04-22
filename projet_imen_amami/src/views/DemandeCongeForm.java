package views;

import models.DemandeConge;
import models.Utilisateur;
import services.CertificateManager;
import services.GestionCongeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DemandeCongeForm extends JDialog {
    private final Utilisateur enseignant;
    private final GestionCongeService congeService;
    private JComboBox<String> typeCongeCombo;
    private JSpinner dateDebutSpinner;
    private JSpinner dateFinSpinner;
    private JButton btnJoindreCertificat;
    private File certificatFile;
    private int demandeId;

    public DemandeCongeForm(JFrame parent, Utilisateur enseignant) {
        super(parent, "Nouvelle demande de congé", true);
        this.enseignant = enseignant;
        this.congeService = new GestionCongeService();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        initUI();
        initListeners();
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Type de congé
        formPanel.add(new JLabel("Type de congé:"));
        String[] types = {"Annuel", "Maladie", "Maternité", "Familial", "Formation"};
        typeCongeCombo = new JComboBox<>(types);
        formPanel.add(typeCongeCombo);

        // Date début
        formPanel.add(new JLabel("Date début:"));
        dateDebutSpinner = new JSpinner(new SpinnerDateModel());
        dateDebutSpinner.setEditor(new JSpinner.DateEditor(dateDebutSpinner, "dd/MM/yyyy"));
        formPanel.add(dateDebutSpinner);

        // Date fin
        formPanel.add(new JLabel("Date fin:"));
        dateFinSpinner = new JSpinner(new SpinnerDateModel());
        dateFinSpinner.setEditor(new JSpinner.DateEditor(dateFinSpinner, "dd/MM/yyyy"));
        formPanel.add(dateFinSpinner);

        // Bouton joindre certificat
        btnJoindreCertificat = new JButton("Joindre certificat médical");
        btnJoindreCertificat.setEnabled(false);
        formPanel.add(btnJoindreCertificat);

        add(formPanel, BorderLayout.CENTER);

        // Panel boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmit = new JButton("Soumettre");
        JButton btnCancel = new JButton("Annuler");
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSubmit);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initListeners() {
        typeCongeCombo.addActionListener(e -> {
            String selectedType = (String) typeCongeCombo.getSelectedItem();
            btnJoindreCertificat.setEnabled("Maladie".equals(selectedType) || "Maternité".equals(selectedType));
        });

        btnJoindreCertificat.addActionListener(this::joindreCertificat);
    }

    private void joindreCertificat(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner un certificat médical");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Documents PDF", "pdf"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Images (JPG, PNG)", "jpg", "jpeg", "png"));

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            certificatFile = fileChooser.getSelectedFile();
            btnJoindreCertificat.setText(certificatFile.getName());
        }
    }

    private boolean validerFormulaire() {
        LocalDate dateDebut = ((java.util.Date) dateDebutSpinner.getValue()).toInstant()
                          .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate dateFin = ((java.util.Date) dateFinSpinner.getValue()).toInstant()
                          .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        if (dateFin.isBefore(dateDebut)) {
            JOptionPane.showMessageDialog(this,
                "La date de fin doit être après la date de début",
                "Erreur de validation",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String type = (String) typeCongeCombo.getSelectedItem();
        if (("Maladie".equals(type) || "Maternité".equals(type)) && certificatFile == null) {
            JOptionPane.showMessageDialog(this,
                "Un certificat médical est requis pour ce type de congé",
                "Certificat manquant",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void soumettreDemande() {
        if (!validerFormulaire()) return;

        try {
            // Créer l'objet DemandeConge
            DemandeConge demande = new DemandeConge();
            demande.setTypeConge((String) typeCongeCombo.getSelectedItem());
            demande.setDateDebut(((java.util.Date) dateDebutSpinner.getValue()).toInstant()
                              .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            demande.setDateFin(((java.util.Date) dateFinSpinner.getValue()).toInstant()
                              .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            demande.setStatut("EN_ATTENTE");
            demande.setEnseignant(enseignant);

            // Sauvegarder la demande
            if (congeService.creerDemande(demande)) {
                this.demandeId = demande.getId();

                // Gérer le certificat si nécessaire
                if (certificatFile != null) {
                    String savedPath = CertificateManager.saveCertificate(certificatFile, demandeId);
                    demande.setCertificatMedical(savedPath);
                    congeService.mettreAJourDemande(demande);
                }

                JOptionPane.showMessageDialog(this,
                    "Demande enregistrée avec succès (ID: " + demandeId + ")",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                throw new Exception("Erreur lors de l'enregistrement");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur technique: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public int getDemandeId() {
        return demandeId;
    }
}