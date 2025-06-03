package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.Product;
import services.ProductService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductDashboardController implements Initializable {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TableColumn<Product, Integer> quantityCol;

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    private ProductService productService;
    private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            productService = new ProductService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        priceCol.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        refreshProductTable();

        productTable.setOnMouseClicked(this::onRowSelect);
    }

    private void onRowSelect(MouseEvent event) {
        selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            nameField.setText(selectedProduct.getName());
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
            quantityField.setText(String.valueOf(selectedProduct.getQuantity()));
        }
    }

    @FXML
    private void handleAddProduct() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        productService.addProduct(new Product(name, price, quantity));
        refreshProductTable();
        clearFields();
    }

    @FXML
    private void handleUpdateProduct() {
        if (selectedProduct == null) return;

        String newName = nameField.getText();
        double newPrice = Double.parseDouble(priceField.getText());
        int newQuantity = Integer.parseInt(quantityField.getText());

        Product updated = new Product(selectedProduct.getId(), newName, newPrice, newQuantity);
        productService.updateProduct(updated);
        refreshProductTable();
        clearFields();
    }


    @FXML
    private void handleDeleteProduct() {
        if (selectedProduct == null) return;

        productService.deleteProduct(String.valueOf((int)selectedProduct.getId()));
        refreshProductTable();
        clearFields();
    }


    private void refreshProductTable() {
        productTable.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        selectedProduct = null;
    }
}
