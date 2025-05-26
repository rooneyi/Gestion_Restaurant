// services/AuthService.java
package services;

import models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private Connection connection;

    public AuthService() {
        try {
            // Connexion à la base de données (MySQL)
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public User getUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String uname = rs.getString("username");
                String pwd = rs.getString("password");
                boolean isAdmin = rs.getBoolean("is_admin");
                return new User(uname, pwd, isAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(String username, String password, boolean isAdmin) {
        String query = "INSERT INTO users (username, password, is_admin) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, isAdmin);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}