import utils.LoggerConfig;
import views.LoginFrame;
import utils.ConfigDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dao.ConnexionDB;


public class main {
    public static void main(String[] args) {
        LoggerConfig.configurer();
      	try{
      		Connection connexion = ConnexionDB.getConnexion();
      	

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });}
            catch(Exception e){
        	System.out.print(e);
        }
    }
}