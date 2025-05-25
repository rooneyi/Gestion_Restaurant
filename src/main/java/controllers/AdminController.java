// controllers/AdminController.java
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.AuthService;

public class AdminController implements Initializable {

    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;

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