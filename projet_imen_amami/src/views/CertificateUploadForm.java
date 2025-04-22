package views;

import services.CertificateManager;
import javax.swing.*;

import dao.DemandeCongeDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class CertificateUploadForm extends JDialog {
    private final int demandeId;
    private JLabel lblFilePath;
    private File selectedFile;

    public CertificateUploadForm(DemandeCongeForm demandeCongeForm, int demandeId) {
        super(demandeCongeForm, "Ajouter un certificat médical", true);
        this.demandeId = demandeId;
        setSize(500, 200);
        setLocationRelativeTo(demandeCongeForm);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de sélection
        JButton btnSelect = new JButton("Sélectionner un fichier");
        lblFilePath = new JLabel("Aucun fichier sélectionné");
        
        btnSelect.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                lblFilePath.setText(selectedFile.getName());
            }
        });

        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.add(btnSelect, BorderLayout.WEST);
        filePanel.add(lblFilePath, BorderLayout.CENTER);

        // Bouton d'upload
        JButton btnUpload = new JButton("Envoyer le certificat");
        btnUpload.addActionListener(this::uploadCertificate);

        panel.add(filePanel, BorderLayout.CENTER);
        panel.add(btnUpload, BorderLayout.SOUTH);
        add(panel);
    }

    private void uploadCertificate(ActionEvent e) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier");
            return;
        }

        try {
            String savedPath = CertificateManager.saveCertificate(selectedFile, demandeId);
            // Mettre à jour la base de données
            DemandeCongeDAO dao = new DemandeCongeDAO();
            if (dao.ajouterCertificat(demandeId, savedPath)) {
                JOptionPane.showMessageDialog(this, "Certificat enregistré avec succès");
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de l'enregistrement: " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}