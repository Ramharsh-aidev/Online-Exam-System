package com.example.onlineexamsystem.service;

import com.example.onlineexamsystem.model.Exam;
import com.example.onlineexamsystem.user.AdminUser;
import com.example.onlineexamsystem.user.StudentUser;
import com.example.onlineexamsystem.user.User; // Correct import for User class
import java.util.ArrayList;
import java.util.List;

// ... (rest of ExamManagementSystem.java code - no changes needed in code logic)

/**
 * Represents the core exam management system.
 * Manages users, exams, and overall system operations.
 */
public class ExamManagementSystem {
    private final List<User> users = new ArrayList<>();
    private final List<Exam> exams = new ArrayList<>();

    /**
     * Creates a new Admin user.
     *
     * @param username Admin username.
     * @param password Admin password.
     * @return The created AdminUser.
     */
    public AdminUser createAdmin(String username, String password) {
        AdminUser admin = new AdminUser(username, password);
        users.add(admin);
        System.out.println("Admin User created: " + username);
        return admin;
    }

    /**
     * Creates a new Student user.
     *
     * @param username Student username.
     * @param password Student password.
     * @return The created StudentUser.
     */
    public StudentUser createStudent(String username, String password) {
        StudentUser student = new StudentUser(username, password);
        users.add(student);
        System.out.println("Student User created: " + username);
        return student;
    }

    /**
     * Adds an exam to the system's list of exams.
     *
     * @param exam The exam to be added.
     */
    public void addExam(Exam exam) {
        exams.add(exam);
    }

    /**
     * Gets a list of all exams in the system.
     *
     * @return List of all exams.
     */
    public List<Exam> getAllExams() {
        return new ArrayList<>(exams); // Return a copy to prevent external modification
    }
}