package models;

import javafx.beans.property.*;

public class FactureItem {
    private final IntegerProperty id;
    private final IntegerProperty factureId;
    private final StringProperty productName;
    private final IntegerProperty quantity;
    private final DoubleProperty unitPrice;
    private final DoubleProperty totalPrice;

    public FactureItem() {
        this(0, 0, "", 0, 0.0);
    }

    public FactureItem(int id, int factureId, String productName, int quantity, double unitPrice) {
        this.id = new SimpleIntegerProperty(id);
        this.factureId = new SimpleIntegerProperty(factureId);
        this.productName = new SimpleStringProperty(productName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.totalPrice = new SimpleDoubleProperty(quantity * unitPrice);
    }

    public FactureItem(int factureId, String productName, int quantity, double unitPrice) {
        this(0, factureId, productName, quantity, unitPrice);
    }

    // Properties
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty factureIdProperty() { return factureId; }
    public StringProperty productNameProperty() { return productName; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty unitPriceProperty() { return unitPrice; }
    public DoubleProperty totalPriceProperty() { return totalPrice; }

    // Getters
    public int getId() { return id.get(); }
    public int getFactureId() { return factureId.get(); }
    public String getProductName() { return productName.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getUnitPrice() { return unitPrice.get(); }
    public double getTotalPrice() { return totalPrice.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setFactureId(int factureId) { this.factureId.set(factureId); }
    public void setProductName(String productName) { this.productName.set(productName); }
    public void setQuantity(int quantity) { 
        this.quantity.set(quantity);
        updateTotalPrice();
    }
    public void setUnitPrice(double unitPrice) { 
        this.unitPrice.set(unitPrice);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        this.totalPrice.set(this.quantity.get() * this.unitPrice.get());
    }
}