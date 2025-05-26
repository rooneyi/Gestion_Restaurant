package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Menu;
import services.MenuService;

// controllers/AdminMenuController.java
public class AdminMenuController {
    @FXML private TableView<Menu> menuTable;
    @FXML
    private TextField nameField, priceField, categoryField;

    private MenuService menuService;

    @FXML
    private void handleAddMenuItem() {
        Menu item = new Menu(
                nameField.getText(),
                Double.parseDouble(priceField.getText()),
                categoryField.getText()
        );
        menuService.addMenuItem(item);
        refreshMenuTable();
    }

    private void refreshMenuTable() {
        menuTable.getItems().setAll(menuService.getMenuByCategory(categoryField.getText()));
    }

}