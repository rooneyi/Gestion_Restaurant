package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;
import services.AuthService;

public class HomeDashboardController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableColumn<User, String> roleCol;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;

    private final AuthService authService = new AuthService();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private User selectedUser;

    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isAdmin() ? "Admin" : "Utilisateur"));

        roleChoiceBox.getItems().addAll("Admin", "Utilisateur");
        roleChoiceBox.setValue("Utilisateur");
        userTable.setItems(users);
        roleChoiceBox.setValue("Utilisateur");

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedUser = newSel;
            if (newSel != null) {
                usernameField.setText(newSel.getUsername());
                passwordField.setText("");
                roleChoiceBox.setValue(newSel.isAdmin() ? "Admin" : "Utilisateur");
            }
        });

        refreshUsers();
    }

    private void refreshUsers() {
        users.setAll(authService.getAllUsers());
    }

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!selectedRole.equalsIgnoreCase("Admin") && !selectedRole.equalsIgnoreCase("Utilisateur")) {
            showAlert("Rôle invalide", "Le rôle doit être 'Admin' ou 'Utilisateur'.");
            return;
        }

        boolean isAdmin = selectedRole.equalsIgnoreCase("Admin");

        if (authService.createUser(username, password, isAdmin)) {
            showAlert("Succès", "Utilisateur ajouté !");
            refreshUsers();
            clearForm();
        } else {
            showAlert("Erreur", "Échec de l’ajout. Nom déjà utilisé ?");
        }
    }



    @FXML
    private void handleUpdateUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Veuillez d’abord sélectionner un utilisateur.");
            return;
        }

        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();
        String selectedRole = roleChoiceBox.getValue();

        if (newUsername.isEmpty() || selectedRole == null) {
            showAlert("Champs manquants", "Nom et rôle sont requis.");
            return;
        }

        if (!selectedRole.equalsIgnoreCase("Admin") && !selectedRole.equalsIgnoreCase("Utilisateur")) {
            showAlert("Rôle invalide", "Le rôle doit être 'Admin' ou 'Utilisateur'.");
            return;
        }

        boolean isAdmin = selectedRole.equalsIgnoreCase("Admin");

        if (authService.updateUser(selected.getUsername(), newUsername, newPassword, isAdmin)) {
            showAlert("Succès", "Utilisateur mis à jour !");
            refreshUsers();
            clearForm();
        } else {
            showAlert("Erreur", "La mise à jour a échoué.");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Veuillez d’abord sélectionner un utilisateur à supprimer.");
            return;
        }

        if (authService.deleteUser(selected.getUsername())) {
            showAlert("Supprimé", "Utilisateur supprimé !");
            refreshUsers();
            clearForm();
        } else {
            showAlert("Erreur", "La suppression a échoué.");
        }
    }


    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleChoiceBox.setValue("Utilisateur");
        selectedUser = null;
        userTable.getSelectionModel().clearSelection();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
