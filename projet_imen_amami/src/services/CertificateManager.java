package services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CertificateManager {
    private static final String UPLOAD_DIR = "uploads/certificats/";

    public static String saveCertificate(File file, int demandeId) throws Exception {
        // Créer le dossier s'il n'existe pas
        new File(UPLOAD_DIR).mkdirs();
        
        String fileName = "certificat_" + demandeId + "_" + System.currentTimeMillis() + 
                         getFileExtension(file.getName());
        Path target = new File(UPLOAD_DIR + fileName).toPath();
        
        Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public static File getCertificate(String filename) {
        return new File(UPLOAD_DIR + filename);
    }

    private static String getFileExtension(String name) {
        int lastDot = name.lastIndexOf('.');
        return lastDot == -1 ? "" : name.substring(lastDot);
    }
}