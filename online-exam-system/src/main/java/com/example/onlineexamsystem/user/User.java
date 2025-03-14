package com.example.onlineexamsystem.user;

import com.example.onlineexamsystem.service.ExamManagementSystem; // Import if needed in abstract class (in this case, it's not directly used, but good practice if subclasses might use it)

import java.util.UUID;

// ... (rest of User.java code - no changes needed in code logic)

/**
 * Abstract class representing a User in the system (Admin or Student).
 */
public abstract class User {
    private final String userId;
    private final String username;
    private final String password;

    /**
     * Constructor for User.
     *
     * @param username User's username.
     * @param password User's password.
     */
    public User(String username, String password) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Checks if the provided password matches the user's password.
     *
     * @param password Password to check.
     * @return True if password matches, false otherwise.
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Abstract method for user login (to be implemented by subclasses).
     *
     * @param system The ExamManagementSystem.
     * @return True if login is successful, false otherwise.
     */
    public abstract boolean login(ExamManagementSystem system);
}