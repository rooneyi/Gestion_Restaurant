package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/auth.fxml"));
        Parent root = fxmlLoader.load();

        stage.setTitle("Restaurant Application");
        stage.setScene(new Scene(root));
        stage.setMaximized(true); // ✅ plein écran adapté automatiquement

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}