module org.example.gestion_restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    // Exportez le package contenant vos contrôleurs
    exports controllers to javafx.fxml;
    exports org.example.gestion_restaurant;

    // Ouvrez le package contenant vos fichiers FXML si nécessaire
    opens controllers to javafx.fxml;
    opens org.example.gestion_restaurant to javafx.fxml;
}