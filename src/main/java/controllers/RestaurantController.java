package controllers;

import models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class RestaurantController {

    @FXML
    private CheckBox dashboardCheckBox;

    @FXML
    private CheckBox billingCheckBox;

    @FXML
    private CheckBox stockCheckBox;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        // Initialisation si nécessaire
    }

    @FXML
    private void handleDashboardButton() {
        // Action pour le bouton Tableau de bord
    }

    @FXML
    private void handleLogout() {
        // Action pour la déconnexion

    }
}