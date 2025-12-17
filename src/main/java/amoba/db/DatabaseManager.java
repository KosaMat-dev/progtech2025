package amoba.db;

import java.sql.*;

public class DatabaseManager {
    // A kapcsolati sztring végére tett ;DB_CLOSE_DELAY=-1 segít, hogy ne álljon le az adatbázis azonnal
    private static final String URL = "jdbc:h2:./amoba_db;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    static {
        try {
            // Ez a sor kényszeríti a meghajtó betöltését, ami megoldja a "No suitable driver" hibát
            Class.forName("org.h2.Driver");
            initDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("H2 Driver nem található: " + e.getMessage());
        }
    }

    private static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS highscores (name VARCHAR(255) PRIMARY KEY, wins INT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveWin(String name) {
        String sql = "MERGE INTO highscores KEY(name) VALUES (?, COALESCE((SELECT wins FROM highscores WHERE name = ?), 0) + 1)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void printHighScores() {
        System.out.println("\n--- RANGLISTA ---");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM highscores ORDER BY wins DESC")) {
            while (rs.next()) {
                System.out.println(rs.getString("name") + ": " + rs.getInt("wins") + " győzelem");
            }
        } catch (SQLException e) {
            System.out.println("Hiba a ranglista lekérésekor.");
        }
    }
}
