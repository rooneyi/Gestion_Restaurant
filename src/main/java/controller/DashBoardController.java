package controller;

/*
<AnchorPane prefHeight="660.0" prefWidth="1150.0" stylesheets="@styleDashBoard.css"
            xmlns="http://javafx.com/javafx/23.0.1"
            xmlns:fx="http://javafx.com/fxml/1" fx:controller="controllers.DashBoardController">
 */



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

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;
    @FXML
    private StackPane contentArea;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = Session.getInstance().getUser();
        if (user != null) {
            System.out.println("Bienvenue " + user.getUsername());
            // tu peux maintenant afficher des infos utilisateur dans l'UI
        }

        // fermer l'application'
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        /*
        // Le slider
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });


         */
        /*
        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
        // fermer le slider

         */



        // debut de StackPane dans le try et catch
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

    // La methode qui permet de changer de vue dans le try et catch
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
    @FXML
    private void handleLogout() throws IOException {
        // 🔐 Vider la session
        Session.getInstance().clear();

        // 🔄 Rediriger vers la page de connexion
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/auth.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
