// controllers/AdminController.java
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import services.AuthService;

public class AdminController implements Initializable {

    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;

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
    private AuthService authService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthService();
    }

    @FXML
    private void handleCreateUser() {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        boolean success = authService.createUser(username, password, false);

        if (success) {
            showAlert("Succès", "Utilisateur créé avec succès");
            newUsernameField.clear();
            newPasswordField.clear();
        } else {
            showAlert("Erreur", "Nom d'utilisateur déjà utilisé");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}