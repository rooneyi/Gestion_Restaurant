package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.User;
import utils.Session;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashBoardController implements Initializable {

    @FXML private Button logoutButton;
    @FXML private ImageView Exit;
    @FXML private Label Menu;
    @FXML private Label MenuClose;
    @FXML private AnchorPane slider;
    @FXML private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = Session.getInstance().getUser();
        if (user != null) {
            System.out.println("Bienvenue " + user.getUsername());
        }

        // Fermer l'application
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Charger la vue d'accueil par défaut
        try {
           Parent fxml = FXMLLoader.load(getClass().getResource("/views/homeDashboard.fxml"));
           contentArea.getChildren().setAll(fxml);
        } catch (IOException ex) {
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void produits(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/productDashboard.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void home(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/homeDashboard.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void facturations (javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/factureDashboard.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void gererUtilisateurs(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/userCrud.fxml"));
        contentArea.getChildren().setAll(root);
    }

    public void gestion (javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/gestionDashboard.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void utilisateurs (javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/gestion_users.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void historiques (javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/historiques.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void mouvementsStock(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/stockMovement.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    @FXML
    private void handleLogout() throws IOException {
        // Vider la session
        Session.getInstance().clear();

        // Rediriger vers la page de connexion
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/auth.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }
}