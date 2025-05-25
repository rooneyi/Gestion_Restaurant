// services/AuthService.java
package services;

import models.User;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users;

    public AuthService() {
        users = new ArrayList<>();
        // Ajouter l'admin par défaut
        users.add(new User("admin", "admin123", true));
    }

    public boolean authenticate(String username, String password) {
        return users.stream()
                .anyMatch(u -> u.getUsername().equals(username)
                        && u.getPassword().equals(password));
    }

    public User getUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean createUser(String username, String password, boolean isAdmin) {
        if (getUser(username) != null) {
            return false; // L'utilisateur existe déjà
        }
        users.add(new User(username, password, isAdmin));
        return true;
    }
}