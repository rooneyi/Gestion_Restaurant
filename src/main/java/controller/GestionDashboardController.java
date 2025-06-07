package controller;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Product;
import services.ProductService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class GestionDashboardController implements Initializable {

    @FXML private ChoiceBox<String> filterChoiceBox;
    @FXML private ChoiceBox<String> categorieChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Product> productsTable;

    @FXML private TextField designationField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private TextField filterField;

    @FXML private TableColumn<Product, String> colDesignation;
    @FXML private TableColumn<Product, String> colCategorie;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, Double> colPrixUnitaire;
    @FXML private TableColumn<Product, Double> colPrixTotal;
    @FXML private TableColumn<Product, String> colDate;

    private ProductService productService;
    private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            productService = new ProductService();

            // Initialisation des ChoiceBox
            filterChoiceBox.getItems().addAll("Tous", "Aliments", "Boissons", "Desserts");
            categorieChoiceBox.getItems().addAll("Aliments", "Boissons", "Desserts");

            // Configuration des colonnes
            colDesignation.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCategorie.setCellValueFactory(new PropertyValueFactory<>("category"));
            colStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("price"));
            colPrixTotal.setCellValueFactory(data ->
                    new ReadOnlyDoubleWrapper(data.getValue().getPrice() * data.getValue().getQuantity()).asObject());
            colDate.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getDate().toString()));

            filterChoiceBox.getSelectionModel().selectFirst();
            filterField.textProperty().addListener((obs, oldVal, newVal) -> filterProducts());

            productsTable.setOnMouseClicked(this::onRowSelect);
            refreshProductTable();

        } catch (SQLException e) {
            showAlert("Erreur de connexion à la base de données");
            e.printStackTrace();
        }
    }

    private void onRowSelect(MouseEvent event) {
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            designationField.setText(selectedProduct.getName());
            categorieChoiceBox.setValue(selectedProduct.getCategory());
            quantityField.setText(String.valueOf(selectedProduct.getQuantity()));
            priceField.setText(String.valueOf(selectedProduct.getPrice()));
            datePicker.setValue(selectedProduct.getDate());
        }
    }

    @FXML
    private void handleAddProduct() {
        try {
            if (!validateFields()) return;

            Product product = new Product(
                    designationField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(quantityField.getText()),
                    categorieChoiceBox.getValue(),
                    datePicker.getValue()
            );

            if (productService.addProduct(product)) {
                refreshProductTable();
                clearFields();
                showAlert("Produit ajouté avec succès", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer des valeurs numériques valides");
        } catch (Exception e) {
            showAlert("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateProduct() {
        if (selectedProduct == null) {
            showAlert("Aucun produit sélectionné");
            return;
        }

        try {
            if (!validateFields()) return;

            selectedProduct.setName(designationField.getText());
            selectedProduct.setCategory(categorieChoiceBox.getValue());
            selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
            selectedProduct.setDate(datePicker.getValue());

            if (productService.updateProduct(selectedProduct)) {
                refreshProductTable();
                clearFields();
                showAlert("Produit mis à jour", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Erreur de mise à jour");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteProduct() {
        if (selectedProduct == null) {
            showAlert("Aucun produit sélectionné");
            return;
        }

        // Boîte de dialogue pour le motif
        TextInputDialog motifDialog = new TextInputDialog();
        motifDialog.setTitle("Suppression du produit");
        motifDialog.setHeaderText("Motif de la suppression");
        motifDialog.setContentText("Veuillez entrer le motif :");

        motifDialog.showAndWait().ifPresent(motif -> {
            if (motif.trim().isEmpty()) {
                showAlert("Le motif est obligatoire");
                return;
            }

            try {
                // Enregistrer l’historique
                boolean historiqueOk = productService.enregistrerSuppressionHistorique(
                        selectedProduct.getId(),
                        selectedProduct.getName(),
                        motif,
                        LocalDate.now()
                );

                // Supprimer le produit
                if (historiqueOk && productService.deleteProduct(selectedProduct.getId())) {
                    refreshProductTable();
                    clearFields();
                    showAlert("Produit supprimé et motif enregistré", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur lors de la suppression ou de l’enregistrement");
                }
            } catch (Exception e) {
                showAlert("Erreur lors de la suppression");
                e.printStackTrace();
            }
        });
    }


    private boolean validateFields() {
        if (designationField.getText().isEmpty() ||
                priceField.getText().isEmpty() ||
                quantityField.getText().isEmpty() ||
                categorieChoiceBox.getValue() == null ||
                datePicker.getValue() == null) {
            showAlert("Veuillez remplir tous les champs");
            return false;
        }
        return true;
    }

    private void refreshProductTable() {
        try {
            productsTable.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        } catch (Exception e) {
            showAlert("Erreur lors du chargement des produits");
            e.printStackTrace();
        }
    }

    private void filterProducts() {
        String keyword = filterField.getText().toLowerCase();
        String filter = filterChoiceBox.getValue();

        if (keyword.isEmpty() && ("Tous".equals(filter) || filter == null)) {
            refreshProductTable();
            return;
        }

        List<Product> filtered = productService.getAllProducts().stream()
                .filter(p -> {
                    boolean matchesKeyword = keyword.isEmpty() ||
                            p.getName().toLowerCase().contains(keyword) ||
                            p.getCategory().toLowerCase().contains(keyword);
                    boolean matchesCategory = "Tous".equals(filter) || p.getCategory().equals(filter);
                    return matchesKeyword && matchesCategory;
                })
                .toList();

        productsTable.setItems(FXCollections.observableArrayList(filtered)); // <-- Ajoute ceci
    }


    private void clearFields() {
        designationField.clear();
        categorieChoiceBox.getSelectionModel().clearSelection();
        quantityField.clear();
        priceField.clear();
        datePicker.setValue(null);
        selectedProduct = null;
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        showAlert(message, Alert.AlertType.ERROR);
    }
}