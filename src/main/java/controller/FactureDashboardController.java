package controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Facture;
import models.FactureItem;
import models.Product;
import services.FactureService;
import services.ProductService;
import utils.Session;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class FactureDashboardController implements Initializable {

    @FXML private TextField clientNameField;
    @FXML private TextField productNumberField;
    @FXML private ChoiceBox<String> productChoiceBox;
    @FXML private TextField quantityField;
    @FXML private TextField unitPriceField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<Integer> tableChoiceBox;
    @FXML private TextField totalGeneralField;

    @FXML private TableView<FactureItem> factureItemsTable;
    @FXML private TableColumn<FactureItem, Integer> colNumber;
    @FXML private TableColumn<FactureItem, String> colDesignation;
    @FXML private TableColumn<FactureItem, Integer> colQuantity;
    @FXML private TableColumn<FactureItem, Double> colUnitPrice;
    @FXML private TableColumn<FactureItem, Double> colTotalPrice;

    @FXML private JFXButton addButton;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton modifyButton;
    @FXML private JFXButton printButton;

    private ProductService productService;
    private FactureService factureService;
    private ObservableList<FactureItem> factureItems;
    private Facture currentFacture;
    private FactureItem selectedItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            productService = new ProductService();
            factureService = new FactureService();
            factureItems = FXCollections.observableArrayList();
            currentFacture = new Facture();

            setupUI();
            setupTable();
            loadProducts();
            
        } catch (SQLException e) {
            showAlert("Erreur de connexion à la base de données", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void setupUI() {
        // Configuration du DatePicker
        datePicker.setValue(LocalDate.now());

        // Configuration des tables (1-20)
        for (int i = 1; i <= 20; i++) {
            tableChoiceBox.getItems().add(i);
        }
        tableChoiceBox.setValue(1);

        // Configuration du total
        totalGeneralField.setEditable(false);
        totalGeneralField.setText("0");

        // Listeners
        productChoiceBox.setOnAction(e -> updateProductInfo());
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> calculateItemTotal());
        unitPriceField.textProperty().addListener((obs, oldVal, newVal) -> calculateItemTotal());
    }

    private void setupTable() {
        colNumber.setCellValueFactory(data -> {
            int index = factureItemsTable.getItems().indexOf(data.getValue()) + 1;
            return new javafx.beans.property.SimpleIntegerProperty(index).asObject();
        });
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        factureItemsTable.setItems(factureItems);
        factureItemsTable.setOnMouseClicked(this::onTableClick);
    }

    private void loadProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            productChoiceBox.getItems().clear();
            
            for (Product product : products) {
                if (product.getQuantity() > 0) { // Seulement les produits en stock
                    productChoiceBox.getItems().add(product.getName());
                }
            }
        } catch (Exception e) {
            showAlert("Erreur lors du chargement des produits", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void updateProductInfo() {
        String selectedProductName = productChoiceBox.getValue();
        if (selectedProductName != null) {
            try {
                List<Product> products = productService.getAllProducts();
                Product selectedProduct = products.stream()
                    .filter(p -> p.getName().equals(selectedProductName))
                    .findFirst()
                    .orElse(null);
                
                if (selectedProduct != null) {
                    productNumberField.setText(String.valueOf(selectedProduct.getId()));
                    unitPriceField.setText(String.valueOf(selectedProduct.getPrice()));
                }
            } catch (Exception e) {
                showAlert("Erreur lors de la récupération des informations produit", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void calculateItemTotal() {
        try {
            if (!quantityField.getText().isEmpty() && !unitPriceField.getText().isEmpty()) {
                int quantity = Integer.parseInt(quantityField.getText());
                double unitPrice = Double.parseDouble(unitPriceField.getText());
                // Le total de l'item sera calculé automatiquement dans FactureItem
            }
        } catch (NumberFormatException e) {
            // Ignore les erreurs de format pendant la saisie
        }
    }

    @FXML
    private void handleAddItem() {
        try {
            if (!validateItemFields()) return;

            String productName = productChoiceBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText());
            double unitPrice = Double.parseDouble(unitPriceField.getText());

            // Vérifier le stock disponible
            if (!checkStockAvailability(productName, quantity)) {
                showAlert("Stock insuffisant pour ce produit", Alert.AlertType.WARNING);
                return;
            }

            FactureItem item = new FactureItem(0, productName, quantity, unitPrice);
            factureItems.add(item);
            currentFacture.addItem(item);

            updateTotalGeneral();
            clearItemFields();
            showAlert("Article ajouté avec succès", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer des valeurs numériques valides", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur lors de l'ajout de l'article", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteItem() {
        if (selectedItem == null) {
            showAlert("Veuillez sélectionner un article à supprimer", Alert.AlertType.WARNING);
            return;
        }

        factureItems.remove(selectedItem);
        currentFacture.removeItem(selectedItem);
        updateTotalGeneral();
        clearItemFields();
        selectedItem = null;
        showAlert("Article supprimé", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleModifyItem() {
        if (selectedItem == null) {
            showAlert("Veuillez sélectionner un article à modifier", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (!validateItemFields()) return;

            String productName = productChoiceBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText());
            double unitPrice = Double.parseDouble(unitPriceField.getText());

            selectedItem.setProductName(productName);
            selectedItem.setQuantity(quantity);
            selectedItem.setUnitPrice(unitPrice);

            factureItemsTable.refresh();
            updateTotalGeneral();
            clearItemFields();
            selectedItem = null;
            showAlert("Article modifié avec succès", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer des valeurs numériques valides", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur lors de la modification", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveFacture() {
        try {
            if (!validateFactureFields()) return;

            currentFacture.setClientName(clientNameField.getText());
            currentFacture.setTableNumber(tableChoiceBox.getValue());
            currentFacture.setDate(datePicker.getValue());
            currentFacture.setStatus("EN_COURS");
            currentFacture.setIsPaid(false);

            boolean success = factureService.createFacture(currentFacture);

            if (success) {
                showAlert("Facture créée avec succès", Alert.AlertType.INFORMATION);
                clearAllFields();
                currentFacture = new Facture();
            } else {
                showAlert("Erreur lors de la création de la facture", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("Erreur lors de la sauvegarde", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrintFacture() {
        if (factureItems.isEmpty()) {
            showAlert("Aucun article à imprimer", Alert.AlertType.WARNING);
            return;
        }

        // TODO: Implémenter l'impression de la facture
        showAlert("Fonctionnalité d'impression en cours de développement", Alert.AlertType.INFORMATION);
    }

    private void onTableClick(MouseEvent event) {
        selectedItem = factureItemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            productChoiceBox.setValue(selectedItem.getProductName());
            quantityField.setText(String.valueOf(selectedItem.getQuantity()));
            unitPriceField.setText(String.valueOf(selectedItem.getUnitPrice()));
        }
    }

    private boolean validateItemFields() {
        if (productChoiceBox.getValue() == null || productChoiceBox.getValue().isEmpty()) {
            showAlert("Veuillez sélectionner un produit", Alert.AlertType.WARNING);
            return false;
        }
        if (quantityField.getText().isEmpty()) {
            showAlert("Veuillez entrer une quantité", Alert.AlertType.WARNING);
            return false;
        }
        if (unitPriceField.getText().isEmpty()) {
            showAlert("Veuillez entrer un prix unitaire", Alert.AlertType.WARNING);
            return false;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(unitPriceField.getText());
            if (quantity <= 0 || price <= 0) {
                showAlert("La quantité et le prix doivent être positifs", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer des valeurs numériques valides", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateFactureFields() {
        if (clientNameField.getText().isEmpty()) {
            showAlert("Veuillez entrer le nom du client", Alert.AlertType.WARNING);
            return false;
        }
        if (factureItems.isEmpty()) {
            showAlert("Veuillez ajouter au moins un article", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private boolean checkStockAvailability(String productName, int requestedQuantity) {
        try {
            List<Product> products = productService.getAllProducts();
            Product product = products.stream()
                .filter(p -> p.getName().equals(productName))
                .findFirst()
                .orElse(null);
            
            return product != null && product.getQuantity() >= requestedQuantity;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateTotalGeneral() {
        double total = factureItems.stream()
            .mapToDouble(FactureItem::getTotalPrice)
            .sum();
        totalGeneralField.setText(String.format("%.0f", total));
        currentFacture.setTotalAmount(total);
    }

    private void clearItemFields() {
        productChoiceBox.getSelectionModel().clearSelection();
        productNumberField.clear();
        quantityField.clear();
        unitPriceField.clear();
    }

    private void clearAllFields() {
        clientNameField.clear();
        tableChoiceBox.setValue(1);
        datePicker.setValue(LocalDate.now());
        factureItems.clear();
        totalGeneralField.setText("0");
        clearItemFields();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}