package models;

import java.time.LocalDate;

public class User {
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;
    private LocalDate createdAt;

    public User(int id, String username, String password, boolean isAdmin, LocalDate createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
    }

    public User(String username, String password, boolean isAdmin, LocalDate createdAt) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }
    public LocalDate getCreatedAt() { return createdAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
