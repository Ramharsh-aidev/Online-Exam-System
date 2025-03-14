package com.example.onlineexamsystem.model;

/**
 * Represents an Essay type question (free-text answer).
 */
public class EssayQuestion extends Question {

    /**
     * Constructor for EssayQuestion.
     *
     * @param questionId   Unique ID for the question.
     * @param questionText The text of the question.
     * @param marks        Marks allocated for the question.
     */
    public EssayQuestion(String questionId, String questionText, int marks) {
        super(questionId, questionText, marks);
    }

    // In a real system, Essay questions would require manual grading.
    // Here, we'll just return 0 initially, assuming manual grading later.
    @Override
    public int checkAnswer(String answer) {
        // Essay questions usually require manual grading.
        // Placeholder: return 0 marks initially. Admin would need to grade these later.
        System.out.println("Essay question '" + getQuestionText().substring(0, Math.min(getQuestionText().length(), 20)) + "...' needs manual grading.");
        return 0; // Placeholder - needs manual grading
    }

    // Method to simulate manual grading by admin (for demonstration)
    public int manualGrade(int awardedMarks) {
        if (awardedMarks >= 0 && awardedMarks <= getMarks()) {
            return awardedMarks;
        } else {
            System.out.println("Invalid marks for essay question. Marks should be between 0 and " + getMarks());
            return 0;
        }
    }

    @Override
    public String toString() {
        return "EssayQuestion{" +
               "questionId='" + getQuestionId() + '\'' +
               ", questionText='" + getQuestionText() + '\'' +
               ", marks=" + getMarks() +
               '}';
    }
}