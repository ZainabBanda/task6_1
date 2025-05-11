package com.mine.pl;

import java.util.List;

public class QuizResponse {
    private List<Question> quiz;

    public List<Question> getQuiz() {
        return quiz;
    }

    public void setQuiz(List<Question> quiz) {
        this.quiz = quiz;
    }
}
