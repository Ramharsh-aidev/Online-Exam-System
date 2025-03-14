package com.example.onlineexamsystem.service;

import com.example.onlineexamsystem.model.*;
import com.example.onlineexamsystem.model.Timer;
import com.example.onlineexamsystem.user.StudentUser;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a student's session while taking an exam.
 */
public class ExamSession {
    private final String sessionId;
    private final StudentUser student;
    private final Exam exam;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private final Map<Question, String> studentAnswers = new LinkedHashMap<>(); // Keep order of questions
    private ExamResult examResult;
    private int currentQuestionIndex = 0;
    private List<Question> questionsForExam;
    private Timer examTimer;
    private final AtomicBoolean isSubmitted = new AtomicBoolean(false);


    /**
     * Constructor for ExamSession.
     *
     * @param student The student taking the exam.
     * @param exam    The exam being taken.
     */
    public ExamSession(StudentUser student, Exam exam) {
        this.sessionId = UUID.randomUUID().toString();
        this.student = student;
        this.exam = exam;
        this.startTime = LocalDateTime.now();
        this.questionsForExam = exam.getExamQuestions();
        if (this.questionsForExam.isEmpty()) {
            throw new IllegalStateException("Exam has no questions."); // Prevent starting session with no questions
        }
        this.examTimer = new Timer(exam.getDuration(), this::onExamTimeout);
    }

    public String getSessionId() {
        return sessionId;
    }

    public StudentUser getStudent() {
        return student;
    }

    public Exam getExam() {
        return exam;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Map<Question, String> getStudentAnswers() {
        return studentAnswers;
    }

    public ExamResult getExamResult() {
        return examResult;
    }

    public boolean isSubmitted() {
        return isSubmitted.get();
    }

    /**
     * Starts the exam session, including the timer.
     */
    public void start() {
        System.out.println("Exam session started for student: " + student.getUsername() + ", Exam: " + exam.getExamName());
        examTimer.start();
        displayCurrentQuestion();
    }

    /**
     * Gets the current question in the exam session.
     *
     * @return The current Question object.
     */
    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questionsForExam.size()) {
            return questionsForExam.get(currentQuestionIndex);
        }
        return null; // No more questions
    }

    /**
     * Displays the current question to the student (console output for this example).
     */
    private void displayCurrentQuestion() {
        Question currentQ = getCurrentQuestion();
        if (currentQ != null) {
            System.out.println("\nQuestion " + (currentQuestionIndex + 1) + "/" + questionsForExam.size() + ": " + currentQ.getQuestionText() + " (Marks: " + currentQ.getMarks() + ")");
            if (currentQ instanceof ObjectiveQuestion) {
                ObjectiveQuestion objectiveQ = (ObjectiveQuestion) currentQ;
                for (int i = 0; i < objectiveQ.getOptions().size(); i++) {
                    System.out.println((char)('A' + i) + ". " + objectiveQ.getOptions().get(i));
                }
            }
            System.out.println("Time Remaining: " + examTimer.getTimeRemainingFormatted());
        } else {
            System.out.println("Exam completed or no questions available.");
        }
    }

    /**
     * Moves to the next question in the exam session.
     */
    public void moveToNextQuestion() {
        currentQuestionIndex++;
        displayCurrentQuestion();
        if (getCurrentQuestion() == null) {
            System.out.println("End of questions in this session.");
        }
    }


    /**
     * Submits an answer for the current question.
     *
     * @param question The question being answered.
     * @param answer   The student's answer.
     */
    public void submitAnswer(Question question, String answer) {
        if (isSubmitted.get()) {
            System.out.println("Exam already submitted, cannot submit more answers.");
            return;
        }
        if (questionsForExam.contains(question)) {
            studentAnswers.put(question, answer);
            System.out.println("Answer submitted for question: " + question.getQuestionId());
        } else {
            System.out.println("Question is not part of this exam session.");
        }
    }


    /**
     * Submits the exam session, calculates score, and generates the ExamResult.
     *
     * @return The generated ExamResult.
     */
    public ExamResult submit() {
        if (isSubmitted.compareAndSet(false, true)) { // Ensure submit only once
            examTimer.stop();
            this.endTime = LocalDateTime.now();
            int totalScore = calculateScore();
            int totalMarks = questionsForExam.stream().mapToInt(Question::getMarks).sum();
            this.examResult = new ExamResult(this, totalScore, totalMarks);
            System.out.println("Exam submitted by student: " + student.getUsername() + ", Score: " + totalScore + "/" + totalMarks);
            return examResult;
        } else {
            System.out.println("Exam already submitted.");
            return examResult; // Return existing result if already submitted
        }
    }

    /**
     * Calculates the total score for the exam session.
     *
     * @return The total score obtained by the student.
     */
    private int calculateScore() {
        int score = 0;
        for (Map.Entry<Question, String> entry : studentAnswers.entrySet()) {
            score += entry.getKey().checkAnswer(entry.getValue());
        }
        return score;
    }

    /**
     * Callback method when the exam timer expires.
     */
    private void onExamTimeout() {
        System.out.println("\nTime's up! Exam timed out for student: " + student.getUsername() + ", Exam: " + exam.getExamName());
        if (!isSubmitted.get()) {
            submit(); // Automatically submit on timeout
        }
    }
}