package com.example.onlineexamsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Objective type question (e.g., Multiple Choice).
 */
public class ObjectiveQuestion extends Question {
    private final List<String> options;
    private final String correctAnswer;

    /**
     * Constructor for ObjectiveQuestion.
     *
     * @param questionId    Unique ID for the question.
     * @param questionText  The text of the question.
     * @param marks         Marks allocated for the question.
     * @param options       List of options for the question.
     * @param correctAnswer The correct answer.
     */
    public ObjectiveQuestion(String questionId, String questionText, int marks, List<String> options, String correctAnswer) {
        super(questionId, questionText, marks);
        this.options = new ArrayList<>(options); // Defensive copy
        this.correctAnswer = correctAnswer;
    }

    public List<String> getOptions() {
        return new ArrayList<>(options); // Return a copy
    }

    @Override
    public int checkAnswer(String answer) {
        if (answer != null && answer.trim().equalsIgnoreCase(correctAnswer)) {
            return getMarks();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "ObjectiveQuestion{" +
               "questionId='" + getQuestionId() + '\'' +
               ", questionText='" + getQuestionText() + '\'' +
               ", marks=" + getMarks() +
               ", options=" + options +
               ", correctAnswer='" + correctAnswer + '\'' +
               '}';
    }
}