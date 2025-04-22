package views;

import services.CertificateManager;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CertificateViewer extends JDialog {
    public CertificateViewer(JFrame parent, String filename) {
        super(parent, "Certificat médical", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        
        File certFile = CertificateManager.getCertificate(filename);
        
        if (certFile.exists()) {
            JPanel panel = new JPanel(new BorderLayout());
            
            // Pour les PDF
            if (filename.toLowerCase().endsWith(".pdf")) {
                JLabel lblPdf = new JLabel(
                    "<html>PDF: " + filename + "<br>Ouvrir avec: " + certFile.getAbsolutePath() + "</html>");
                panel.add(lblPdf, BorderLayout.CENTER);
            } 
            // Pour les images
            else if (filename.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                ImageIcon icon = new ImageIcon(certFile.getAbsolutePath());
                JLabel lblImage = new JLabel(icon);
                panel.add(new JScrollPane(lblImage), BorderLayout.CENTER);
            } 
            // Autres types
            else {
                JTextArea txtContent = new JTextArea();
                txtContent.setText("Fichier: " + certFile.getAbsolutePath());
                panel.add(new JScrollPane(txtContent), BorderLayout.CENTER);
            }
            
            add(panel);
        } else {
            JOptionPane.showMessageDialog(this, "Fichier non trouvé");
            dispose();
        }
    }
}