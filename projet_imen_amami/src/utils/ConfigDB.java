package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigDB {
    private static Properties properties;
    
    static {
        properties = new Properties();
        try (InputStream input = ConfigDB.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                System.err.println("Fichier config.properties introuvable");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture config.properties: " + e.getMessage());
        }
    }
    
    public static String getDbUrl() {
    	System.out.print(properties.getProperty("url"));
    	System.out.print("jjjjjjjjjjjjjjjjjjj'");

        return properties.getProperty("url");
    }
    
    public static String getDbUser() {
        return properties.getProperty("user");
    }
    
    public static String getDbPassword() {
        return properties.getProperty("password");
    }
    
    public static String getJoursFeriesDefaut() {
        return properties.getProperty("gestion_conges_isae");
    }
}