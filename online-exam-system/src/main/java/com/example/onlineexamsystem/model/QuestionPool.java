package com.example.onlineexamsystem.model;

import com.example.onlineexamsystem.user.AdminUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a question pool, a collection of questions managed by an Admin.
 */
public class QuestionPool {
    private final String poolId;
    private final String poolName;
    private final AdminUser creator;
    private final List<Question> questions = new ArrayList<>();

    /**
     * Constructor for QuestionPool.
     *
     * @param poolName Name of the question pool.
     * @param creator  AdminUser who created the pool.
     */
    public QuestionPool(String poolName, AdminUser creator) {
        this.poolId = UUID.randomUUID().toString();
        this.poolName = poolName;
        this.creator = creator;
    }

    public String getPoolId() {
        return poolId;
    }

    public String getPoolName() {
        return poolName;
    }

    /**
     * Adds a question to the question pool.
     *
     * @param question The question to add.
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
        System.out.println("Question added to pool: " + poolName + ", Question ID: " + question.getQuestionId());
    }

    /**
     * Gets a question from the pool by its ID.
     *
     * @param questionId The ID of the question to retrieve.
     * @return The Question object if found, null otherwise.
     */
    public Question getQuestionById(String questionId) {
        return questions.stream()
                .filter(q -> q.getQuestionId().equals(questionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a specified number of random questions from the pool, excluding questions already in the exam.
     *
     * @param count             Number of random questions to retrieve.
     * @param excludedQuestions List of questions to exclude from random selection (e.g., already added to exam).
     * @return List of random questions.
     */
    public List<Question> getRandomQuestions(int count, List<Question> excludedQuestions) {
        List<Question> availableQuestions = questions.stream()
                .filter(q -> !excludedQuestions.contains(q))
                .collect(Collectors.toList());
        if (availableQuestions.size() <= count) {
            return new ArrayList<>(availableQuestions); // Return all available if not enough
        }
        Collections.shuffle(availableQuestions);
        return availableQuestions.subList(0, count);
    }
}