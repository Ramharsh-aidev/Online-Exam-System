package com.example.onlineexamsystem;

import com.example.onlineexamsystem.model.*; // Import all model classes
import com.example.onlineexamsystem.service.ExamManagementSystem;
import com.example.onlineexamsystem.service.ExamSession;
import com.example.onlineexamsystem.user.AdminUser;
import com.example.onlineexamsystem.user.StudentUser;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ... (rest of the OnlineExamSystem.java code - no changes needed in code logic)

/**
 * Main class to demonstrate the Online Exam System.
 */
public class OnlineExamSystem {

    public static void main(String[] args) {
        // Initialize the system
        ExamManagementSystem examSystem = new ExamManagementSystem();

        // Create Admin User
        AdminUser admin = examSystem.createAdmin("adminUser", "adminPass");

        // Create Student Users
        StudentUser student1 = examSystem.createStudent("student1", "student1Pass");
        StudentUser student2 = examSystem.createStudent("student2", "student2Pass");

        // Create Question Pool
        QuestionPool questionPool = admin.createQuestionPool("General Knowledge Pool");

        // Add Questions to the Pool
        questionPool.addQuestion(new ObjectiveQuestion(
                "GK_Q1",
                "What is the capital of France?",
                1,
                Arrays.asList("London", "Paris", "Berlin", "Rome"),
                "Paris")
        );
        questionPool.addQuestion(new ObjectiveQuestion(
                "GK_Q2",
                "Which planet is known as the 'Red Planet'?",
                1,
                Arrays.asList("Earth", "Mars", "Jupiter", "Venus"),
                "Mars")
        );
        questionPool.addQuestion(new EssayQuestion(
                "GK_E1",
                "Explain the theory of relativity in brief.",
                5)
        );
        questionPool.addQuestion(new ObjectiveQuestion(
                "GK_Q3",
                "What is the largest mammal?",
                2,
                Arrays.asList("Elephant", "Blue Whale", "Giraffe", "Lion"),
                "Blue Whale")
        );
        questionPool.addQuestion(new EssayQuestion(
                "GK_E2",
                "Discuss the impact of artificial intelligence on society.",
                8)
        );

        // Create an Exam
        Exam exam = admin.createExam("General Knowledge Test", Duration.ofMinutes(30), questionPool);
        exam.addQuestion(questionPool.getQuestionById("GK_Q1")); // Adding specific question as well
        exam.setRandomQuestionCount(3); // Setting random question count in addition to specific ones

        // Admin publishes the exam
        admin.publishExam(exam);

        // Student 1 takes the exam
        ExamSession session1 = student1.startExam(exam);
        if (session1 != null) {
            System.out.println("\nStudent 1 started exam: " + exam.getExamName());

            // Simulate Student answering questions
            Question q1 = session1.getCurrentQuestion();
            if (q1 instanceof ObjectiveQuestion) {
                session1.submitAnswer(q1, "Paris");
            }

            session1.moveToNextQuestion();
            Question q2 = session1.getCurrentQuestion();
            if (q2 instanceof ObjectiveQuestion) {
                session1.submitAnswer(q2, "Mars");
            }

            session1.moveToNextQuestion();
            Question q3 = session1.getCurrentQuestion();
            if (q3 instanceof EssayQuestion) {
                session1.submitAnswer(q3, "This is a brief explanation of relativity...");
            }

            // Simulate time passing (or student submitting early)
            try {
                TimeUnit.SECONDS.sleep(10); // Simulate student taking some time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ExamResult result1 = student1.submitExam(session1);
            System.out.println("Student 1 submitted exam. Score: " + result1.getScore() + "/" + result1.getTotalMarks());
        }

        // Student 2 takes the exam
        ExamSession session2 = student2.startExam(exam);
        if (session2 != null) {
            System.out.println("\nStudent 2 started exam: " + exam.getExamName());
            // Simulate Student answering questions differently
            Question q1 = session2.getCurrentQuestion();
            if (q1 instanceof ObjectiveQuestion) {
                session2.submitAnswer(q1, "London"); // Wrong answer
            }
            session2.moveToNextQuestion();
            Question q2 = session2.getCurrentQuestion();
            if (q2 instanceof ObjectiveQuestion) {
                session2.submitAnswer(q2, "Mars"); // Correct answer
            }
            session2.moveToNextQuestion();
            Question q3 = session2.getCurrentQuestion();
            if (q3 instanceof EssayQuestion) {
                session2.submitAnswer(q3, "Another essay answer...");
            }

            try {
                TimeUnit.SECONDS.sleep(5); // Simulate student taking less time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ExamResult result2 = student2.submitExam(session2);
            System.out.println("Student 2 submitted exam. Score: " + result2.getScore() + "/" + result2.getTotalMarks());
        }

        // Admin views results and generates summary
        System.out.println("\nAdmin viewing exam results:");
        List<ExamResult> examResults = admin.viewExamResults(exam);
        examResults.forEach(result -> {
            System.out.println("Student: " + result.getExamSession().getStudent().getUsername() +
                               ", Score: " + result.getScore() + "/" + result.getTotalMarks());
            if (result.getExamSession().getStudentAnswers() != null) {
                result.getExamSession().getStudentAnswers().forEach((question, answer) -> {
                    System.out.println("  Q: " + question.getQuestionText().substring(0, Math.min(question.getQuestionText().length(), 20)) + "..., Answer: " + answer);
                });
            }
        });

        admin.generateExamSummary(exam);
    }
}