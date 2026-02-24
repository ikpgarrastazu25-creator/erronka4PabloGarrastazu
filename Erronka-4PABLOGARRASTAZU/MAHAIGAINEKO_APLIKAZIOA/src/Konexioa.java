import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Konexioa {
    private static final String URL = "jdbc:mysql://localhost:3306/online_denda";
    private static final String ERABILTZAILEA = "root";
    private static final String PASAHITZA = ""; 

    public static Connection getKonexioa() {
        Connection konexioa = null;
        try {
            konexioa = DriverManager.getConnection(URL, ERABILTZAILEA, PASAHITZA);
            System.out.println("Ondo konektatuta");
        } catch (SQLException e) {
            System.out.println("Errorea datu-basearekin konektatzerakoan:");
            e.printStackTrace();
        }
        return konexioa;
    }
}