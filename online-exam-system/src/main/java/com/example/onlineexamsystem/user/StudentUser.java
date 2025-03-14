package com.example.onlineexamsystem.user;

import com.example.onlineexamsystem.model.Exam;
import com.example.onlineexamsystem.model.ExamResult;
import com.example.onlineexamsystem.service.ExamManagementSystem;
import com.example.onlineexamsystem.service.ExamSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Student user, who can take exams.
 */
public class StudentUser extends User {
    private final List<ExamSession> examSessions = new ArrayList<>();

    /**
     * Constructor for StudentUser.
     *
     * @param username Student username.
     * @param password Student password.
     */
    public StudentUser(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login(ExamManagementSystem system) {
        // Student login logic (can be extended)
        System.out.println("Student " + getUsername() + " logged in.");
        return true;
    }

    /**
     * Starts an exam session for the student.
     *
     * @param exam The exam to take.
     * @return The created ExamSession if exam is published and student hasn't already taken it, null otherwise.
     */
    public ExamSession startExam(Exam exam) {
        if (!exam.isPublished()) {
            System.out.println("Exam " + exam.getExamName() + " is not published yet.");
            return null;
        }
        if (hasAlreadyTakenExam(exam)) {
            System.out.println("You have already taken exam: " + exam.getExamName());
            return null;
        }

        ExamSession session = new ExamSession(this, exam);
        examSessions.add(session);
        exam.addExamSession(session); // Add session to exam's list
        session.start(); // Start the timer and question flow
        return session;
    }

    /**
     * Submits an exam session and generates the result.
     *
     * @param session The ExamSession to submit.
     * @return The ExamResult for the submitted session.
     */
    public ExamResult submitExam(ExamSession session) {
        if (session.isSubmitted()) {
            System.out.println("Exam already submitted.");
            return session.getExamResult();
        }
        return session.submit();
    }

    /**
     * Views results for a specific exam taken by the student.
     *
     * @param exam The exam to view results for.
     * @return The ExamResult if the student has taken the exam and it's available, null otherwise.
     */
    public ExamResult viewExamResult(Exam exam) {
        return examSessions.stream()
                .filter(session -> session.getExam().equals(exam))
                .map(ExamSession::getExamResult)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if the student has already taken a specific exam.
     *
     * @param exam The exam to check.
     * @return True if the student has taken the exam, false otherwise.
     */
    private boolean hasAlreadyTakenExam(Exam exam) {
        return examSessions.stream()
                .anyMatch(session -> session.getExam().equals(exam));
    }
}