package models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class StockMovement {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final StringProperty movementType;
    private final IntegerProperty quantity;
    private final StringProperty reason;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty userAction;

    public StockMovement() {
        this(0, 0, "", "", 0, "", LocalDate.now(), "");
    }

    public StockMovement(int id, int productId, String productName, String movementType, 
                        int quantity, String reason, LocalDate date, String userAction) {
        this.id = new SimpleIntegerProperty(id);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.movementType = new SimpleStringProperty(movementType);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.reason = new SimpleStringProperty(reason);
        this.date = new SimpleObjectProperty<>(date);
        this.userAction = new SimpleStringProperty(userAction);
    }

    public StockMovement(int productId, String productName, String movementType, 
                        int quantity, String reason, LocalDate date, String userAction) {
        this(0, productId, productName, movementType, quantity, reason, date, userAction);
    }

    // Properties
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty productIdProperty() { return productId; }
    public StringProperty productNameProperty() { return productName; }
    public StringProperty movementTypeProperty() { return movementType; }
    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty reasonProperty() { return reason; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty userActionProperty() { return userAction; }

    // Getters
    public int getId() { return id.get(); }
    public int getProductId() { return productId.get(); }
    public String getProductName() { return productName.get(); }
    public String getMovementType() { return movementType.get(); }
    public int getQuantity() { return quantity.get(); }
    public String getReason() { return reason.get(); }
    public LocalDate getDate() { return date.get(); }
    public String getUserAction() { return userAction.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setProductId(int productId) { this.productId.set(productId); }
    public void setProductName(String productName) { this.productName.set(productName); }
    public void setMovementType(String movementType) { this.movementType.set(movementType); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public void setReason(String reason) { this.reason.set(reason); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setUserAction(String userAction) { this.userAction.set(userAction); }
}