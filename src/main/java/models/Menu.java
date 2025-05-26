package models;

public class Menu {
    private String name;
    private double price;
    private String category;

    // Constructeur avec arguments
    public Menu(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Constructeur sans argument (si besoin pour FXML/ORM)
    public Menu() { }

    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
