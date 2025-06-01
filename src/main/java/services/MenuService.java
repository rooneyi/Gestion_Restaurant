package services;

import models.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private Connection connection;

    public MenuService() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant_db", "root", "");
            initMenuTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initMenuTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS menu (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100),
                price DOUBLE,
                category VARCHAR(100)
            )
        """);
    }

    public void addMenuItem(Menu menu) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO menu (name, price, category) VALUES (?, ?, ?)")) {
            stmt.setString(1, menu.getName());
            stmt.setDouble(2, menu.getPrice());
            stmt.setString(3, menu.getCategory());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMenuItem(Menu menu) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE menu SET name=?, price=?, category=? WHERE id=?")) {
            stmt.setString(1, menu.getName());
            stmt.setDouble(2, menu.getPrice());
            stmt.setString(3, menu.getCategory());
            stmt.setInt(4, menu.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenuItem(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM menu WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Menu> getAllMenus() {
        List<Menu> list = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu")) {
            while (rs.next()) {
                list.add(new Menu(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
