package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Menu;
import services.MenuService;

public class AdminMenuController {
    @FXML private TableView<Menu> menuTable;
    @FXML private TableColumn<Menu, String> nameColumn;
    @FXML private TableColumn<Menu, Double> priceColumn;
    @FXML private TableColumn<Menu, String> categoryColumn;

    @FXML private TextField nameField, priceField, categoryField;

    private final MenuService menuService = new MenuService();

    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        priceColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPrice()));
        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));
        refreshTable();
    }

    @FXML
    private void handleAdd() {
        Menu item = new Menu(nameField.getText(), Double.parseDouble(priceField.getText()), categoryField.getText());
        menuService.addMenuItem(item);
        clearFields();
        refreshTable();
    }

    @FXML
    private void handleUpdate() {
        Menu selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(nameField.getText());
            selected.setPrice(Double.parseDouble(priceField.getText()));
            selected.setCategory(categoryField.getText());
            menuService.updateMenuItem(selected);
            clearFields();
            refreshTable();
        }
    }

    @FXML
    private void handleDelete() {
        Menu selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            menuService.deleteMenuItem(selected.getId());
            refreshTable();
        }
    }

    private void refreshTable() {
        ObservableList<Menu> data = FXCollections.observableArrayList(menuService.getAllMenus());
        menuTable.setItems(data);
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        categoryField.clear();
    }
}
