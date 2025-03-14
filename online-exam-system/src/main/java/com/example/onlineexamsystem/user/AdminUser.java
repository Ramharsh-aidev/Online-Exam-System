package com.example.onlineexamsystem.user;

import com.example.onlineexamsystem.model.Exam;
import com.example.onlineexamsystem.model.ExamResult;
import com.example.onlineexamsystem.model.QuestionPool;
import com.example.onlineexamsystem.service.ExamManagementSystem;
import com.example.onlineexamsystem.service.ExamSession;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents an Admin user, capable of creating and managing exams.
 */
public class AdminUser extends User {
    private final List<QuestionPool> questionPools = new ArrayList<>();

    /**
     * Constructor for AdminUser.
     *
     * @param username Admin username.
     * @param password Admin password.
     */
    public AdminUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login(ExamManagementSystem system) {
        // Admin login logic (can be extended)
        System.out.println("Admin " + getUsername() + " logged in.");
        return true;
    }

    /**
     * Creates a new Question Pool.
     *
     * @param poolName Name of the question pool.
     * @return The created QuestionPool.
     */
    public QuestionPool createQuestionPool(String poolName) {
        QuestionPool questionPool = new QuestionPool(poolName, this);
        questionPools.add(questionPool);
        System.out.println("Question Pool created: " + poolName);
        return questionPool;
    }

    /**
     * Creates a new Exam.
     *
     * @param examName      Name of the exam.
     * @param duration      Duration of the exam.
     * @param questionPool  QuestionPool to use for the exam (can be null if questions are added manually).
     * @return The created Exam.
     */
    public Exam createExam(String examName, Duration duration, QuestionPool questionPool) {
        Exam exam = new Exam(examName, duration, this, questionPool);
        getExamManagementSystem().addExam(exam); // Assuming AdminUser has access to ExamManagementSystem
        System.out.println("Exam created: " + examName);
        return exam;
    }

    /**
     * Publishes an exam, making it available for students to take.
     *
     * @param exam The exam to publish.
     */
    public void publishExam(Exam exam) {
        exam.publish();
        System.out.println("Exam published: " + exam.getExamName());
    }

    /**
     * Views results for a specific exam.
     *
     * @param exam The exam to view results for.
     * @return List of ExamResult objects for the exam.
     */
    public List<ExamResult> viewExamResults(Exam exam) {
        System.out.println("Viewing results for exam: " + exam.getExamName());
        return exam.getExamSessions().stream()
                .map(ExamSession::getExamResult)
                .filter(Objects::nonNull) // Ensure result is generated (exam submitted)
                .collect(Collectors.toList());
    }

    /**
     * Generates a summary report for an exam.
     *
     * @param exam The exam to generate a summary for.
     */
    public void generateExamSummary(Exam exam) {
        System.out.println("\n--- Exam Summary for: " + exam.getExamName() + " ---");
        List<ExamResult> results = viewExamResults(exam);
        if (results.isEmpty()) {
            System.out.println("No students have taken this exam yet.");
            return;
        }

        double averageScore = results.stream()
                .mapToInt(ExamResult::getScore)
                .average()
                .orElse(0.0);
        int highestScore = results.stream()
                .mapToInt(ExamResult::getScore)
                .max()
                .orElse(0);
        int lowestScore = results.stream()
                .mapToInt(ExamResult::getScore)
                .min()
                .orElse(0);

        System.out.println("Total Students Taken Exam: " + results.size());
        System.out.println("Average Score: " + String.format("%.2f", averageScore));
        System.out.println("Highest Score: " + highestScore);
        System.out.println("Lowest Score: " + lowestScore);
        System.out.println("------------------------------------");
    }

    // Placeholder to access ExamManagementSystem (in a real app, dependency injection or similar would be used)
    private ExamManagementSystem getExamManagementSystem() {
        return new ExamManagementSystem(); // In a real application, this would be properly injected.
    }
}