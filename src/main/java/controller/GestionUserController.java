package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import models.User;
import services.AuthService;

import java.util.List;

public class GestionUserController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableColumn<User, String> passwordCol;
    @FXML private TableColumn<User, Boolean> isAdminCol;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox isAdminCheckbox;
    @FXML private TextField searchField;

    private final AuthService authService = new AuthService();
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        isAdminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));

        loadUsers();

        userTable.setOnMouseClicked(this::onTableClick);
    }

    private void loadUsers() {
        List<User> users = authService.getAllUsers();
        userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList);
    }

    @FXML
    public void addUser() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        boolean isAdmin = isAdminCheckbox.isSelected();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.");
            return;
        }

        boolean success = authService.createUser(username, password, isAdmin);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Utilisateur ajouté avec succès.");
            clearForm();
            loadUsers();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout de l'utilisateur.");
        }
    }

    @FXML
    public void updateUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un utilisateur.");
            return;
        }

        String newUsername = usernameField.getText().trim();
        String newPassword = passwordField.getText().trim(); // vide = pas de modification
        boolean isAdmin = isAdminCheckbox.isSelected();

        boolean success = authService.updateUser(
                selected.getUsername(), newUsername, newPassword, isAdmin
        );

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Utilisateur mis à jour.");
            clearForm();
            loadUsers();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour.");
        }
    }

    @FXML
    public void deleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sélectionnez un utilisateur à supprimer.");
            return;
        }

        boolean success = authService.deleteUser(selected.getUsername());
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Utilisateur supprimé.");
            clearForm();
            loadUsers();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression.");
        }
    }

    @FXML
    public void filterUsers(KeyEvent event) {
        String keyword = searchField.getText().toLowerCase();
        ObservableList<User> filtered = FXCollections.observableArrayList();

        for (User user : userList) {
            if (user.getUsername().toLowerCase().contains(keyword)) {
                filtered.add(user);
            }
        }

        userTable.setItems(filtered);
    }

    private void onTableClick(MouseEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            usernameField.setText(selected.getUsername());
            passwordField.setText(""); // Ne pas afficher mot de passe
            isAdminCheckbox.setSelected(selected.isAdmin());
        }
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        isAdminCheckbox.setSelected(false);
        userTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
