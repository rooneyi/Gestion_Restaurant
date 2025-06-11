package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import services.AuthService;
import services.FactureService;
import services.ProductService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import models.Product;

public class HomeDashboardController implements Initializable {

    @FXML private Label usersCountLabel;
    @FXML private Label dailySalesLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label outOfStockLabel;
    @FXML private Label totalRevenueLabel;

    private AuthService authService;
    private FactureService factureService;
    private ProductService productService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            authService = new AuthService();
            factureService = new FactureService();
            productService = new ProductService();
            
            loadDashboardData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDashboardData() {
        try {
            // Nombre d'utilisateurs
            int userCount = authService.getAllUsers().size();
            if (usersCountLabel != null) {
                usersCountLabel.setText(String.valueOf(userCount));
            }

            // Ventes du jour
            double dailyRevenue = factureService.getTotalRevenueByDate(LocalDate.now());
            if (dailySalesLabel != null) {
                dailySalesLabel.setText(String.format("%.0f", dailyRevenue));
            }

            // Statistiques de stock
            List<Product> products = productService.getAllProducts();
            long lowStockCount = products.stream().filter(p -> p.getQuantity() > 0 && p.getQuantity() < 10).count();
            long outOfStockCount = products.stream().filter(p -> p.getQuantity() == 0).count();

            if (lowStockLabel != null) {
                lowStockLabel.setText(String.valueOf(lowStockCount));
            }
            if (outOfStockLabel != null) {
                outOfStockLabel.setText(String.valueOf(outOfStockCount));
            }

            // Total argent (revenus du mois en cours)
            // Pour simplifier, on affiche les revenus du jour
            if (totalRevenueLabel != null) {
                totalRevenueLabel.setText(String.format("%.0f", dailyRevenue));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        loadDashboardData();
    }
}