// controllers/AuthController.java
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.AuthService;

public class AuthController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthService();
        roleChoiceBox.getItems().addAll("Admin", "Utilisateur");
        roleChoiceBox.setValue("Utilisateur"); // Valeur par défaut
    }

    private AuthService authService;


    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleChoiceBox.getValue();

        if (authService.authenticate(username, password)) {
            User user = authService.getUser(username);

            if ((user.isAdmin() && "admin".equalsIgnoreCase(selectedRole)) ||
                    (!user.isAdmin() && "user".equalsIgnoreCase(selectedRole))) {

                utils.Session.getInstance().setUser(user);
                redirectToDashboard(user);
            } else {
                showAlert("Erreur", "Le rôle sélectionné ne correspond pas à l'utilisateur.");
            }

        } else {
            showAlert("Erreur d'authentification", "Identifiants incorrects");
        }
    }


    private void redirectToDashboard(User user) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root;

        if (user.isAdmin()) {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/dashboard.fxml")));
        } else {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/user_dashboard.fxml")));
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