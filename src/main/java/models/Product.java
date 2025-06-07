package models;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;

public class Product {

    private final StringProperty category;
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty quantity; // ✅ Ajout de la propriété quantité

    public Product(int id, String name, double price, int quantity, String category, LocalDate date) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleObjectProperty<>(date);
    }

    public Product(String name, double price, int quantity, String category, LocalDate date) {
        this(0, name, price, quantity, category, date);
    }

    public StringProperty categoryProperty() { return category; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }

    public LocalDate getDate() {
        return date.get();
    }
    public void setDate(LocalDate date) { this.date.set(date); }


    // Propriétés JavaFX pour TableView
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty quantityProperty() { return quantity; }

    // Getters/Setters classiques
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
}
