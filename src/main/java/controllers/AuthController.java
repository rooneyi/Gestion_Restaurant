// controllers/AuthController.java
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.AuthService;

public class AuthController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private AuthService authService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthService();
    }

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.authenticate(username, password)) {
            User user = authService.getUser(username);
            redirectToDashboard(user);
        } else {
            showAlert("Erreur d'authentification", "Identifiants incorrects");
        }
    }

    private void redirectToDashboard(User user) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root;

        if (user.isAdmin()) {
            root = FXMLLoader.load(getClass().getResource("/views/admin_dashboard.fxml"));
        } else {
            root = FXMLLoader.load(getClass().getResource("/views/user_dashboard.fxml"));
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}