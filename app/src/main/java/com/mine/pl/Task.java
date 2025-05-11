package com.mine.pl;

import java.util.List;

public class Task {
    private String id;
    private String title;
    private String description;
    private boolean completed;
    private List<Question> questions;

    public Task(String id,
                String title,
                String description,
                boolean completed,
                List<Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.questions = questions;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
