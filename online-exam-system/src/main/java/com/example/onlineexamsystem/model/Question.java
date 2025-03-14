package com.example.onlineexamsystem.model;

/**
 * Abstract class representing a Question.
 */
public abstract class Question {
    private final String questionId;
    private final String questionText;
    private final int marks;

    /**
     * Constructor for Question.
     *
     * @param questionId   Unique ID for the question.
     * @param questionText The text of the question.
     * @param marks        Marks allocated for the question.
     */
    public Question(String questionId, String questionText, int marks) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.marks = marks;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getMarks() {
        return marks;
    }

    /**
     * Abstract method to check if the given answer is correct (implementation varies by question type).
     *
     * @param answer The student's answer.
     * @return Marks obtained for this question (0 if incorrect, full marks if correct for objective, graded for essay).
     */
    public abstract int checkAnswer(String answer);
}