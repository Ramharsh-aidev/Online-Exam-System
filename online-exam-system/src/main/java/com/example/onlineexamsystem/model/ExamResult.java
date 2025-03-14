package com.example.onlineexamsystem.model;

import com.example.onlineexamsystem.service.ExamSession;

import java.util.UUID;

/**
 * Represents the result of an exam session.
 */
public class ExamResult {
    private final String resultId;
    private final ExamSession examSession;
    private final int score;
    private final int totalMarks;
    private String comments;
    private boolean isPublished = false;

    /**
     * Constructor for ExamResult.
     *
     * @param examSession The ExamSession this result belongs to.
     * @param score       The score obtained in the exam.
     * @param totalMarks  The total marks for the exam.
     */
    public ExamResult(ExamSession examSession, int score, int totalMarks) {
        this.resultId = UUID.randomUUID().toString();
        this.examSession = examSession;
        this.score = score;
        this.totalMarks = totalMarks;
    }

    public String getResultId() {
        return resultId;
    }

    public ExamSession getExamSession() {
        return examSession;
    }

    public int getScore() {
        return score;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public String getComments() {
        return comments;
    }

    public boolean isPublished() {
        return isPublished;
    }

    /**
     * Adds comments to the exam result (e.g., by admin for essay questions or feedback).
     *
     * @param comments Comments to add.
     */
    public void addComments(String comments) {
        this.comments = comments;
    }

    /**
     * Publishes the exam result, making it visible to students.
     */
    public void publishResult() {
        this.isPublished = true;
        System.out.println("Result published for student: " + examSession.getStudent().getUsername() + ", Exam: " + examSession.getExam().getExamName());
    }
}