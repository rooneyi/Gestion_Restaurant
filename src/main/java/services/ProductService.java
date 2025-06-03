package services;

import models.Product;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final Connection connection;

    public ProductService() throws SQLException {
        this.connection = DBConnection.getConnection();
        initTable();
    }

    private void initTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS products (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100),
                price DOUBLE,
                quantity INT
            )
        """);
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM products");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addProduct(Product p) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO products (name,price,quantity) VALUES (?, ?, ?)")) {
            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getQuantity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product p) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE products SET name=?, price=?, quantity=? WHERE id=?")) {
            stmt.setString(1, p.getName());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(2, p.getQuantity());
            stmt.setInt(4, p.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM products WHERE id=?")) {
            stmt.setInt(1, Integer.parseInt(id));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
