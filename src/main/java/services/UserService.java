package services;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import utils.DBConnection;
import utils.DatabaseInitializer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final Connection connection;

    public UserService() {
        try {
            DatabaseInitializer.initialize();
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la BDD", e);
        }
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, password, is_admin, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword); // <-- HASH ICI
            stmt.setBoolean(3, user.isAdmin());
            stmt.setDate(4, Date.valueOf(user.getCreatedAt()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("is_admin"),
                        rs.getDate("created_at").toLocalDate()
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, password = ?, is_admin = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isAdmin());
            stmt.setInt(4, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("is_admin"),
                        rs.getDate("created_at").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
