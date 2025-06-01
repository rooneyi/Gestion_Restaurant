package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_NAME = "restaurant_db";
    private static final String URL_WITHOUT_DB = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
    private static final String URL_WITH_DB = "jdbc:mysql://localhost:3306/" + DB_NAME + "?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void initialize() {
        try {
            // Étape 1 : Créer la base de données si elle n'existe pas
            try (Connection conn = DriverManager.getConnection(URL_WITHOUT_DB, USER, PASSWORD)) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                System.out.println("✅ Base de données vérifiée/créée");
            }

            // Étape 2 : Créer les tables nécessaires si elles n'existent pas
            try (Connection conn = DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD)) {
                Statement stmt = conn.createStatement();

                // Table Menu
                String createMenuTable = """
                    CREATE TABLE IF NOT EXISTS menu (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        price DOUBLE NOT NULL,
                        category VARCHAR(100) NOT NULL
                    );
                """;

                // Table User (optionnelle pour login si besoin)
                String createUserTable = """
    CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(100) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        is_admin BOOLEAN DEFAULT FALSE
    );
""";

                String createProductTable = """
    CREATE TABLE IF NOT EXISTS products (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        price DOUBLE NOT NULL,
        quantity INT NOT NULL
    );
""";
                stmt.executeUpdate(createProductTable);


                stmt.executeUpdate(createMenuTable);
                stmt.executeUpdate(createUserTable);

                System.out.println("✅ Tables vérifiées/créées");

            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur d'initialisation de la base de données : " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL_WITH_DB, USER, PASSWORD);
    }
}
