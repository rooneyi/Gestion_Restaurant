package utils;// utils/Session.java

import models.User;

public class Session {
    private static Session instance;
    private User user;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void clear() {
        user = null;
    }
}
