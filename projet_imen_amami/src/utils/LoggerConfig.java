package utils;

import java.util.logging.*;

public class LoggerConfig {
    private static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());
    
    public static void configurer() {
        try {
            // Créer le dossier logs s'il n'existe pas
            new java.io.File("logs").mkdirs();
            
            // Configurer le fichier de log
            FileHandler fileHandler = new FileHandler("logs/app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            
            // Niveau de log
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            
            // Logger aussi sur la console
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur configuration logger", e);
        }
    }
}