package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Facture;
import models.StockMovement;
import services.FactureService;
import services.StockMovementService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HistoriquesController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TabPane historyTabPane;
    
    // Onglet Ventes du jour
    @FXML private TableView<Facture> ventesTable;
    @FXML private TableColumn<Facture, String> colClientName;
    @FXML private TableColumn<Facture, Integer> colTableNumber;
    @FXML private TableColumn<Facture, String> colDate;
    @FXML private TableColumn<Facture, Double> colMontant;
    @FXML private TableColumn<Facture, String> colStatus;

    // Onglet Mouvements de stock
    @FXML private TableView<StockMovement> stockTable;
    @FXML private TableColumn<StockMovement, String> colProductName;
    @FXML private TableColumn<StockMovement, String> colMovementType;
    @FXML private TableColumn<StockMovement, Integer> colQuantity;
    @FXML private TableColumn<StockMovement, String> colReason;
    @FXML private TableColumn<StockMovement, String> colMovementDate;
    @FXML private TableColumn<StockMovement, String> colUser;

    @FXML private DatePicker filterDatePicker;
    @FXML private ChoiceBox<String> filterTypeChoiceBox;
    @FXML private Label totalRevenueLabel;

    private FactureService factureService;
    private StockMovementService stockMovementService;
    private ObservableList<Facture> ventesData;
    private ObservableList<StockMovement> stockData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            factureService = new FactureService();
            stockMovementService = new StockMovementService();
            
            setupUI();
            setupTables();
            loadData();
            
        } catch (SQLException e) {
            showAlert("Erreur de connexion à la base de données", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void setupUI() {
        // Configuration du DatePicker
        filterDatePicker.setValue(LocalDate.now());
        filterDatePicker.setOnAction(e -> filterByDate());

        // Configuration du ChoiceBox pour filtrer les mouvements
        filterTypeChoiceBox.getItems().addAll("Tous", "ENTREE", "SORTIE");
        filterTypeChoiceBox.setValue("Tous");
        filterTypeChoiceBox.setOnAction(e -> filterStockMovements());

        // Configuration de la recherche
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData());
    }

    private void setupTables() {
        // Configuration table des ventes
        colClientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colTableNumber.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        colDate.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configuration table des mouvements de stock
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colMovementType.setCellValueFactory(new PropertyValueFactory<>("movementType"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colMovementDate.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));
        colUser.setCellValueFactory(new PropertyValueFactory<>("userAction"));

        // Style pour les mouvements
        colMovementType.setCellFactory(column -> new TableCell<StockMovement, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("ENTREE".equals(item)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("SORTIE".equals(item)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                }
            }
        });

        ventesData = FXCollections.observableArrayList();
        stockData = FXCollections.observableArrayList();
        
        ventesTable.setItems(ventesData);
        stockTable.setItems(stockData);
    }

    private void loadData() {
        loadVentesData();
        loadStockData();
        updateTotalRevenue();
    }

    private void loadVentesData() {
        try {
            List<Facture> factures = factureService.getAllFactures();
            ventesData.setAll(factures);
        } catch (Exception e) {
            showAlert("Erreur lors du chargement des ventes", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadStockData() {
        try {
            List<StockMovement> movements = stockMovementService.getAllMovements();
            stockData.setAll(movements);
        } catch (Exception e) {
            showAlert("Erreur lors du chargement des mouvements de stock", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void filterByDate() {
        LocalDate selectedDate = filterDatePicker.getValue();
        if (selectedDate != null) {
            try {
                // Filtrer les ventes par date
                List<Facture> facturesFiltered = factureService.getFacturesByDate(selectedDate);
                ventesData.setAll(facturesFiltered);

                // Filtrer les mouvements de stock par date
                List<StockMovement> movementsFiltered = stockMovementService.getMovementsByDate(selectedDate);
                stockData.setAll(movementsFiltered);

                updateTotalRevenue();
            } catch (Exception e) {
                showAlert("Erreur lors du filtrage par date", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private void filterStockMovements() {
        String selectedType = filterTypeChoiceBox.getValue();
        if (selectedType != null && !selectedType.equals("Tous")) {
            try {
                List<StockMovement> allMovements = stockMovementService.getAllMovements();
                List<StockMovement> filtered = allMovements.stream()
                    .filter(movement -> movement.getMovementType().equals(selectedType))
                    .collect(Collectors.toList());
                stockData.setAll(filtered);
            } catch (Exception e) {
                showAlert("Erreur lors du filtrage des mouvements", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        } else {
            loadStockData();
        }
    }

    private void filterData() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        // Filtrer les ventes
        try {
            List<Facture> allFactures = factureService.getAllFactures();
            List<Facture> filteredFactures = allFactures.stream()
                .filter(facture -> 
                    facture.getClientName().toLowerCase().contains(searchText) ||
                    String.valueOf(facture.getTableNumber()).contains(searchText) ||
                    facture.getStatus().toLowerCase().contains(searchText)
                )
                .collect(Collectors.toList());
            ventesData.setAll(filteredFactures);

            // Filtrer les mouvements de stock
            List<StockMovement> allMovements = stockMovementService.getAllMovements();
            List<StockMovement> filteredMovements = allMovements.stream()
                .filter(movement ->
                    movement.getProductName().toLowerCase().contains(searchText) ||
                    movement.getMovementType().toLowerCase().contains(searchText) ||
                    (movement.getReason() != null && movement.getReason().toLowerCase().contains(searchText)) ||
                    (movement.getUserAction() != null && movement.getUserAction().toLowerCase().contains(searchText))
                )
                .collect(Collectors.toList());
            stockData.setAll(filteredMovements);

        } catch (Exception e) {
            showAlert("Erreur lors de la recherche", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void updateTotalRevenue() {
        LocalDate selectedDate = filterDatePicker.getValue();
        if (selectedDate != null) {
            try {
                double totalRevenue = factureService.getTotalRevenueByDate(selectedDate);
                totalRevenueLabel.setText(String.format("Chiffre d'affaires du %s: %.0f FC", 
                    selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), totalRevenue));
            } catch (Exception e) {
                totalRevenueLabel.setText("Erreur de calcul du chiffre d'affaires");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadData();
        searchField.clear();
        filterDatePicker.setValue(LocalDate.now());
        filterTypeChoiceBox.setValue("Tous");
        showAlert("Données actualisées", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleExportVentes() {
        // TODO: Implémenter l'export des ventes en CSV/Excel
        showAlert("Fonctionnalité d'export en cours de développement", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleExportStock() {
        // TODO: Implémenter l'export des mouvements de stock en CSV/Excel
        showAlert("Fonctionnalité d'export en cours de développement", Alert.AlertType.INFORMATION);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}