package services;

import models.Menu;

import java.util.ArrayList;
import java.util.List;

// services/MenuService.java
public class MenuService {
    private List<Menu> menuItems = new ArrayList<>();

    public void addMenuItem(Menu item) { /* ... */ }
    public void removeMenuItem(int id) { /* ... */ }
    public List<Menu> getMenuByCategory(String category) { /* ... */ return null;  }
}