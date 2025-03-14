package com.example.onlineexamsystem.model;

import com.example.onlineexamsystem.service.ExamSession;
import com.example.onlineexamsystem.user.AdminUser;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents an Exam, containing questions, duration, and settings.
 */
public class Exam {
    private final String examId;
    private final String examName;
    private final Duration duration;
    private final AdminUser creator;
    private final QuestionPool questionPool; // Optional question pool to draw questions from
    private final List<Question> questions = new ArrayList<>(); // Specific questions added to the exam
    private int randomQuestionCount = 0; // Number of random questions to pick from the pool
    private boolean isPublished = false;
    private final List<ExamSession> examSessions = new ArrayList<>(); // Track exam sessions for this exam


    /**
     * Constructor for Exam.
     *
     * @param examName      Name of the exam.
     * @param duration      Duration of the exam.
     * @param creator       AdminUser who created the exam.
     * @param questionPool  QuestionPool to use for random questions (optional).
     */
    public Exam(String examName, Duration duration, AdminUser creator, QuestionPool questionPool) {
        this.examId = UUID.randomUUID().toString();
        this.examName = examName;
        this.duration = duration;
        this.creator = creator;
        this.questionPool = questionPool;
    }

    public String getExamId() {
        return examId;
    }

    public String getExamName() {
        return examName;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public List<ExamSession> getExamSessions() {
        return examSessions;
    }

    /**
     * Adds an exam session to the list of sessions for this exam.
     *
     * @param session The ExamSession to add.
     */
    public void addExamSession(ExamSession session) {
        this.examSessions.add(session);
    }


    /**
     * Adds a specific question to the exam.
     *
     * @param question The question to add.
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    /**
     * Sets the number of random questions to be selected from the question pool.
     *
     * @param count Number of random questions.
     */
    public void setRandomQuestionCount(int count) {
        this.randomQuestionCount = count;
    }

    /**
     * Publishes the exam, making it available for students.
     */
    public void publish() {
        if (questions.isEmpty() && (questionPool == null || randomQuestionCount <= 0)) {
            System.out.println("Exam " + examName + " cannot be published without questions.");
            return;
        }
        this.isPublished = true;
    }

    /**
     * Gets the list of questions for the exam, including specific questions and random questions from the pool.
     *
     * @return List of questions for the exam.
     */
    public List<Question> getExamQuestions() {
        List<Question> examQuestions = new ArrayList<>(this.questions); // Start with specific questions

        if (questionPool != null && randomQuestionCount > 0) {
            List<Question> randomQuestions = questionPool.getRandomQuestions(randomQuestionCount, examQuestions);
            examQuestions.addAll(randomQuestions);
        }
        Collections.shuffle(examQuestions); // Randomize the order of all questions
        return examQuestions;
    }
}